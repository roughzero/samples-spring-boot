package rough.samples.spring.boot.mybatis.dynamic.mapper;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class SplUserDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.883+08:00", comments="Source Table: SPL_USER")
    public static final SplUser splUser = new SplUser();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.884+08:00", comments="Source field: SPL_USER.USER_ID")
    public static final SqlColumn<String> userId = splUser.userId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.884+08:00", comments="Source field: SPL_USER.USER_CODE")
    public static final SqlColumn<String> userCode = splUser.userCode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.884+08:00", comments="Source field: SPL_USER.USER_NAME")
    public static final SqlColumn<String> userName = splUser.userName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.884+08:00", comments="Source field: SPL_USER.CREATE_TIME")
    public static final SqlColumn<Date> createTime = splUser.createTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.883+08:00", comments="Source Table: SPL_USER")
    public static final class SplUser extends SqlTable {
        public final SqlColumn<String> userId = column("USER_ID", JDBCType.CHAR);

        public final SqlColumn<String> userCode = column("USER_CODE", JDBCType.CHAR);

        public final SqlColumn<String> userName = column("USER_NAME", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("CREATE_TIME", JDBCType.TIMESTAMP);

        public SplUser() {
            super("SPL_USER");
        }
    }
}