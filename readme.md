# samples-spring-boot

[TOC]

Spring boot 工程相关示例，内容如下：

- [x] 发布到 weblogic
- [x] 数据源配置
  - [x] 使用 jndi 数据源
  - [x] 多数据源的 NamedJdbcTemplate 配置
  - [x] 多数据源时数据库连接与 jndi 配置兼容
  - [x] myBatis 多数据源配置
  - [ ] 使用 MyBatis batch 处理 insert 与 update
- [x] MyBatis 生成与添加方法
  - [x] 使用 MyBatis Generator 生成
    - [x] MyBatis3（传统 XML 形态）
    - [x] MyBatis3Simple（注解形态）
    - [x] MyBatis3DynamicSql（暂不研究）
    - [x] MyBatis3Kotlin（略）
  - [x] 添加新方法（含动态SQL示例）
  - [x] 其他动态SQL实现方法
- [x] 打包发布
  - [x] 打包成原始，而不是可执行
  - [x] 同一个工程做成多个包，可以根据需要发布选定的内容
  - [x] 指定包名
  - [x] 根据配置文件打包（结合 Maven）
- [ ] 其他
  - [x] 定时任务
  - [x] 属性配置类
  - [x] 测试类
  - [x] 事务处理
  - [ ] 事务处理进阶
    - [ ] 是否可以全局配置
    - [ ] 超时等精细化的配置如何进行
  - [x] 切片以及使用例子
  - [x] Log的配置方法

---

## 发布到 weblogic

---

## 数据源配置

默认的数据源配置这里就不举例了，网上随便找，也比较简单，主要讲一些不常见的示例。

### 使用 Jndi 数据源

这里的使用仅限于使用 weblogic 的，其他的暂时用不到。

这个先说一下默认的好了，非常简单

```pro
# 这个是使用配置文件的例子，yml 就不举例了
spring.datasource.jndi-name=JndiName
```

### 多数据源的 myBatis 配置

然后是多数据源，同样是需要读到数据源名称，这里我使用注入数据源名称到配置类中再生产的办法。

1. 在 yml 文件中定义 Jndi 名称

```yaml
samples:
  spring:
    boot:
      jndi-datasource:
        datasource01: DataSource01
        datasource02: DataSource02
```

2. 定义属性配置类注入配置信息

```java
@Component
@ConfigurationProperties(prefix = "samples.spring.boot.jndi-datasource")
@Getter
@Setter
public class JndiConfigure {
    private String datasource01;
    private String datasource02;
}
```

3. 使用配置类生产数据源，这个类会先判断是否有定义 Jndi 名称，如果有则使用，否则使用数据库连接直接生产 DataSource。这里顺便把 NamedJdbcTemplate 的定义方式也放在一起了。

```java
@Configuration
@CommonsLog
public class DataSourceConfigure {
    @Resource
    private JndiConfigure jndiConfigure;

    @Bean(name = "dataSource01")
    @Qualifier("dataSource01")
    @ConfigurationProperties(prefix = "samples.spring.boot.datasource01")
    public DataSource dataSource01() {
        if (StringUtils.isNotEmpty(jndiConfigure.getDatasource01())) {
            log.info("Jndi name of data source 01: " + jndiConfigure.getDatasource01());
            JndiDataSourceLookup lookup = new JndiDataSourceLookup();
            lookup.setResourceRef(true);
            return lookup.getDataSource(jndiConfigure.getDatasource01());
        } else {
            log.info("Use direct connect for data source 01.");
            return DataSourceBuilder.create().build();
        }
    }

    @Bean(name = "namedParameterJdbcTemplate01")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate01(@Qualifier("dataSource01") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
    
    @Bean(name = "dataSource02")
    @Qualifier("dataSource02")
    @ConfigurationProperties(prefix = "samples.spring.boot.datasource02")
    @Primary // 至少需要定义一个 Primary
    public DataSource dataSource02() {
        if (StringUtils.isNotEmpty(jndiConfigure.getDatasource02())) {
            log.info("Jndi name of data source 02: " + jndiConfigure.getDatasource02());
            JndiDataSourceLookup lookup = new JndiDataSourceLookup();
            lookup.setResourceRef(true);
            return lookup.getDataSource(jndiConfigure.getDatasource02());
        } else {
            log.info("Use direct connect for data source 01.");
            return DataSourceBuilder.create().build();
        }
    }
	//...
}
```

### 使用 MyBatis batch 处理 insert 与 update

