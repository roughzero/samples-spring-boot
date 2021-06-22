package rough.samples.spring.boot.db;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "samples.spring.boot.jndi-datasource")
@Getter
@Setter
@SuppressWarnings("unused")
public class JndiConfigure {
    private String datasource01;
    private String datasource02;
}
