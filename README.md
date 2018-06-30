## github地址
[https://github.com/PactStart/kangaroo](https://github.com/PactStart/kangaroo)

## 项目结构
kangaroo-parent maven父项目，全局依赖管理和插件管理

### 第三方服务
1. aliyun-sms-spring-boot-starter 阿里云短信服务通过spring-boot-starter的方式封装。
2. aliyun-sts-spring-boot-starter 阿里云STS(Security Token Service)通过spring-boot-starter的方式封装，主要用于获取临时访问令牌，例如OSS（Object Storage Service）web端或移动端直传。
3. jpush-spring-boot-starter 极光推送通过spring-boot-starter方式的封装，用于给移动端App推送通知。
4. redis-mq-spring-boot-starter 基于spring-data-redis的简易封装，为你的应用快速提供Redis的MQ功能。
5. aliyun-mq-spring-boot-starter 阿里云消息队列服务通过spring-boot-starter的方式封装。
6. cache-spring-boot-starter 对外暴露统一的缓存接口，通过配置决定缓存的实现方式。

### 基础组件
1. kangaroo-basedao 基于tkmybatis和pagehelper提供数据操作基类和分页支持。
2. kangaroo-code-generator 基于MBG提供数据库到实体和Mapper以及映射文件的生成。
3. kangaroo-commonutils 提供一些最基本的工具类。
4. kangaroo-httputils 提供http相关的工具类。
5. kangaroo-biz-common 业务基础组件，定义了基础的全局异常码和全局异常。
6. kangaroo-simple-web-framework 封装web应用相关常用基础配置和工具类。