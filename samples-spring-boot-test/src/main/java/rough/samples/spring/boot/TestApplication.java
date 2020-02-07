package rough.samples.spring.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("rough")
@Slf4j
public class TestApplication {
    public static void main(String[] args) {
        log.info("Start test.");
        SpringApplication.run(TestApplication.class, args);
    }
}
