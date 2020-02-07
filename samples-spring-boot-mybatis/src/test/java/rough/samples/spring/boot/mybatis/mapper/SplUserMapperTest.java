package rough.samples.spring.boot.mybatis.mapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import rough.samples.spring.boot.TestApplication;
import rough.samples.spring.boot.mybatis.mapper.ex.SplUserExMapper;
import rough.samples.spring.boot.mybatis.model.SplUser;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
public class SplUserMapperTest {
    @Resource
    private SplUserMapper mapper;
    @Resource
    private SplUserExMapper exMapper;

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

        user = exMapper.selectByUserCode("TEST_001");
        Assert.assertNotNull(user);
        Assert.assertEquals("001", user.getUserId());
        Assert.assertEquals("TEST_001", user.getUserCode());
        Assert.assertEquals("测试用户", user.getUserName());

        List<SplUser> users = exMapper.selectByUserName("测试用户");
        Assert.assertFalse(users.isEmpty());
        user = users.get(0);
        Assert.assertNotNull(user);
        Assert.assertEquals("001", user.getUserId());
        Assert.assertEquals("TEST_001", user.getUserCode());
        Assert.assertEquals("测试用户", user.getUserName());

        users = exMapper.selectByUserInfo(user);
        Assert.assertFalse(users.isEmpty());
        user = users.get(0);
        Assert.assertNotNull(user);
        Assert.assertEquals("001", user.getUserId());
        Assert.assertEquals("TEST_001", user.getUserCode());
        Assert.assertEquals("测试用户", user.getUserName());
    }
}
