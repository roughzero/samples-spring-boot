package rough.samples.spring.boot.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = "rough", excludeFilters = @ComponentScan.Filter(
        type = FilterType.ANNOTATION, classes = {SpringBootApplication.class})
)
public class MybatisTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisTestApplication.class, args);
    }
}
