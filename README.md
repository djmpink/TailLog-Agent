# TailLog-Agent

配合TailLog工具使用的代理服务程序（Java版）

<a href="http://www.taillog.cn">TailLog</a>

### 一.功能作用：

1. 该程序主要实现两个功能：

* 通过ssh协议访问日志服务器并读取日志

* 提供WebSocket服务，输出日志信息

2.其基本结构如下：

PC <----> 代理服务器 <----> 日志服务器

TailLog <----> Agent <----> 日志文件

3.主要技术栈

* <a href="http://projects.spring.io/spring-boot/">Spring Boot</a>

* WebSocket

* <a href="http://www.jcraft.com/jsch/">JSch</a>

### 二.部署

* Step-1: 下载最新发布jar包

<a href="https://github.com/djmpink/TailLog-Agent/releases/download/1.0.0/taillog-agent-1.0.0.jar">taillog-agent-1.0.0.jar</a>

* Step-2: 在运行目录下，添加配置文件

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

* Step-3: 运行

````
java -jar taillog-agent-1.0.0.jar --spring.config.location=file:./config.properties
````


### 三.使用源码打包

配置内容和方式同上

````
$ mvn package
$ mvn install
````

### 四.接口说明

开发人员可以根据以下接口说明使用其他语言自行实现代理程序

* WebSocket访问地址：
````
ws://{ip}:{port}/websocket
````

默认端口：10091


* 连接参数：
````
{
    "ssh": {
        "ip":"192.168.5.55",
        "port":"22",
        "username":"root",
        "password":"123456"
    },
    "content": "tail -f /home/project/app.log" 
}
````
参数说明：

ssh：[选填] 被代理的服务器IP地址。客户端可以通过配置该信息指定访问的服务器。未填则使用代理配置的服务器信息

content：[必填] 待执行的linux命令。客户端将发送"tail..."等命令获取日志内容



