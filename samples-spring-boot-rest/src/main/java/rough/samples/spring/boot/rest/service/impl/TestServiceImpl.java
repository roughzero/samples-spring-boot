package rough.samples.spring.boot.rest.service.impl;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import rough.samples.spring.boot.db.ds01.mapper.SplUserMapper;
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
        // throw new RuntimeException();
    }
}
