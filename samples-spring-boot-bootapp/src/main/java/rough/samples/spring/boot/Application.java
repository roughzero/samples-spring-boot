package rough.samples.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;

import lombok.extern.apachecommons.CommonsLog;

@SpringBootApplication
@ComponentScan("rough")
@EnableScheduling
@CommonsLog
@Configuration
@EnableConfigurationProperties
@EnableTransactionManagement
public class Application extends SpringBootServletInitializer
        implements WebApplicationInitializer {
    // 这边的继承以及实现是为了兼容 weblogic.

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        log.info("Start application.");
        SpringApplication.run(Application.class, args);
    }

}
