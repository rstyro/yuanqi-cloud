# 微服务脚手架
- 自用脚手架

#### 1、环境
- Springcloud  2024.0.0
- Springcloud-alibaba 2023.0.3.2
- Springboot 3.4.4
- JDK17
- Nacos 2.5.1
- ELK
  - Elasticsearch 8.17.6
  - Logstash 8.17.6
  - Kibana  8.17.6
- Seata 

#### 2、模块解释
- data 资源数据，数据脚本、nacos配置以及相关组件说明
- springcloud-gencode 代码生成器
- springcloud-gateway  网关，动态路由、sentinel简单流控、api分组流控
- springcloud-common  通用模块
  - springcloud-common-bom common包pom引入
  - springcloud-common-core  核心，通用工具实体类
  - springcloud-common-redis  集成redis缓存
  - springcloud-common-security 权限管理类，拦截器异常处理等
  - springcloud-common-swagger 集成swagger3
  - springcloud-common-mybatis-plus 集成mybatis-plus
  - springcloud-common-seata 集成分布式事务seata
  - springcloud-common-sentinel 集成sentinel流控与熔断等
  - springcloud-common-logstash 集成ELK,elasticsearch和kibana自己安装
  - springcloud-common-satoken sa-token权限校验
  - springcloud-common-api-encrypt api接口RSA+AES加解密
- springcloud-scheduled  分布式定时任务xxl-job
  - springcloud-scheduled-admin 定时器后台管理
  - springcloud-scheduled-demo  定时器执行器demo
- springcloud-module  业务服务模块，实战demo,集成各种组件
  - springcloud-user 用户模块
  - springcloud-order 订单模块
  - springcloud-mall 商城模块
- springcloud-api   各个module的api调用
  - springcloud-api-bom  api的pom包引入
  - springcloud-api-mall  商城模块api
  - springcloud-api-order  订单模块api
  - springcloud-api-user  用户模块api
