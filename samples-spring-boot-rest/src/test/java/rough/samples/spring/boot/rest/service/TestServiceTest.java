package rough.samples.spring.boot.rest.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import rough.samples.spring.boot.TestApplication;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
@Slf4j
public class TestServiceTest {
    @Resource
    private TestService testService;

    @Test
    public void testTransaction() {
        try {
            testService.testTransaction();
        } catch (Throwable e) {
            log.info("Catch exception for testTransaction() to check.");
        }
    }
}
