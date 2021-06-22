/*
 * Created on 2018年8月13日
 */
package org.mybatis.generator.codegen.mybatis3;

import java.sql.Types;

import org.mybatis.generator.api.IntrospectedColumn;

public class MyMyBatis3FormattingUtilities {
    public static String getParameterClauseInWhereCondition(IntrospectedColumn introspectedColumn) {
        return getParameterClauseInWhereCondition(introspectedColumn, null);
    }

    public static String getParameterClauseInWhereCondition(IntrospectedColumn introspectedColumn, String prefix) {
        StringBuilder sb = new StringBuilder();

        if (introspectedColumn.getJdbcType() == Types.CHAR) {
            sb.append("RPAD(");
        }

        sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, prefix));

        if (introspectedColumn.getJdbcType() == Types.CHAR) {
            sb.append(", ");
            sb.append(introspectedColumn.getLength());
            sb.append(", ' ')");
        }

        return sb.toString();
    }
}
