package xyz.its_me.alfresco;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DefaultSampleService implements SampleService {
    private final NodeService nodeService;

    @Autowired
    public DefaultSampleService(@Qualifier("nodeService") NodeService nodeService) {
        this.nodeService = nodeService;
        System.out.printf("nodeService: %s\n", nodeService);
    }

    @Override
    public NodeRef getRoot() {
        return nodeService.getRootNode(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE);
    }
}
