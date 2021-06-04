package rough.samples.spring.boot.db.ds01.mapper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import rough.samples.spring.boot.db.ds01.model.TestBlob;
import rough.samples.spring.boot.test.DbTestApplication;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DbTestApplication.class)
public class TestBlobMapperTest {
    @Resource
    TestBlobMapper testBlobMapper;
    @Resource
    NamedParameterJdbcTemplate namedParameterJdbcTemplate01;

    @Before
    public void Before() {
        testBlobMapper.deleteByPrimaryKey(new BigDecimal(1));
        TestBlob testBlob = new TestBlob();
        testBlob.setTestBlobId(new BigDecimal(1));
        testBlob.setColBlob("测试数据".getBytes());
        testBlobMapper.insert(testBlob);
    }

    @Test
    public void testQuery() {
        TestBlob testBlob = testBlobMapper.selectByPrimaryKey(new BigDecimal(1));
        Assert.assertArrayEquals("测试数据".getBytes(), testBlob.getColBlob());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("TEST_BLOB_ID", new BigDecimal(1));
        testBlob = namedParameterJdbcTemplate01.queryForObject("SELECT * FROM TEST_BLOB WHERE TEST_BLOB_ID = :TEST_BLOB_ID",
                paramMap,
                (rs, rowNum) -> {
                    TestBlob result = new TestBlob();
                    result.setTestBlobId(rs.getBigDecimal(1));
                    result.setColBlob(rs.getBytes(2));
                    return result;
                });
        Assert.assertNotNull(testBlob);
        Assert.assertArrayEquals("测试数据".getBytes(), testBlob.getColBlob());
    }

    @After
    public void After() throws UnsupportedEncodingException {
        testBlobMapper.deleteByPrimaryKey(new BigDecimal(1));
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("TEST_BLOB_ID", new BigDecimal(1));
        paramMap.put("COL_BLOB", "测试数据".getBytes("GBK"));
        namedParameterJdbcTemplate01.update("INSERT INTO TEST_BLOB VALUES(:TEST_BLOB_ID, :COL_BLOB)", paramMap);
    }
}
