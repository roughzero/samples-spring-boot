package rough.samples.spring.boot.mybatis.dynamic.mapper;

import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static rough.samples.spring.boot.mybatis.dynamic.mapper.SplUserDynamicSqlSupport.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;
import rough.samples.spring.boot.mybatis.dynamic.model.SplUser;

@Mapper
public interface SplUserMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.894+08:00", comments="Source Table: SPL_USER")
    BasicColumn[] selectList = BasicColumn.columnList(userId, userCode, userName, createTime);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.885+08:00", comments="Source Table: SPL_USER")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.886+08:00", comments="Source Table: SPL_USER")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.887+08:00", comments="Source Table: SPL_USER")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<SplUser> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.887+08:00", comments="Source Table: SPL_USER")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<SplUser> multipleInsertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.888+08:00", comments="Source Table: SPL_USER")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("SplUserResult")
    Optional<SplUser> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.888+08:00", comments="Source Table: SPL_USER")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="SplUserResult", value = {
        @Result(column="USER_ID", property="userId", jdbcType=JdbcType.CHAR, id=true),
        @Result(column="USER_CODE", property="userCode", jdbcType=JdbcType.CHAR),
        @Result(column="USER_NAME", property="userName", jdbcType=JdbcType.VARCHAR),
        @Result(column="CREATE_TIME", property="createTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<SplUser> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.889+08:00", comments="Source Table: SPL_USER")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.889+08:00", comments="Source Table: SPL_USER")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, splUser, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.89+08:00", comments="Source Table: SPL_USER")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, splUser, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.89+08:00", comments="Source Table: SPL_USER")
    default int deleteByPrimaryKey(String userId_) {
        return delete(c -> 
            c.where(userId, isEqualTo(userId_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.89+08:00", comments="Source Table: SPL_USER")
    default int insert(SplUser record) {
        return MyBatis3Utils.insert(this::insert, record, splUser, c ->
            c.map(userId).toProperty("userId")
            .map(userCode).toProperty("userCode")
            .map(userName).toProperty("userName")
            .map(createTime).toProperty("createTime")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.891+08:00", comments="Source Table: SPL_USER")
    default int insertMultiple(Collection<SplUser> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, splUser, c ->
            c.map(userId).toProperty("userId")
            .map(userCode).toProperty("userCode")
            .map(userName).toProperty("userName")
            .map(createTime).toProperty("createTime")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.892+08:00", comments="Source Table: SPL_USER")
    default int insertSelective(SplUser record) {
        return MyBatis3Utils.insert(this::insert, record, splUser, c ->
            c.map(userId).toPropertyWhenPresent("userId", record::getUserId)
            .map(userCode).toPropertyWhenPresent("userCode", record::getUserCode)
            .map(userName).toPropertyWhenPresent("userName", record::getUserName)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.894+08:00", comments="Source Table: SPL_USER")
    default Optional<SplUser> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, splUser, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.895+08:00", comments="Source Table: SPL_USER")
    default List<SplUser> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, splUser, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.895+08:00", comments="Source Table: SPL_USER")
    default List<SplUser> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, splUser, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.895+08:00", comments="Source Table: SPL_USER")
    default Optional<SplUser> selectByPrimaryKey(String userId_) {
        return selectOne(c ->
            c.where(userId, isEqualTo(userId_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.896+08:00", comments="Source Table: SPL_USER")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, splUser, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.896+08:00", comments="Source Table: SPL_USER")
    static UpdateDSL<UpdateModel> updateAllColumns(SplUser record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(userId).equalTo(record::getUserId)
                .set(userCode).equalTo(record::getUserCode)
                .set(userName).equalTo(record::getUserName)
                .set(createTime).equalTo(record::getCreateTime);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.897+08:00", comments="Source Table: SPL_USER")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(SplUser record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(userId).equalToWhenPresent(record::getUserId)
                .set(userCode).equalToWhenPresent(record::getUserCode)
                .set(userName).equalToWhenPresent(record::getUserName)
                .set(createTime).equalToWhenPresent(record::getCreateTime);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.897+08:00", comments="Source Table: SPL_USER")
    default int updateByPrimaryKey(SplUser record) {
        return update(c ->
            c.set(userCode).equalTo(record::getUserCode)
            .set(userName).equalTo(record::getUserName)
            .set(createTime).equalTo(record::getCreateTime)
            .where(userId, isEqualTo(record::getUserId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-01-21T19:26:29.898+08:00", comments="Source Table: SPL_USER")
    default int updateByPrimaryKeySelective(SplUser record) {
        return update(c ->
            c.set(userCode).equalToWhenPresent(record::getUserCode)
            .set(userName).equalToWhenPresent(record::getUserName)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .where(userId, isEqualTo(record::getUserId))
        );
    }
}