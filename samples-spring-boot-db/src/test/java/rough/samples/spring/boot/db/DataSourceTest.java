package rough.samples.spring.boot.db;

import lombok.extern.apachecommons.CommonsLog;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import rough.samples.spring.boot.TestApplication;

import javax.annotation.Resource;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
@CommonsLog
public class DataSourceTest {
    private static final String TEST_SQL = "SELECT COUNT(1) FROM DEMO_LOCK";

    @Resource(name = "namedParameterJdbcTemplate01")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate01;
    @Resource(name = "namedParameterJdbcTemplate02")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate02;

    @Test
    public void testDataSource01() {
        Integer result = namedParameterJdbcTemplate01.queryForObject(
                TEST_SQL, new HashMap<>(), Integer.class);
        log.info("Result of data source01 is: " + result);
        Assert.assertEquals(result, Integer.valueOf(0));
    }

    @Test
    public void testDataSource02() {
        Integer result = namedParameterJdbcTemplate02.queryForObject(
                TEST_SQL, new HashMap<>(), Integer.class);
        log.info("Result of data source02 is: " + result);
        Assert.assertEquals(result, Integer.valueOf(1));
    }
}
