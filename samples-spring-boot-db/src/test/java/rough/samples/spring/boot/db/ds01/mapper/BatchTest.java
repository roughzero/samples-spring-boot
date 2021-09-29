package rough.samples.spring.boot.db.ds01.mapper;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rough.samples.spring.boot.db.ds01.model.SplUser;
import rough.samples.spring.boot.test.DbTestApplication;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DbTestApplication.class)
@CommonsLog
public class BatchTest {
    private static final int TEST_SIZE = 1000;

    @Resource
    private SplUserMapper splUserMapper;
    @Resource(name = "sqlSessionFactory01")
    private SqlSessionFactory sqlSessionFactory01;
    @Resource(name = "batchSqlSession01")
    private SqlSession batchSqlSession01;

    @Test
    public void test() {
        cleanTestData();
        testNormalInsert();
        cleanTestDataBatch();
        testBatchInsert();
        cleanTestData();
        testNormalInsert();
        cleanTestDataBatch();
        testBatchInsert();
        cleanTestData();
        testNormalInsert();
        cleanTestDataBatch();
        testBatchInsert();
    }

    @Transactional
    public void testNormalInsert() {
        long start = System.currentTimeMillis();
        NumberFormat format = new DecimalFormat("TEST_000000");
        for (int i = 0; i < TEST_SIZE; i++) {
            String userId = format.format(i);
            SplUser user = new SplUser();
            user.setUserId(userId);
            user.setUserCode(userId);
            splUserMapper.insert(user);
        }
        long cost = System.currentTimeMillis() - start;
        log.info("NormalInsert cost: " + cost + " milliseconds.");
    }

    @Transactional
    public void testBatchInsert() {
        long start = System.currentTimeMillis();
        SqlSession sqlSession = sqlSessionFactory01.openSession(ExecutorType.BATCH);
        SplUserMapper mapper = sqlSession.getMapper(SplUserMapper.class);
        NumberFormat format = new DecimalFormat("TEST_000000");
        for (int i = 0; i < TEST_SIZE; i++) {
            String userId = format.format(i);
            SplUser user = new SplUser();
            user.setUserId(userId);
            user.setUserCode(userId);
            mapper.insert(user);
        }
        sqlSession.flushStatements();
        long cost = System.currentTimeMillis() - start;
        log.info("BatchInsert cost: " + cost + " milliseconds.");
    }


    @Transactional
    public void cleanTestData() {
        long start = System.currentTimeMillis();
        SplUserMapper mapper = batchSqlSession01.getMapper(SplUserMapper.class);
        NumberFormat format = new DecimalFormat("TEST_000000");
        int deleted;
        for (int i = 0; i < TEST_SIZE; i++) {
            String userId = format.format(i);
            mapper.deleteByPrimaryKey(userId);
        }
        List<BatchResult> batchResultList = batchSqlSession01.flushStatements();
        deleted = batchResultList.stream().mapToInt(batchResult -> Arrays.stream(batchResult.getUpdateCounts()).sum()).sum();
        long cost = System.currentTimeMillis() - start;
        log.info("CleanTestData cost: " + cost + " milliseconds. deleted " + deleted);
    }

    public void cleanTestDataBatch() {
        long start = System.currentTimeMillis();
        SqlSession sqlSession = sqlSessionFactory01.openSession(ExecutorType.BATCH);
        SplUserMapper mapper = sqlSession.getMapper(SplUserMapper.class);
        NumberFormat format = new DecimalFormat("TEST_000000");
        int deleted;
        for (int i = 0; i < TEST_SIZE; i++) {
            String userId = format.format(i);
            mapper.deleteByPrimaryKey(userId);
        }
        List<BatchResult> batchResultList = sqlSession.flushStatements();
        deleted = batchResultList.stream().mapToInt(batchResult -> Arrays.stream(batchResult.getUpdateCounts()).sum()).sum();
        long cost = System.currentTimeMillis() - start;
        log.info("CleanTestDataBatch cost: " + cost + " milliseconds. deleted " + deleted);
    }
}
