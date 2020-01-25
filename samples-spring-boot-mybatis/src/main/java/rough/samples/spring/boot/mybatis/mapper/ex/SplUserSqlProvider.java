package rough.samples.spring.boot.mybatis.mapper.ex;

import org.springframework.stereotype.Repository;

import rough.samples.spring.boot.mybatis.model.SplUser;

@Repository
public class SplUserSqlProvider {
    public String selectByUserInfo(SplUser user) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM SPL_USER");
        if (user == null) {
            sql.append(" WHERE 1 <> 1");
            return sql.toString();
        }
        sql.append(" WHERE 1 = 1"); // 使用这个避免 WHERE 和 AND 的判断
        if (user.getUserCode() != null) {
            sql.append(" AND USER_CODE = RPAD(#{userCode, jdbcType=CHAR}, 20, ' ')");
        }
        if (user.getUserName() != null) {
            sql.append(" AND USER_NAME = #{userName, jdbcType=VARCHAR}");
        }
        return sql.toString();
    }
}
