package rough.samples.spring.boot.db;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "samples.spring.boot.jndi-datasource")
@Getter
@Setter
public class JndiConfigure {
    private String datasource01;
    private String datasource02;
}
