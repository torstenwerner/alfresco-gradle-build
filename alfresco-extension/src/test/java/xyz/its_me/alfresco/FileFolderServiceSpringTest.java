package xyz.its_me.alfresco;

import org.alfresco.repo.model.Repository;
import org.alfresco.repo.transaction.RetryingTransactionHelper;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.transaction.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static java.lang.String.format;
import static org.alfresco.model.ContentModel.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

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

    @Test
    public void testFolderCreated() throws Exception {
        assertThat(testFolder, notNullValue());
        assertThat(nodeService.getProperty(testFolder, PROP_NAME), is("testFolder"));
    }

    @Test
    public void testFilesCreated() throws Exception {
        assertThat(fileFolderService.searchSimple(testFolder, "test09999.txt"), notNullValue());
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
