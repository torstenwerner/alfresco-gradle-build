package xyz.its_me.alfresco;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class NodeServiceTest extends AbstractSpringTest {
    @Autowired
    @Qualifier("nodeService")
    private NodeService nodeService;

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
