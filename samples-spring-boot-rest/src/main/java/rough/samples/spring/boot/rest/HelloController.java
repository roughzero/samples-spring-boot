package rough.samples.spring.boot.rest;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import rough.samples.spring.boot.rest.service.TestService;

import javax.annotation.Resource;
import java.util.HashMap;

@RestController
@CommonsLog
public class HelloController {

    @Resource(name = "namedParameterJdbcTemplate01")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate01;
    @Resource(name = "namedParameterJdbcTemplate02")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate02;
    @Resource
    private TestService testService;

    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {
        log.info("Say hello.");
        String sql = "SELECT COUNT(1) FROM DEMO_LOCK";
        Integer result = namedParameterJdbcTemplate01.queryForObject(sql,
                new HashMap<>(), Integer.class);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" Result of data source 01 is : ").append(result);

        result = namedParameterJdbcTemplate02.queryForObject(sql,
                new HashMap<>(), Integer.class);
        stringBuilder.append(", Result of data source 02 is : ").append(result);

        try {
            testService.testTransaction();
        } catch (Throwable e) {
            log.info("Catch exception for testTransaction() to check.");
        }

        return "Hello!" + stringBuilder;
    }

}
