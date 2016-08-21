package xyz.its_me.alfresco;

import org.alfresco.service.cmr.repository.NodeRef;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import xyz.its_me.TomcatLauncher;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

public class SampleTomcatTest extends AbstractTomcatTest {
    @Autowired
    private SampleService sampleService;

    @Test
    public void serverIsUp() throws Exception {
        final String url = BASE_URL + "/s/api/server";
        final Map response = restTemplate.getForObject(url, Map.class);

        final Map data = (Map) response.get("data");
        assertThat(data.get("edition"), is("Community"));
        assertThat((String) data.get("version"), startsWith("5.2"));
    }

    @Test
    public void sampleWebscript() throws Exception {
        final String url = BASE_URL + "/s/sample";
        final String response = restTemplate.getForObject(url, String.class);

        assertThat(response, startsWith("workspace://SpacesStore/"));

        final NodeRef rootNode = sampleService.getRoot();
        assertThat(response, is(rootNode.toString()));
    }
}
