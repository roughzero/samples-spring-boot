package rough.samples.spring.boot.db.ds01.mapper.ex;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import rough.samples.spring.boot.db.ds01.model.SplUser;
import rough.samples.spring.boot.db.dto.UserDto;

import java.util.List;

@Mapper
public interface SplUserExMapper {
    @Select({"SELECT * FROM SPL_USER WHERE USER_CODE = RPAD(#{userCode,jdbcType=CHAR}, 20, ' ')"})
    // 这里使用 ResultMap id 是由程序生成的默认 Map
    @ResultMap("rough.samples.spring.boot.db.ds01.mapper.SplUserMapper.BaseResultMap")
    SplUser selectByUserCode(String userCode);

    @Select({"SELECT * FROM SPL_USER WHERE USER_NAME = #{userName, jdbcType=VARCHAR}"})
    // 这里使用了 @Results 标签来定义返回，作为示例
    @Results({
            @Result(column = "USER_ID", property = "userId", jdbcType = JdbcType.CHAR, id = true),
            @Result(column = "USER_CODE", property = "userCode", jdbcType = JdbcType.CHAR),
            @Result(column = "USER_NAME", property = "userName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP)
    })
    List<SplUser> selectByUserName(String userName);

    @SelectProvider(type = SplUserSqlProvider.class, method = "selectByUserInfo")
    @ResultMap("rough.samples.spring.boot.db.ds01.mapper.SplUserMapper.BaseResultMap")
    List<SplUser> selectByUserInfo(SplUser user);

    @Select({"SELECT TRIM(USER_CODE) AS USER_CODE, USER_NAME FROM SPL_USER WHERE USER_CODE = RPAD(#{userCode,jdbcType=CHAR}, 20, ' ')"})
    UserDto selectUserDtoByUserCode(String userCode);
}
