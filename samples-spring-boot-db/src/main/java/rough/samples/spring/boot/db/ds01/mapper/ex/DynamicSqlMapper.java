package rough.samples.spring.boot.db.ds01.mapper.ex;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import rough.samples.spring.boot.db.dto.DynamicSqlDto;

import java.util.List;
import java.util.Map;

public interface DynamicSqlMapper {
    /**
     * 动态 SQL 查询
     *
     * @param sql 查询 sql
     * @return 查询结果列表，存储在 Map 对象里
     */
    @Select("${sql}")
    List<Map<String, Object>> queryForMapList(@Param("sql") String sql);

    /**
     * 动态 SQL 查询
     *
     * @param dynamicSqlDto 查询参数，sql 为查询语句，resultType 为返回类型
     * @return 查询结果列表
     */
    @Select("${sql}")
    List<Object> queryForList(DynamicSqlDto dynamicSqlDto);
}