代码示例

```java
public class BatchProcess {
    private static final int TEST_SIZE = 1000;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    public void doBatch() {
        // 使用 openSession 取得批量执行的 Session，再取得 Mapper
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        SplUserMapper mapper = sqlSession.getMapper(SplUserMapper.class);
        for (int i = 0; i < TEST_SIZE; i++) {
            String userId = format.format(i);
            SplUser user = new SplUser();
            user.setUserId(userId);
            user.setUserCode(userId);
            mapper.insert(user);
        }
        // 注意最后一定要执行 flushStatements() 方法，否则批量操作不会提交数据库
        // 如果一次操作的量很多，可以定期执行 flushStatements() 方法，以免内存积压过多
        sqlSession.flushStatements();
    }
}
```

定义 SqlSession Bean

```java
public class DataSourceConfigure {
    // 可以定义一个批量 SqlSession，这样就可以直接注入，不需要再 open。
    @Bean("batchSqlSession01")
    public SqlSession batchSqlSession(@Qualifier("sqlSessionFactory01") SqlSessionFactory sqlSessionFactory) {
        // 注意，这里不能使用 new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
        // 实验了以下，仍然是默认的提交方式
        return sqlSessionFactory.openSession(ExecutorType.BATCH);
    }
}
```



---

## MyBatis 生成与添加方法

### 使用 MyBatis Generator 生成

目前最新版本为 1.40，已取消对 ibatis 的支持，并且默认使用 MyBatis3DynamicSql。

我们需要解决的问题是

1. 如何生成
2. 能够兼容 CHAR 类型的字段，要求做到
   1. 读取后去掉尾部的空格
   2. 查询时使用去掉空格的字符串能够正常查询

为此，先建立一个```samples-spring-boot-mybatis-generator-patch```工程，在其中放置需要覆盖 mybatis-generator 中的代码。

然后，在建立 ```samples-spring-boot-mybatis-generator``` 工程，使用它生成逆向工程，详细如何使用见 pom.xml 文件以及示例。

使用以下命令生成

```shell
mvn mybatis-generator:generate
```



使用的测试表结构如下，为包含自动生成序列主键

```sql
-- 准备示例数据表，包含 VARCHAR 和 CHAR 类型
CREATE TABLE SPL_USER
(
    USER_ID        CHAR (20) NOT NULL PRIMARY KEY,
    USER_CODE      CHAR (20) NOT NULL,
    USER_NAME      VARCHAR2 (100),
    CREATE_TIME    DATE
);
```

目前的结论时，可以实现生成的 SQL 正常查询 CHAR，使用 Example 则不行，因此，会在<font color="red">**规范中禁用 Example 的使用**</font>。

