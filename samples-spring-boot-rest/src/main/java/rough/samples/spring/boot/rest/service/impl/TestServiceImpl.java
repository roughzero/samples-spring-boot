package rough.samples.spring.boot.rest.service.impl;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import rough.samples.spring.boot.rest.service.TestService;

import javax.annotation.Resource;
import java.util.HashMap;

@CommonsLog
@Service
public class TestServiceImpl implements TestService {
    @Resource(name = "namedParameterJdbcTemplate02")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void testTransaction() {
        String sql = "INSERT INTO DEMO_LOCK VALUES (\n" +
                "    -1, 'Test', 'Test', 'ROUGH', Sysdate\n" +
                ")";
        int result = namedParameterJdbcTemplate.update(sql, new HashMap<>());
        log.info("result: " + result);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error(e, e);
        }

        throw new RuntimeException();
    }
}
