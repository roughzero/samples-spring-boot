package rough.samples.spring.boot.mybatis.simple.mapper.ex;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import rough.samples.spring.boot.mybatis.simple.model.SplUser;

import java.util.List;

@Mapper
public interface SplUserExMapper {
    @Select({"SELECT * FROM SPL_USER WHERE USER_CODE = RPAD(#{userCode,jdbcType=CHAR}, 20, ' ')"})
    @Results(id = "splUser", value = {
            @Result(column = "USER_ID", property = "userId", jdbcType = JdbcType.CHAR, id = true),
            @Result(column = "USER_CODE", property = "userCode", jdbcType = JdbcType.CHAR),
            @Result(column = "USER_NAME", property = "userName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP)
    })
    SplUser selectByUserCode(String userCode);

    @Select({"SELECT * FROM SPL_USER WHERE USER_NAME = #{userName, jdbcType=VARCHAR}"})
    @ResultMap("splUser")
    List<SplUser> selectByUserName(String userName);
}
