# docker-compose使用示例

```bash

# 后台启动docker-compose 所有服务
docker-compose up -d

# 单独启动 es 服务
docker-compose up -d elasticsearch

# 启动后检查容器状态
docker-compose ps

# -f 表示实时跟踪日志
docker-compose logs -f  
```

- docker-compose中ES的健康检查：healthcheck，中的`-u elastic:$ELASTIC_PASSWORD`需要从环境配置文件`.env`中获取


### Elasticsearch

- 因为kibana不能使用elastic用户，所以可以使用内置的`kibana_system`,但是需要改密码
- **修改ES内置用户：`kibana_system`的密码**
```bash
http://localhost:9200/_security/user/kibana_system/_password
{
  "password": "你要修改的密码"
}
```
- 如果不改密码，kibana启动会卡死在：

```bash
# kibana 日志会卡在这里不动了，查看ES日志发现认证错误，
[plugins.screenshotting.chromium] Browser executable: /usr/share/kibana/node_modules/@kbn/screenshotting-plugin/chromium/headless_shell-linux_x64/headless_shell
```
