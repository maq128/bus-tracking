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
