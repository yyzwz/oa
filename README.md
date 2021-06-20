# 基于Vue和SpringBoot的OA系统

### 介绍
本项目基于Vue和SpringBoot，前端采用View UI组件库，后端集成MyBatisPlus连接MySQL数据库，采用Spring Security做权限控制。  
整个系统分为系统基础、人事模块、后勤模块、基础档案 四大模块  
系统基础包含角色权限控制、数据字典、菜单配置等等  
人事模块包含登入用户、人事档案、岗位岗级、社保商保  
后勤模块包含资产出入库，宿舍模块、水电通讯费  

### 界面预览

![登入界面](https://images.gitee.com/uploads/images/2021/0604/113700_c84216d7_7525468.png "11.png")

![首页](https://images.gitee.com/uploads/images/2021/0604/113712_45ea88c8_7525468.png "22.png")

![角色权限管理](https://images.gitee.com/uploads/images/2021/0604/113943_2d0f9c2d_7525468.png "33.png")

![菜单配置](https://images.gitee.com/uploads/images/2021/0604/113956_71990ed5_7525468.png "44.png")

![部门组织机构](https://images.gitee.com/uploads/images/2021/0604/114016_847edbef_7525468.png "55.png")

![登入用户管理](https://images.gitee.com/uploads/images/2021/0604/114034_1863f008_7525468.png "66.png")

![岗位岗级](https://images.gitee.com/uploads/images/2021/0604/114051_75c88d5e_7525468.png "77.png")

![宿舍模块](https://images.gitee.com/uploads/images/2021/0604/114109_1162da00_7525468.png "88.png")

![资产出入库](https://images.gitee.com/uploads/images/2021/0604/114124_bd39b379_7525468.png "99.png")

![水电费](https://images.gitee.com/uploads/images/2021/0604/114141_ee6171e0_7525468.png "111.png")

![数据字典](https://images.gitee.com/uploads/images/2021/0604/114157_ab0dd640_7525468.png "222.png")

![个人中心](https://images.gitee.com/uploads/images/2021/0604/114211_0ec542ff_7525468.png "333.png")

![修改密码](https://images.gitee.com/uploads/images/2021/0604/114226_469a5c85_7525468.png "444.png")

![锁屏](https://images.gitee.com/uploads/images/2021/0604/114241_ce9aaaa9_7525468.png "555.png")

## 安装教程

1.本机安装GIT，输入命令
```java
git clone https://gitee.com/yyzwz/oa.git
```
2.前端使用VsCode打开front文件夹，控制台输入npm i 安装依赖

3.前端控制台输入npm run dev 运行（默认8080端口）

4.控制台cd到redis目录，运行以下命令
```java
redis-server.exe redis.windows.conf
```
5.导入数据库（oa.sql）

6.使用idea导入back后端项目，maven方式导入，运行(默认1314端口)！

7.运行项目，账号admin 密码123456

### 欢迎光临我的博客：https://zwz99.blog.csdn.net/   
![我的CSDN博客](https://images.gitee.com/uploads/images/2021/0604/100703_32e14138_7525468.jpeg "132246_599dbf21_7525468.jpeg")

### 和我的公众号：元培ACM社团   
![我的微信公众号](https://images.gitee.com/uploads/images/2021/0604/100801_30c6572c_7525468.jpeg "aaaaaa.jpg")
