package xyz.its_me.alfresco;

import org.alfresco.service.cmr.repository.NodeService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class NodeServiceTest {
    private static final String CONFIG_LOCATION = "classpath:alfresco/application-context.xml";
    private static final ApplicationContext context = new ClassPathXmlApplicationContext(CONFIG_LOCATION);
    private NodeService nodeService = context.getBean("nodeService", NodeService.class);

    @Test
    public void testContext() {
        assertThat(nodeService, notNullValue());
    }
}
