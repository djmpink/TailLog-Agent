# TailLog-Agent
配合TailLog工具使用的代理服务程序（Java版）

### 功能作用：
该程序主要实现两个功能：

* 通过ssh协议访问日志服务器并读取日志

* 提供WebSocket服务，输出日志信息

其基本结构如下：

PC <----> 代理服务器 <----> 日志服务器

TailLog <----> Agent <----> 日志文件

### 部署

* 下载最新发布jar包

* 在运行目录下，添加配置文件：

config.properties

````
##服务端口
server.port=10091

##被代理服务器的ssh配置（日志文件服务器）
ssh.server.log.ip=192.168.1.5
ssh.server.log.port=22
ssh.server.log.username=root
ssh.server.log.password=123456
````

* 运行：

````
java -jar taillog-agent-1.0.0.jar --spring.config.location=file:./config.properties
````

