package xyz.its_me.alfresco;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import xyz.its_me.TomcatLauncher;

public abstract class AbstractTomcatTest {
    // The test port should be a little bit random to avoid conflicts with other programs.
    private static final String TOMCAT_PORT = "9274";
    private static final String BASE_URL = "http://localhost:" + TOMCAT_PORT + "/alfresco";

    private static final TomcatLauncher launcher;
    private static AutowireCapableBeanFactory beanFactory;

    static {
        System.setProperty("tomcat.port", TOMCAT_PORT);
        launcher = TomcatLauncher.create();
    }

    private final RestOperations restTemplate = new RestTemplate();

    @BeforeClass
    public static void setupClass() {
        launcher.start();
        launcher.waitForStart();

        final WebApplicationContext applicationContext =
                WebApplicationContextUtils.getWebApplicationContext(launcher.getServletContext());
        beanFactory = applicationContext.getAutowireCapableBeanFactory();
    }

    @AfterClass
    public static void teardownClass() {
        launcher.stop();
        launcher.waitForStop();
    }

    @Before
    public void setup() {
        beanFactory.autowireBean(this);
    }

    public String getUrl(String path) {
        return BASE_URL + path;
    }

    public RestOperations getRestTemplate() {
        return restTemplate;
    }
}
