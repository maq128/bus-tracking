# 项目说明

世纪互联提供的“行政服务app”里面可以查看班车位置。

[app下载](http://211.151.82.84:35257/APPdownload.php)

本项目基于对这个 app 的逆向工程，实现了简易的班车位置查询功能。


# docker 打包运行

```sh
docker volume create --name MavenRepo
docker run -it --rm -v MavenRepo:/root/.m2 -v `pwd`:/bus-tracking -w /bus-tracking maven:3.6-jdk-8-alpine mvn clean package -Dmaven.test.skip=true
docker build -t bus-tracking .

docker run -d --name=bus-tracking --network=main_network bus-tracking
```


# 百度地图接口

[百度地图开放平台](http://lbsyun.baidu.com/)
| [JS API 2.0](http://lbsyun.baidu.com/index.php?title=jspopular)
| [类参考](https://lbsyun.baidu.com/cms/jsapi/reference/jsapi_reference.html)
| [示例DEMO](http://lbsyun.baidu.com/jsdemo.htm)
| [地图生成器](http://api.map.baidu.com/lbsapi/createmap/index.html)

[一条指令即可在网页中嵌入百度地图](https://my.oschina.net/ZhenyuanLiu/blog/1791570)
| [静态图 API V2.0](https://lbsyun.baidu.com/index.php?title=static)

# `@ServerEndpoint` 的自动发现

`@ServerEndpoint` 来自于 `javax.websocket`，它本身并不是 Spring 里面的东西。

一个 `ServerEndpointExporter` 实例可用于自动发现并注册 `ServerEndpoint` 类，它缺省的发现逻辑是扫描 Spring 的 Beans
容器里面的实例，过滤出其中加了 `@ServerEndpoint` 注解的类作为“模板”，给每个新建立的连接创建一个实例。而 Beans 容器里面
的那个实例就废弃了。也是这个原因，导致 `ServerEndpoint` 类里面的 `@Autowired` 注解无法达到预期的效果。

也可以调用 `ServerEndpointExporter.setAnnotatedEndpointClasses()` 直接指定 `ServerEndpoint` 类，从而绕过自动发现。


# `@ServerEndpoint` 跟 `@Autowired` 不能同时用的问题

[@ServerEndpoint and @Autowired](https://stackoverflow.com/questions/29306854/serverendpoint-and-autowired)

`ServerEndpoint` 类缺省情况下是为每个连接创建一个实例，所以在这个类里面使用 `@Autowired` 无法实现自动绑定的效果。

这就导致一个问题，在 `@ServerEndpoint` 注解的类里面无法访问到基于 Spring 组装的 Beans，比如各种 `@Component`、`@Service` 等。

一个解决方案是通过指定 `configurator` 来改变 `ServerEndpoint` 的实例化方式：
```java
@ServerEndpoint(value = "/ws", configurator = SpringConfigurator.class)
```
但是这样也有问题，会报错找不到 `WebApplicationContext`，需要通过使用 `ContextLoaderListener` 来解决。

还有一个简单的办法，就是在 `ServerEndpoint` 里面定义一个静态属性，然后通过在 method 上使用 `@Autowired` 来初始化：
```java
@Component
@ServerEndpoint("/ws")
public class WsServerEndpoint {
	static SomeService someService;

	@Autowired
	public void setSomeService(SomeService someService) {
		WsServerEndpoint.someService = someService;
	}

	...
}
```
不过这种用法需要注意静态属性的并发访问问题。


# 定时任务问题

[Scheduled task not working with websockets](https://stackoverflow.com/questions/56169448/scheduled-task-not-working-with-websockets)

本来在 Spring 里面只要使用 `@EnableScheduling` 和 `@Scheduled` 就可以启动后台任务，非常简单。
但是在跟 websockets 一起使用时会出现冲突，解决的办法是自己创建一个 `TaskScheduler` 实例：
```java
	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		return scheduler;
	}
```
