# 项目说明

世纪互联提供的“行政服务app”里面可以查看班车位置。

[app下载](http://211.151.82.84:35257/APPdownload.php)

本项目基于对这个 app 的逆向工程，实现了简易的班车位置查询功能。

# 百度地图接口

[百度地图生成器](http://api.map.baidu.com/lbsapi/creatmap/)

[百度地图API](http://lbsyun.baidu.com/jsdemo.htm)

# docker 打包运行

```sh
docker volume create --name MavenRepo
docker run -it --rm -v MavenRepo:/root/.m2 -v `pwd`:/bus-tracking -w /bus-tracking maven:3.6-jdk-8-alpine mvn clean package -Dmaven.test.skip=true
docker build -t bus-tracking .

docker run -d --name=bus-tracking --network=main_network bus-tracking
```

# `@ServerEndpoint` 跟 `@Autowired` 不能同时用的问题

[@ServerEndpoint and @Autowired](https://stackoverflow.com/questions/29306854/serverendpoint-and-autowired)

# 定时任务问题

[Scheduled task not working with websockets](https://stackoverflow.com/questions/56169448/scheduled-task-not-working-with-websockets)
