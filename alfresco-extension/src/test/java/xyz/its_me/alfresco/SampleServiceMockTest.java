package xyz.its_me.alfresco;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class SampleServiceMockTest {
    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    private NodeService nodeService;

    @Test
    public void testRoot() throws Exception {
        final NodeRef rootNode = new NodeRef("workspace://SpacesStore/" + UUID.randomUUID());
        when(nodeService.getRootNode(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE)).thenReturn(rootNode);

        final SampleService sampleService = new DefaultSampleService(nodeService);

        assertThat(sampleService.getRoot(), is(rootNode));
    }
}
