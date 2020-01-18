# samples-spring-boot

Spring boot 工程相关示例，内容如下：

- [x] 发布到 weblogic
- [ ] 数据源配置
  - [x] 使用 jndi 数据源
  - [x] 多数据源的 NamedJdbcTemplate 配置
  - [x] 多数据源时数据库连接与 jndi 配置兼容
  - [ ] myBatis 多数据源配置
- [x] 打包发布
  - [x] 打包成原始，而不是可执行
  - [x] 同一个工程做成多个包，可以根据需要发布选定的内容
  - [x] 指定包名
  - [x] 根据配置文件打包（结合 Maven）
- [ ] 其他
  - [ ] 定时任务
  - [x] 属性配置类
  - [x] 测试类
  - [x] 事务处理
  - [ ] 事务处理进阶
    - [ ] 是否可以全局配置
    - [ ] 超时等精细话的配置如何进行
  - [ ] 切片以及使用例子
  - [ ] Log的配置方法

## 发布到 weblogic

## 数据源配置

默认的数据源配置这里就不举例了，网上随便找，也比较简单，主要讲一些不常见的示例。

### 使用 Jndi 数据源

这里的使用仅限于使用 weblogic 的，其他的暂时用不到。

这个先说一下默认的好了，非常简单

```pro
# 这个是使用配置文件的例子，yml 就不举例了
spring.datasource.jndi-name=JndiName
```

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
    @Primary // 至少需要定义一个 Primay
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
	...
}
```

### 多数据源的 myBatis 配置

## 打包发布

如果是比较小的 Spring Boot 工程，打包发布其实没什么好研究的，这里打包发布的主要目的是要让 Spring Boot 工程也模块话，可以分成多个 jar 包，提高模块的独立性已方便多人合作，也便于管理，避免一个工程过大的情况。

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
        <mainClass>rough.samples.springboot.Application</mainClass>
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
    <!-- 在打包时使用 -PprofileId 的形式选择环境 -->
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



## 其他

### 测试类

使用 Junit 测试类，注意测试类不能管理事务，示例如下：

必须要使用的是前面三个注解。

```java
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
@CommonsLog
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
        Assert.assertEquals(result, Integer.valueOf(0));
    }

    @Test
    public void testDataSource02() {
        Integer result = namedParameterJdbcTemplate02.queryForObject(
                TEST_SQL, new HashMap<>(), Integer.class);
        log.info("Result of data source02 is: " + result);
        Assert.assertEquals(result, Integer.valueOf(1));
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