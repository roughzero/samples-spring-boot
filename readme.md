# samples-spring-boot

Spring boot 工程相关示例，内容如下：

- 发布到 weblogic
- 数据源配置
  - 多数据源
    - JdbcTemplate 配置
    - myBatis 配置
  - 使用 jndi 数据源
  - 多数据源时数据库连接与 jndi 配置兼容
- 打包发布
  - 打包成原始，而不是可执行
  - 同一个工程做成多个包，可以根据需要发布选定的内容
  - 指定包名
  - 根据配置文件打包（结合 Maven）
- 定时任务
- 属性配置类
- 测试类
- 事务处理