package xyz.its_me.alfresco;

import org.alfresco.query.PagingRequest;
import org.alfresco.query.PagingResults;
import org.alfresco.repo.model.Repository;
import org.alfresco.repo.transaction.RetryingTransactionHelper;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.transaction.TransactionService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

import static java.lang.String.format;
import static org.alfresco.model.ContentModel.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileFolderServiceSpringTest extends AbstractSpringTest {
    @Autowired
    @Qualifier("fileFolderService")
    private FileFolderService fileFolderService;

    @Autowired
    @Qualifier("nodeService")
    private NodeService nodeService;

    @Autowired
    @Qualifier("transactionService")
    private TransactionService transactionService;

    @Autowired
    private Repository repository;

    private static NodeRef testFolder;

    @Before
    public void before() {
        if (testFolder != null) {
            return;
        }
        final NodeRef companyHome = repository.getCompanyHome();
        assertThat(companyHome, notNullValue());
        testFolder = fileFolderService.searchSimple(companyHome, "testFolder");
        if (testFolder != null) {
            return;
        }
        final FileInfo fileInfo = runTransactional(() -> fileFolderService.create(companyHome, "testFolder", TYPE_FOLDER));
        testFolder = fileInfo.getNodeRef();
        for (int i = 0; i < 10000; i++) {
            final NodeRef nodeRef = createFile(i);
            assertThat(nodeRef, notNullValue());
        }
    }

    /**
     * Runs before other tests and triggers the initialization.
     */
    @Test
    public void test00Context() throws Exception {
        assertThat(nodeService, notNullValue());
    }

    @Test
    public void test10FolderCreated() throws Exception {
        assertThat(testFolder, notNullValue());
        assertThat(nodeService.getProperty(testFolder, PROP_NAME), is("testFolder"));
    }

    @Test
    public void test20SearchSimple() throws Exception {
        final NodeRef lastTestFile = fileFolderService.searchSimple(testFolder, "test09999.txt");
        assertThat(lastTestFile, notNullValue());
    }

    @Test
    public void test30Search() throws Exception {
        // uses solr search
        final List<FileInfo> hundredFiles = fileFolderService.search(testFolder, "test099*.txt", false);
        assertThat(hundredFiles.size(), is(100));
        final String firstName = nodeService.getProperty(hundredFiles.get(0).getNodeRef(), PROP_NAME).toString();
        assertThat(firstName, startsWith("test099"));
    }

    @Test
    public void test40ListFiles() throws Exception {
        final List<FileInfo> incompleteFileInfos = fileFolderService.listFiles(testFolder);
        // should be 10000
        assertThat(incompleteFileInfos.size(), is(5000));
    }

    @Test
    public void test90Paging() throws Exception {
        final PagingRequest pagingRequest = new PagingRequest(101);
        final PagingResults<FileInfo> pagingResults =
                fileFolderService.list(testFolder, true, false, "test*99.txt", null, null, pagingRequest);
        final List<FileInfo> page = pagingResults.getPage();
        assertThat(page.size(), is(100));
    }

    private NodeRef createFile(int index) {
        final String name = format("test%05d.txt", index);
        final FileInfo fileInfo = runTransactional(() -> fileFolderService.create(testFolder, name, TYPE_CONTENT));
        return fileInfo.getNodeRef();
    }

    /**
     * Helper for running code in a retrying transaction.
     *
     * @param <R> return type for callback
     */
    private <R> R runTransactional(RetryingTransactionHelper.RetryingTransactionCallback<R> callback) {
        return transactionService.getRetryingTransactionHelper().doInTransaction(callback);
    }

    /**
     * Helper for running code in a retrying transaction with no return value.
     */
    private void runTransactional(Runnable callback) {
        transactionService.getRetryingTransactionHelper().doInTransaction(() -> {
            callback.run();
            return null;
        });
    }
}
