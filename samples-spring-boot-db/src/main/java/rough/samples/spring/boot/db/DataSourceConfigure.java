package rough.samples.spring.boot.db;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import rough.samples.spring.boot.db.plugin.FileTypePlugin;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@CommonsLog
@MapperScan(value = {"rough.samples.spring.boot.db.ds01.mapper"},
        sqlSessionFactoryRef = "sqlSessionFactory01")
@MapperScan(value = {"rough.samples.spring.boot.db.ds02.mapper"},
        sqlSessionFactoryRef = "sqlSessionFactory02")
@ImportResource("classpath:/transaction-config.xml")
@SuppressWarnings("unused")
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

    @Bean(name = "dataSourceTransactionManager01")
    public DataSourceTransactionManager dataSourceTransactionManager01(@Qualifier("dataSource01") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "namedParameterJdbcTemplate01")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate01(@Qualifier("dataSource01") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean(name = "sqlSessionFactory01")
    @Qualifier("sqlSessionFactory01")
    public SqlSessionFactory sqlSessionFactory01(@Qualifier("dataSource01") DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setPlugins(new FileTypePlugin());
        return sqlSessionFactoryBean.getObject();
    }

    @Bean("sqlSessionTemplate01")
    public SqlSessionTemplate sqlSessionTemplate01(@Qualifier("sqlSessionFactory01") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean("batchSqlSession01")
    public SqlSession batchSqlSession01(@Qualifier("sqlSessionFactory01") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
        //return sqlSessionFactory.openSession(ExecutorType.BATCH);
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
            log.info("Use direct connect for data source 02.");
            return DataSourceBuilder.create().build();
        }
    }

    @Bean(name = "dataSourceTransactionManager02")
    @Primary
    public DataSourceTransactionManager dataSourceTransactionManager02(@Qualifier("dataSource02") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "namedParameterJdbcTemplate02")
    @Primary
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate02(@Qualifier("dataSource02") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean(name = "sqlSessionFactory02")
    @Qualifier("sqlSessionFactory02")
    public SqlSessionFactory sqlSessionFactory02(@Qualifier("dataSource02") DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setPlugins(new FileTypePlugin());
        return sqlSessionFactoryBean.getObject();
    }

    @Bean("sqlSessionTemplate02")
    public SqlSessionTemplate sqlSessionTemplate02(@Qualifier("sqlSessionFactory02") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
