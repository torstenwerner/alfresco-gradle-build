package xyz.its_me.alfresco;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.model.Repository;
import org.alfresco.repo.transaction.RetryingTransactionHelper;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.transaction.TransactionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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

    private NodeRef testFolder;

    @Before
    public void before() {
        final NodeRef companyHome = repository.getCompanyHome();
        assertThat(companyHome, notNullValue());
        final FileInfo fileInfo = runTransactional(
                () -> fileFolderService.create(companyHome, "testFolder", ContentModel.TYPE_FOLDER));
        this.testFolder = fileInfo.getNodeRef();
    }

    @After
    public void after() {
        if (testFolder != null) {
            runTransactional(() -> fileFolderService.delete(testFolder));
        }
    }

    @Test
    public void testCreateFolder() throws Exception {
        assertThat(testFolder, notNullValue());

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