关于 MyBatis 生成部分的资料参见 [MyBatis Generator 官方网站](http://mybatis.org/generator/index.html)

#### MyBatis3（传统 XML 形态）

1. 创建配置文件 generateConfigMyBatis3.xml，这里在表中设置属性可以去掉尾部空格

```xml
<!DOCTYPE generatorConfiguration PUBLIC
        "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>

    <classPathEntry location="lib/jdbc-driver-11g1.6.jar"/>

    <context id="dynamicSql" targetRuntime="MyBatis3DynamicSql">
        <jdbcConnection driverClass="oracle.jdbc.driver.OracleDriver"
                        connectionURL="jdbc:oracle:thin:@192.168.5.128:1521:demo"
                        userId="rough" password="rough"/>
        <javaModelGenerator targetPackage="rough.samples.spring.boot.mybatis.dynamic.model"
                            targetProject="output/src/main/java"/>

        <javaClientGenerator targetPackage="rough.samples.spring.boot.mybatis.dynamic.mapper"
                             targetProject="output/src/main/java"/>

        <table tableName="SPL_USER" domainObjectName="SplUser">
            <property name="useActualColumnName" value="false"/>
            <!-- 使属性 trimString = true 可以去掉查询得到的值的尾部空格-->
            <property name="trimString" value="true"/>
        </table>
    </context>
</generatorConfiguration>
```

2. 为满足查询时使用去掉空格的字符串能够正常查询，需要改造生成程序，并将其的执行路径放在 jar 包之前，具体做法如下：

   1. 先创建一个 MyMyBatis3FormattingUtilities 类，该类在 CHAR 类型字段作为查询条件时，自动对传入参数作 RPAD处理，主要代码如下：

   ```java
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
   ```

   2. 修改创建 XML 中 SQL 代码的类，调整生成查询条件的代码，使其生成额外做过处理的代码，这几个类为 org.mybatis.generator.codegen.mybatis3 包下的

   ```java
   class DeleteByPrimaryKeyElementGenerator{}
   class SelectByPrimaryKeyElementGenerator{}
   class SimpleSelectByPrimaryKeyElementGenerator{}
   class UpdateByPrimaryKeySelectiveElementGenerator{}
   class UpdateByPrimaryKeyWithBLOBsElementGenerator{}
   class UpdateByPrimaryKeyWithoutBLOBsElementGenerator{}
   ```
   
   可参见例子，这些代码都是根据原始代码进行修改的，使用`MyMyBatis3FormattingUtilities`的地方就是修改的地方。
   
   3. 使用如下命令行生成
   
   ```shell
   generate.sh generateConfigMyBatis3.xml
   ```
   
   命令具体内容可直接看 generate.sh
   
   4. 测试：详见`samples-spring-boot-mybatis`工程

#### MyBatis3Simple（注解形态）

注解形式生成的内容较少，不需要写 xml 文件，推荐数据结构简单的时候使用。

生成的方式与传统 XML 形态类似，过程就不详细说明了，配置文件为 generateConfigMyBatis3Simple.xl，测试工程为``samples-spring-boot-mybatis-simple`

#### MyBatis3DynamicSql（暂不研究）

尽管是目前的默认推荐形式，但是由于没有找到具体的例子，官网介绍较少，没有相关文档，在查询时就无法使用 CHAR 类型字段进行查询，故先暂时放一下，以后有机会再说。

#### MyBatis3Kotlin（略）

这个生成的是 Kotlin，就忽略了

### 添加新方法（含动态SQL示例）

注意事项：

1. 为了不影响数据库修改的重新打包，所有新方法<font color="red">**不能**</font>添加到生成的 Mapper 中。
2. 建议将生成的文件单独打成 jar 包，至少做到与代码 package 分离

还有，这里的动态 SQL 指能够在运行时决定 SQL 内容，开发时不能确定 SQL 内容的情况。简单的例子就是查询 SQL 是存储在数据库中的，运行时查询出 SQL 后再查询使用的例子。

添加过程，建议使用注解方式添加，同时可以实现动态 SQL。

1. 非动态SQL，示例如下：

```java
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
}
```

2. 动态 SQL，示例如下：

   1. Mapper

   ```java
    public interface SplUserExMapper {
       @SelectProvider(type = SplUserSqlProvider.class, method = "selectByUserInfo")
       // 注意不要遗漏 Result 定义
       @ResultMap("rough.samples.spring.boot.mybatis.mapper.SplUserMapper.BaseResultMap")
       List<SplUser> selectByUserInfo(SplUser user);
    }
   ```

   2. Provider

   ```java
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
   ```

   注意这个 Provider 是不归 spring 管理的，所以不要想在 Provider 里面再去查询数据库什么的。但是可以把执行 SQL 传给  Provider，实现真动态 SQL，而不是在 XML 里面定义的。
   
   这个示例与 Mybatis 提供的动态 SQL 标签中的 where 标签的作用是一样的，这里的例子仅是说明如何在 java 类中实现动态 SQL 的效果。
### 其他动态SQL实现方法

除了在 Provider 中实现以外，还可以通过 map 参数来实现动态 SQL。

下面的例子是根据传入的表名决定从哪个表中查询数据的例子。

```sql
SELECT COUNT(1) FROM ${tableName}
```

这里注意 ```${}``` 标签与 ```#{}``` 标签的使用区别，一个在 SQL 完成之前，作为 SQL 的组成部分，一个是 SQL 完成后的参数部分。

极端的情况下，可以将运行查询语句全部传入。

```sql
${sql}
```

下面的例子实现传入 SQL，并返回 Map 对象

```java
public interface DynamicSqlMapper {
    @Select("${sql}")
    List<Map<String, Object>> queryForMapList(@Param("sql") String sql);
}
```

测试语句如下：

```java
public class Test {

    @Test
    public void test() {
        String sql = "SELECT 1 AS CNT, SYSDATE AS CURRENT_TIME FROM DUAL";
        List<Map<String, Object>> mapResults = dynamicSqlMapper.queryForMapList(sql);
        Assert.assertNotNull(mapResults);
        Assert.assertEquals(1, mapResults.size());
        Map<String, Object> mapResult = mapResults.get(0);
        log.info(mapResult);
        log.info(mapResult.getClass());
        Assert.assertFalse(mapResult instanceof QueryResultDto);
        Assert.assertEquals(new BigDecimal(1), mapResult.get("CNT"));
    }
}
```

