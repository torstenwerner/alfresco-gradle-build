package xyz.its_me.alfresco;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import xyz.its_me.TomcatLauncher;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

public class HttpTest {
    private static final String TOMCAT_PORT = "9274";
    private static final String BASE_URL = "http://localhost:" + TOMCAT_PORT + "/alfresco";

    private static final TomcatLauncher launcher;

    static {
        // The test port should be a little bit random to avoid conflicts with other programs.
        System.setProperty("tomcat.port", TOMCAT_PORT);
        launcher = TomcatLauncher.create();
    }

    private final RestTemplate restTemplate = new RestTemplate();

    @BeforeClass
    public static void setupClass() {
        launcher.start();
        launcher.waitForStart();
    }

    @AfterClass
    public static void teardownClass() {
        launcher.stop();
        launcher.waitForStop();
    }

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
    }
}
