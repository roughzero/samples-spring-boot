package rough.samples.spring.boot.db.plugin;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import rough.samples.spring.boot.db.dto.DynamicSqlDto;

import java.util.*;

@Intercepts(@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
@SuppressWarnings("rawtypes, unchecked")
@CommonsLog
public class FileTypePlugin implements Interceptor {
    private static final Map<String , MappedStatement> mappedStatementCache = new HashMap<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];
        if (parameterObject instanceof DynamicSqlDto) {
            DynamicSqlDto dynamicSqlDto = (DynamicSqlDto) parameterObject;
            Class resultType = dynamicSqlDto.getResultType();
            if (resultType != null) {
                //替换返回类型
                args[0] = replaceMappedStatementWithNewResultType(ms, resultType);
            }
        }
        return invocation.proceed();
    }

    /**
     * @param ms         original MappedStatement
     * @param resultType 返回对象类型
     * @description 根据现有的ms创建一个新的，使用新的返回值类型
     * @author yuqi
     * @date 2021/05/06
     * @link MappedStatement
     */
    private MappedStatement replaceMappedStatementWithNewResultType(MappedStatement ms, Class resultType) {
        String key = ms.getId() + ":" + resultType.getSimpleName();
        return mappedStatementCache.computeIfAbsent(key, k -> rebuildMappedStatement(ms, resultType, key));
    }

    private MappedStatement rebuildMappedStatement(MappedStatement ms, Class resultType, String key) {
        //下面是新建的过程，考虑效率和复用对象的情况下，这里最后生成的ms可以缓存起来，下次根据 ms.getId() + "_" + getShortName(resultType) 直接返回 ms,省去反复创建的过程
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), key, ms.getSqlSource(), ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        //count查询返回值int
        List<ResultMap> resultMaps = new ArrayList<>();
        ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId(), resultType, Collections.EMPTY_LIST).build();
        resultMaps.add(resultMap);
        builder.resultMaps(resultMaps);
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }
}
