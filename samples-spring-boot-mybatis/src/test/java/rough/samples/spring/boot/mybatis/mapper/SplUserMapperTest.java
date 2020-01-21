package rough.samples.spring.boot.mybatis.mapper;

import lombok.extern.apachecommons.CommonsLog;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import rough.samples.spring.boot.mybatis.Application;
import rough.samples.spring.boot.mybatis.model.SplUser;

import javax.annotation.Resource;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
@CommonsLog
public class SplUserMapperTest {
    @Resource
    private SplUserMapper mapper;

    @Before
    public void prepareTest() {
        mapper.deleteByPrimaryKey("001");
        SplUser user = new SplUser();
        user.setUserId("001");
        user.setUserCode("TEST_001");
        user.setUserName("测试用户");
        user.setCreateTime(new Date());
        mapper.insert(user);
    }

    @Test
    public void testMapper() {
        SplUser user = mapper.selectByPrimaryKey("001");
        Assert.assertNotNull(user);
        Assert.assertEquals("001", user.getUserId());
        Assert.assertEquals("TEST_001", user.getUserCode());
        Assert.assertEquals("测试用户", user.getUserName());
    }
}
