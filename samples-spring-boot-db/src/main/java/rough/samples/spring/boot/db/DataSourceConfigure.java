package rough.samples.spring.boot.db;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@CommonsLog
public class DataSourceConfigure {
    @Resource
    private JndiConfigure jndiConfigure;

    @Bean(name = "dataSource01")
    @Qualifier("dataSource01")
    @ConfigurationProperties(prefix = "samples.spring.boot.datasource01")
    public DataSource dataSource01() {
        if (StringUtils.isNotEmpty(jndiConfigure.getDatasource01())) {
            log.info("Jndi name of data source 01: " + jndiConfigure.getDatasource01());
            JndiDataSourceLookup lookup = new JndiDataSourceLookup();
            lookup.setResourceRef(true);
            return lookup.getDataSource(jndiConfigure.getDatasource01());
        } else {
            log.info("Use direct connect for data source 01.");
            return DataSourceBuilder.create().build();
        }
    }

    @Bean(name = "namedParameterJdbcTemplate01")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate01(@Qualifier("dataSource01") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean(name = "dataSource02")
    @Qualifier("dataSource02")
    @ConfigurationProperties(prefix = "samples.spring.boot.datasource02")
    @Primary
    public DataSource dataSource02() {
        if (StringUtils.isNotEmpty(jndiConfigure.getDatasource02())) {
            log.info("Jndi name of data source 02: " + jndiConfigure.getDatasource02());
            JndiDataSourceLookup lookup = new JndiDataSourceLookup();
            lookup.setResourceRef(true);
            return lookup.getDataSource(jndiConfigure.getDatasource02());
        } else {
            log.info("Use direct connect for data source 01.");
            return DataSourceBuilder.create().build();
        }
    }

    @Bean(name = "namedParameterJdbcTemplate02")
    @Primary
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate02(@Qualifier("dataSource02") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
