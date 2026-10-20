# 秒杀系统（Seckill V2）

基于 Spring Boot 3 + Redis + Redisson 的高并发秒杀系统，解决超卖、重复下单、订单超时等问题。

## 技术栈

- Spring Boot 3.0.2
- MyBatis-Plus 3.5.5
- MySQL 8
- Redis + Redisson
- SpringDoc OpenAPI（Swagger）
- Logback

## 核心功能

- 用户注册 / 登录（BCrypt 加密）
- 商品列表 / 详情（分页）
- 秒杀下单（Redis 预减库存 + Redisson 分布式锁）
- 订单查询
- 订单超时自动取消（Redisson 延迟队列）
- 接口限流（Redis SETNX）
- 秒杀时间校验（Redis Hash）

## 如何解决超卖

1. **Redis Lua 脚本原子预减库存**：把"读库存-判断-扣减"打包成原子操作，挡住大部分无效请求。
2. **Redisson 分布式锁**：保证同一商品只有一个线程能扣减数据库库存、创建订单。
3. **双重检查 + 回补机制**：拿到锁后再次检查数据库库存；若库存不足，回补 Redis 库存，保证数据一致。

## 如何解决订单超时

使用 Redisson 的 `RDelayedQueue`，下单成功后投放一个 30 秒后到期的任务；消费者从阻塞队列取出任务，检查订单是否仍未支付，若未支付则取消订单并回补库存。

## 如何运行

1. 启动 MySQL 和 Redis（推荐用 Docker）
2. 执行 `sql/init.sql` 建库建表
3. 修改 `application.properties` 中的数据库密码
4. 运行 `SekillV2Application`
5. 访问 Swagger：http://localhost:8080/swagger-ui.html

## 接口文档

| 接口 | 方法 | 说明 |
|------|------|------|
| `/user/register` | POST | 注册 |
| `/user/login` | POST | 登录 |
| `/product/list` | GET | 商品列表 |
| `/seckill/order` | POST | 秒杀下单 |
| `/order/list` | GET | 查询订单 |

## 项目结构
src/main/java/single/seckillv2/
├── Commen/ # 统一返回、异常、分页
├── Config/ # Redis、Redisson、MyBatis-Plus、日志配置
├── Controller/ # 接口层
├── Entity/ # 实体类 + VO
├── Mapper/ # 数据访问
└── Service/ # 业务逻辑

## 作者

你的名字 · 1036686629@qq.com