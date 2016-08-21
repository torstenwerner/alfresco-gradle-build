package xyz.its_me.alfresco;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SampleServiceSpringTest extends AbstractSpringTest {
    @Autowired
    private SampleService sampleService;

    @Test
    public void testRoot() throws Exception {
        assertThat(sampleService.getRoot(), notNullValue());
    }
}
