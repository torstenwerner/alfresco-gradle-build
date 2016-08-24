package xyz.its_me.alfresco;

import org.alfresco.service.cmr.repository.NodeRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller("webscript.xyz.its_me.sample.get")
public class SampleWebscript extends org.springframework.extensions.webscripts.AbstractWebScript {
    private final SampleService sampleService;

    @Autowired
    public SampleWebscript(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @Override
    public void execute(WebScriptRequest req, WebScriptResponse res) throws IOException {
        final NodeRef rootNode = sampleService.getRoot();
        res.getWriter().write(rootNode.toString());
    }
}
