package rough.samples.spring.boot.mybatis.dynamic.model;

import java.util.Date;
import javax.annotation.Generated;

public class SplUser {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.877+08:00", comments="Source field: SPL_USER.USER_ID")
    private String userId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.88+08:00", comments="Source field: SPL_USER.USER_CODE")
    private String userCode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.88+08:00", comments="Source field: SPL_USER.USER_NAME")
    private String userName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.88+08:00", comments="Source field: SPL_USER.CREATE_TIME")
    private Date createTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.88+08:00", comments="Source field: SPL_USER.USER_ID")
    public String getUserId() {
        return userId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.88+08:00", comments="Source field: SPL_USER.USER_ID")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.88+08:00", comments="Source field: SPL_USER.USER_CODE")
    public String getUserCode() {
        return userCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.88+08:00", comments="Source field: SPL_USER.USER_CODE")
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.88+08:00", comments="Source field: SPL_USER.USER_NAME")
    public String getUserName() {
        return userName;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.88+08:00", comments="Source field: SPL_USER.USER_NAME")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.881+08:00", comments="Source field: SPL_USER.CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.881+08:00", comments="Source field: SPL_USER.CREATE_TIME")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}