package rough.samples.spring.boot.rest.service.impl;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import rough.samples.spring.boot.db.ds01.mapper.SplUserMapper;
import rough.samples.spring.boot.db.ds01.mapper.ex.SplUserExMapper;
import rough.samples.spring.boot.db.ds01.model.SplUser;
import rough.samples.spring.boot.rest.service.TestService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;

@CommonsLog
@Service
public class TestServiceImpl implements TestService {
    @Resource(name = "namedParameterJdbcTemplate01")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate01;
    @Resource(name = "namedParameterJdbcTemplate02")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate02;
    @Resource(name = "sqlSessionFactory01")
    private SqlSessionFactory sqlSessionFactory01;
    @Resource(name = "batchSqlSession01")
    private SqlSession batchSqlSession01;
    @Resource
    private SplUserExMapper splUserExMapper;

    @Override
    public void testTransaction() {
        String sql = "INSERT INTO DEMO_LOCK VALUES (\n" +
                "    1, 'Test', 'Test', 'ROUGH', Sysdate\n" +
                ")";
        int result = namedParameterJdbcTemplate01.update(sql, new HashMap<>());
        log.info("result: " + result);

        sql = "INSERT INTO DEMO_LOCK VALUES (\n" +
                "    2, 'Test', 'Test', 'ROUGH', Sysdate\n" +
                ")";
        result = namedParameterJdbcTemplate02.update(sql, new HashMap<>());
        log.info("result: " + result);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error(e, e);
        }

        SplUser splUser = new SplUser();
        splUser.setUserId("test");
        splUser.setUserCode("test");
        splUser.setUserName("test");
        splUser.setCreateTime(new Date(System.currentTimeMillis()));

        SqlSession sqlSession = sqlSessionFactory01.openSession(ExecutorType.BATCH);
        SplUserMapper splUserMapper = sqlSession.getMapper(SplUserMapper.class);

        splUserMapper.insert(splUser);
        splUserMapper.insert(splUser);

        sqlSession.flushStatements();
        throw new RuntimeException();
    }

    public void testBatchTransaction() {
        // 如果注释下面这句，则会报 "Cannot change the ExecutorType when there is an existing transaction"
        // 结论，使用 new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH); 创建的 SqlSession()，
        // 可以通过在事务中使用 getMapper 方法来处理数据，不会报更换执行类型的错误。
        // 但是，无法起到批量处理的效果，实测不需要使用 flushStatements 方法即可提交，疑似自动提交，故批量插入速度未有提高。
        // 应该是要有一个配置选项在这里，没有找到，暂时放弃。
        // 目前，仍需要使用先用 SqlSessionFactory 生产出 SqlSession，然后再 getMapper，
        // 且在处理完批处理后需要调用 flushStatements 方法的批量调用模式。
        SplUserExMapper splUserExMapper = batchSqlSession01.getMapper(SplUserExMapper.class);
        splUserExMapper.selectByUserCode("test");

        SplUser splUser = new SplUser();
        splUser.setUserId("test");
        splUser.setUserCode("test");
        splUser.setUserName("test");
        splUser.setCreateTime(new Date(System.currentTimeMillis()));

        SplUserMapper splUserMapper = batchSqlSession01.getMapper(SplUserMapper.class);

        splUserMapper.insert(splUser);
        splUserMapper.insert(splUser);

        //batchSqlSession01.flushStatements();
    }
}
