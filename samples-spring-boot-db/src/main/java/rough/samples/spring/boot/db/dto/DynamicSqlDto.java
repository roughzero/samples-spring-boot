package rough.samples.spring.boot.db.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class DynamicSqlDto {
    /** 查询语句 */
    private String sql;
    /** 返回对象，默认为 Map */
    private Class resultType = Map.class;
}
