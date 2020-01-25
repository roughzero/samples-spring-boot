package rough.samples.spring.boot.mybatis.mapper.ex;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;

import rough.samples.spring.boot.mybatis.model.SplUser;

@Mapper
public interface SplUserExMapper {
    @Select({"SELECT * FROM SPL_USER WHERE USER_CODE = RPAD(#{userCode,jdbcType=CHAR}, 20, ' ')"})
    // 这里使用 ResultMap id 是由程序生成的默认 Map
    @ResultMap("rough.samples.spring.boot.mybatis.mapper.SplUserMapper.BaseResultMap")
    SplUser selectByUserCode(String userCode);

    @Select({"SELECT * FROM SPL_USER WHERE USER_NAME = #{userName, jdbcType=VARCHAR}"})
    // 这里使用了 @Results 标签来定义返回，作为示例
    @Results({
            @Result(column="USER_ID", property="userId", jdbcType=JdbcType.CHAR, id=true),
            @Result(column="USER_CODE", property="userCode", jdbcType=JdbcType.CHAR),
            @Result(column="USER_NAME", property="userName", jdbcType=JdbcType.VARCHAR),
            @Result(column="CREATE_TIME", property="createTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<SplUser> selectByUserName(String userName);

    @SelectProvider(type = SplUserSqlProvider.class, method = "selectByUserInfo")
    @ResultMap("rough.samples.spring.boot.mybatis.mapper.SplUserMapper.BaseResultMap")
    List<SplUser> selectByUserInfo(SplUser user);
}