上面的简单例子，传入的 sql 不能有参数，且不能指定返回对象类型，下面会增加传入参数以及通过拦截器改变返回对象类型的例子。

```java
// 参数对象类
@Getter
@Setter
@SuppressWarnings("rawtypes")
public class DynamicSqlDto {
    /** 查询语句 */
    private String sql;
    /** 返回对象，默认为 Map */
    private final Class resultClass = Map.class;
}

// Mapper 类
public interface DynamicSqlMapper {
    // 这里的返回数据对象 object，可以通过拦截器修改为参数中指定的对象
    @Select("${sql}")
    List<Object> queryForList(DynamicSqlDto dynamicSqlDto);
}

// 拦截器类（该类由余其提供），较长，仅展示主体
@Intercepts(@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
@SuppressWarnings("rawtypes")
public class FileTypePlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) {
        final Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];
        if (parameterObject instanceof DynamicSqlDto dynamicSqlDto) {
            Class resultType = dynamicSqlDto.getResultType();
            if (resultType != null) {
                //替换返回类型
                args[0] = replaceMappedStatementWithNewResultType(ms, resultType);
            }
        }
        return invocation.proceed();
    }
}

// 使配置生效，修改 DataSourceConfigure 类
public class DataSourceConfigure {
    //...
    public SqlSessionFactory sqlSessionFactory() {
        // 在 return sqlSessionFactoryBean.getObject(); 前增加
        // 如果有多个 sqlSessionFactoryBean，则均需要增加
        sqlSessionFactoryBean.setPlugins(new FileTypePlugin());
        return sqlSessionFactoryBean.getObject();
    }
    //...
}
```

以上两种方法，可以结合使用，如在数据库中查询出 SQL，又可能需要根据某些条件替换 SQL 中字符，则将替换工作放在 Provider 中是一个方便地解决方案。

---

## 打包发布

如果是比较小的 Spring Boot 工程，打包发布其实没什么好研究的，这里打包发布的主要目的是要让 Spring Boot 工程也模块化，可以分成多个 jar 包，提高模块的独立性已方便多人合作，也便于管理，避免一个工程过大的情况。

### 指定包名

在 pom.xml 中的`build`节点中加入`finalName`节点即可，示例如下：

```xml
<build>
    <!-- 最终包名为默认包名加上环境名-->
    <finalName>${project.name}-${project.version}-${activeProfile}</finalName>
</build>
```

### 打包成原始包，而不是可执行包

为了实现模块化，需要打出的 jar 包是不可执行，这里需要对`spring-boot-maven-plugin`进行配置，示例如下：

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <classifier>exec</classifier> <!-- 打包时生成原始包与可执行包，可执行包结尾的加上 exec 标识 -->
        <attach>false</attach> <!-- 发布到仓库时只发布原始包，不发布可执行包 -->
        <!-- 由于在打 war 包时是在 classes 目录里面找 main class 的，所以需要指定 main class -->
        <mainClass>rough.samples.spring.boot.Application</mainClass>
    </configuration>
</plugin>
```

### 根据配置文件打包

这里只说明 Spring Boot 使用 yml 文件的情况。

1. 配置文件暗中 application-profileName.yml 的格式保存，如现在有 test, dev 两个环境，则配置文件如下：

```
application.yml
application-dev.yml
application-test.yml
```

2. 在  application.yml 中指定激活配置的变量`activeProfile`，注意这里用的格式与 properties 文件不同

```yaml
spring:
    profiles:
      active: @activeProfile@
```

3. 在 pom.xml 中定义 profile 设置该变量

```xml
    <!-- 使用编译参数打包成不同环境的发布包 -->
    <!-- 在打包时使用 -P${profileId} 的形式选择环境 -->
    <!-- test 默认环境，供开发人员在 IDE 下测试，除了 test 环境以外，均需要使用 jndi -->
    <!-- dev 供开发人员在本机的 weblogic 测试 -->
    <!-- dat 测试人员测试环境 -->
    <!-- uat 用户测试环境 -->
    <!-- stg 预演环境 -->
    <!-- prd 生产环境 -->
    <profiles>
        <profile>
            <id>test</id>
            <properties>
                <activeProfile>test</activeProfile>
            </properties>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
        </profile>
        <profile>
            <id>dev</id>
            <properties>
                <activeProfile>dev</activeProfile>
            </properties>
        </profile>
    </profiles>
