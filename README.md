# microservice-config

### Description
微服务配置中心

首先感谢[程序猿DD，stone-jin](https://github.com/dyc87112/spring-cloud-config-admin)的开源项目。目前做配置中心的少有的带UI的项目，本项目是在这两位的项目基础上，针对需要使用的内容做了简化，以及对项目未完善的内容做了优化。

### 项目结构

#### 配置存储
![](https://images.gitee.com/uploads/images/2019/0916/200041_9e968a17_5294959.png "屏幕截图.png")

[详细内容](https://github.com/dyc87112/spring-cloud-config-admin)

#### 配置动态刷新

![![输入图片说明](https://images.gitee.com/uploads/images/2019/0916/201103_14b18729_5294959.png "屏幕截图.png")](https://images.gitee.com/uploads/images/2019/0916/201103_f9c99d03_5294959.png "屏幕截图.png")

參考资料：</br>
[配置中心svn示例和refresh](https://www.cnblogs.com/ityouknow/p/6906917.html)</br>
[配置中心服务和高可用](https://www.cnblogs.com/ityouknow/p/6922705.html)</br>
[配置中心和消息总线](https://www.cnblogs.com/ityouknow/p/6931958.html)


#### 项目主要内容

1. 实现从界面中对微服务配置进行添加和修改</br>
2. 持久化配置到数据库</br>
3. 支持动态刷新配置









