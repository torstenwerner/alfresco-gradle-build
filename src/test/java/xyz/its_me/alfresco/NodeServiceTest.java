package xyz.its_me.alfresco;

import org.alfresco.repo.web.scripts.TestWebScriptRepoServer;
import org.alfresco.service.cmr.repository.NodeService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.extensions.webscripts.TestWebScriptServer;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class NodeServiceTest {
    private NodeService nodeService;

    @Before
    public void setUp() throws Exception {
        final TestWebScriptServer server = TestWebScriptRepoServer.getTestServer();
        final ApplicationContext context = server.getApplicationContext();
        nodeService = context.getBean("nodeService", NodeService.class);
    }

    @Test
    public void testContext() {
        assertThat(nodeService, notNullValue());
    }
}
