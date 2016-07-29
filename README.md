[TOC]

# MgChatServer
MgChat服务器端
使用Mybatis管理数据库


## 解释说明
### Appliation 程序包
 - AuthVertify 处理客户端的登陆验证
 - MgCharServerApplication 服务器端主程序
 
### Bean 
 - Message 封装用户发送的信息及部分控制信息
 - User 封装用户信息

### ChatServer 聊天服务器
 - ChatServer 处理在线客户端之间的消息交互
 - MonitorServer 处理离线客户端之间的消息交互

### Config MyBatis配置文件
 - Manager 管理配置
 - User 用户配置

### Dao 数据库交互
使用Mybatis接口式编程
 - ManagerDao 与管理相关的数据库操作
 - UserDao 与用户相关的数据库操作
 
### Servlet 处理注册页面提交的数据
 - MailVertifyServlet 邮箱验证Servlet,前天页面通过ajax验证邮箱是否被注册
 - RegisterServlet 注册信息提取Servlet

### Util 工具类
 MD5Util 加密类，将密码加密加盐后存入数据库
 
## web
web端使用tomcat做服务器
注册页面为根目录下的register.html
注册成功后跳转到result.jsp页面，显示成功信息
