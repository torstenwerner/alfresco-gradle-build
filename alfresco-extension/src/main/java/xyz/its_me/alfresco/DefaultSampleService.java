package xyz.its_me.alfresco;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static org.alfresco.repo.security.authentication.AuthenticationUtil.runAsSystem;

@Service
public class DefaultSampleService implements SampleService {
    private final NodeService nodeService;

    @Autowired
    public DefaultSampleService(@Qualifier("NodeService") NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @Override
    public NodeRef getRoot() {
        return runAsSystem(() -> nodeService.getRootNode(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE));
    }
}
