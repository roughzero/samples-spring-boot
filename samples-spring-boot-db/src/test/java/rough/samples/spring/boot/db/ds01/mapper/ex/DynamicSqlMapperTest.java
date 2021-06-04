package rough.samples.spring.boot.db.ds01.mapper.ex;

import lombok.extern.apachecommons.CommonsLog;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import rough.samples.spring.boot.db.dto.DynamicSqlDto;
import rough.samples.spring.boot.test.DbTestApplication;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DbTestApplication.class)
@CommonsLog
@SuppressWarnings({"rawtypes", "unused"})
public class DynamicSqlMapperTest {

    @Resource
    private DynamicSqlMapper dynamicSqlMapper;

    @Test
    public void testDynamicQuery() {
        String sql = "SELECT 1 AS CNT, SYSDATE AS CURRENT_TIME FROM DUAL";
        {
            List<Map<String, Object>> mapResults = dynamicSqlMapper.queryForMapList(sql);
            Assert.assertNotNull(mapResults);
            Assert.assertEquals(1, mapResults.size());
            Map<String, Object> mapResult = mapResults.get(0);
            Assert.assertEquals(new BigDecimal(1), mapResult.get("CNT"));
        }

        DynamicSqlDto sqlDto = new DynamicSqlDto();
        sqlDto.setSql(sql);

        {
            List<Object> results = dynamicSqlMapper.queryForList(sqlDto);
            Assert.assertNotNull(results);
            Assert.assertEquals(1, results.size());
            Object result = results.get(0);
            Assert.assertTrue(result instanceof Map);
            Assert.assertEquals(new BigDecimal(1), ((Map) result).get("CNT"));
        }

        sqlDto.setResultType(QueryResultDto.class);
        {
            List<Object> results = dynamicSqlMapper.queryForList(sqlDto);
            Assert.assertNotNull(results);
            Assert.assertEquals(1, results.size());
            Object result = results.get(0);
            Assert.assertTrue(result instanceof QueryResultDto);
            Assert.assertEquals(1L, ((QueryResultDto) result).getCnt().longValue());
        }

        sqlDto.setSql("SELECT 2 AS CNT, SYSDATE AS CURRENT_TIME FROM DUAL");
        {
            List<Object> results = dynamicSqlMapper.queryForList(sqlDto);
            Assert.assertNotNull(results);
            Assert.assertEquals(1, results.size());
            Object result = results.get(0);
            Assert.assertTrue(result instanceof QueryResultDto);
            Assert.assertEquals(2L, ((QueryResultDto) result).getCnt().longValue());
        }

        // 使用匿名类传入额外参数
        sql = "SELECT #{cnt} AS CNT, SYSDATE AS CURRENT_TIME FROM DUAL";
        {
            List<Object> results = dynamicSqlMapper.queryForList(new DynamicSqlDto(sql, QueryResultDto.class) {
                public int getCnt() {
                    return 3;
                }
            });
            Assert.assertNotNull(results);
            Assert.assertEquals(1, results.size());
            Object result = results.get(0);
            Assert.assertTrue(result instanceof QueryResultDto);
            Assert.assertEquals(3L, ((QueryResultDto) result).getCnt().longValue());
        }

    }
}
