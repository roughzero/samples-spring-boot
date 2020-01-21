package rough.samples.spring.boot.mybatis.simple;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan("rough")
@EnableScheduling
@CommonsLog
@Configuration
@EnableConfigurationProperties
@EnableTransactionManagement
public class Application {
    public static void main(String[] args) {
        log.info("Start application.");
        SpringApplication.run(Application.class, args);
    }
}
