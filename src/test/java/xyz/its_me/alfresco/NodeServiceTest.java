package xyz.its_me.alfresco;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class NodeServiceTest {
    private static final String CONFIG_LOCATION = "classpath:alfresco/application-context.xml";
    private static final ApplicationContext context = new ClassPathXmlApplicationContext(CONFIG_LOCATION);
    private NodeService nodeService = context.getBean("nodeService", NodeService.class);

    @Test
    public void testContextStarts() {
        assertThat(nodeService, notNullValue());
    }

    @Test
    public void testNodeExistsNullCheck() {
        try {
            nodeService.exists((NodeRef) null);
            fail("expected exception has not been thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("nodeRef is a mandatory parameter"));
        }
    }

    @Test
    public void testRandomNodeRefNotExists() throws Exception {
        final NodeRef nodeRef = new NodeRef("workspace://SpacesStore/" + UUID.randomUUID());
        assertFalse(nodeService.exists(nodeRef));
    }

    @Test
    public void testStoreRootExists() throws Exception {
        assertTrue(nodeService.exists(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE));
    }
}
