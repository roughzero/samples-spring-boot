package rough.samples.spring.boot.db;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSourceConfigureDTO {
    private String url;
    private String username;
    private String password;
}