```

---

## 其他

### 测试类

为测试搭建单独模块 samples-spring-boot-test，并将其依赖放到 parent 中保证都可以用到。该模块包括启动类 `TestApplication` 以及测试配置文件 `application-test.yml`。

TestApplication.java

```java
@SpringBootApplication
@ComponentScan("rough")
@Slf4j
public class TestApplication {
    public static void main(String[] args) {
        log.info("Start test.");
        SpringApplication.run(TestApplication.class, args);
    }
}
```

在 parent 中的 pom.xml 的配置

```xml
        <dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>samples-spring-boot-test</artifactId>
            <version>${project.version}</version>
            <scope>test</scope>
        </dependency>
```



使用 Junit 测试类，注意测试类不能管理事务，示例如下：

必须要使用的是前面三个注解。

```java
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
@Slf4j
public class DataSourceTest {
    private static final String TEST_SQL = "SELECT COUNT(1) FROM DEMO_LOCK";

    @Resource(name = "namedParameterJdbcTemplate01")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate01;
    @Resource(name = "namedParameterJdbcTemplate02")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate02;

    @Test
    public void testDataSource01() {
        Integer result = namedParameterJdbcTemplate01.queryForObject(
                TEST_SQL, new HashMap<>(), Integer.class);
        log.info("Result of data source01 is: " + result);
        Assert.assertEquals(result, 0);
    }

    @Test
    public void testDataSource02() {
        Integer result = namedParameterJdbcTemplate02.queryForObject(
                TEST_SQL, new HashMap<>(), Integer.class);
        log.info("Result of data source02 is: " + result);
        Assert.assertEquals(result, 1);
    }
}
```

### 属性配置类

参见`JndiConfigure`，`DataSourceConfigure`

### 事务处理

使用声明式注解，需要做两件事：

1. 在 Application 类中加入 `@EnableTransactionManagement`注解（需要引入 spring-tx）
2. 在需要事务的类或方法加入`@Transactional`注解

### 定时任务

使用最简单的定时任务作为批处理入口，同样需要做声明与定制两件事：

1. 在 Application 类中加入 `@EnableScheduling`注解
2. 在需要定时执行的方法前加入注解：`@EnableScheduling`

### 切片以及使用例子

先引入`spring-aspects`

```xml
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aspects</artifactId>
        </dependency>
```

代码示例参见`AspectSample`

### Log配置

使用默认的 logback，只要在**application.properties**或者**application.yml**中配置日志级别就可以了。

console默认输入ERROR, WARN ，INFO级别的日志。可通过修改logging.level属性来改变日志的输出级别。可以通过配置logging.file属性或logging.path属性将日志输出到文件中。当文件到达10M的时候，将新建一个文件记录日志。

1. 配置 level （yaml）

```yaml
# 建议基础级别定义为 WARN，然后根据自己的需要定义对应包的级别
logging:
  level:
    root: WARN
    org:
      springframework:
        security: DEBUG
        web: ERROR    
      hibernate: DEBUG        
      apache:
        commons:
          dbcp2: DEBUG 
```

2. logging path & logging file

```yaml
logging:
  path: concretepage/logs
  file: mylogfile.log
```

3. 日志样式

```yaml
logging:
  pattern:
    console: '%d{yyyy-MMM-dd HH:mm:ss.SSS} %-5level [%thread] %logger{15} - %msg%n'
    file: '%d{yyyy-MMM-dd HH:mm:ss.SSS} %-5level [%thread] %logger{15} - %msg%n'
```

4. 在程序中使用

使用 slf4j 和 commons-logging 都可以，我个人比较习惯使用 commons-logging。

```java
@CommonsLog // 使用 lombok 定义 commons-logging

@Slf4j // 使用 lombok 定义 slf4j

public class UsageClass {
    public static void main(String[] args) {
        log.info("log message"); // 无论是使用 slf4j 还是 commons-logging 记录工具的名称都是 log
    }
}
```







参考资料：

[MyBatis Generator](http://mybatis.org/generator/index.html)

[简书上的Spring Boot Logging 配置](https://www.jianshu.com/p/1fa12b92d5c4)

[Mybatis动态SQL的实现](https://blog.csdn.net/lks1139230294/article/details/87957689)

