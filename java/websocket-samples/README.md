# WebSocket聊天室示例项目

一个基于Spring Boot和WebSocket技术的实时聊天室应用，支持多用户在线聊天、用户上下线通知等功能。

## 🚀 功能特性

- ✅ **实时聊天**：基于WebSocket的实时消息传输
- ✅ **多用户支持**：支持多个用户同时在线聊天
- ✅ **用户管理**：用户上线/下线通知
- ✅ **在线用户列表**：实时显示当前在线用户
- ✅ **消息类型**：支持系统消息和用户消息
- ✅ **连接状态监控**：WebSocket连接状态实时监控
- ✅ **异常处理**：完善的错误处理和连接恢复机制

## 🛠️ 技术栈

- **后端框架**：Spring Boot 3.x
- **WebSocket**：Java WebSocket API (@ServerEndpoint)
- **前端技术**：HTML5 + CSS3 + JavaScript (ES6+)
- **构建工具**：Maven
- **JDK版本**：Java 17+

## 📁 项目结构

```
websocket-samples/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── online/willwe/blog/websocket/websocketsamples/
│   │   │       ├── WebsocketSamplesApplication.java    # 主启动类
│   │   │       ├── config/
│   │   │       │   ├── WebSocketConfig.java           # WebSocket配置
│   │   │       │   └── WebMvcConfig.java              # Web MVC配置
│   │   │       ├── controller/
│   │   │       │   └── ChatController.java            # HTTP控制器
│   │   │       ├── endpoint/
│   │   │       │   └── ChatEndpoint.java              # WebSocket端点
│   │   │       └── entity/
│   │   │           └── Message.java                   # 消息实体类
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── chat.html                          # 聊天室页面
│   │       │   ├── chat.js                            # 前端JavaScript
│   │       │   └── chat.css                           # 样式文件
│   │       └── application.properties                 # 应用配置
│   └── test/
├── pom.xml                                            # Maven配置
└── README.md                                          # 项目文档
```

## 🔧 安装与运行

### 环境要求

- JDK 17 或更高版本
- Maven 3.6 或更高版本

### 快速开始

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd websocket-samples
   ```

2. **编译项目**
   ```bash
   mvn clean compile
   ```

3. **运行应用**
   ```bash
   mvn spring-boot:run
   ```

4. **访问应用**
   
   打开浏览器访问：http://localhost:8080

## 📖 使用说明

### 基本使用

1. **进入聊天室**
   - 访问 http://localhost:8080
   - 输入用户名
   - 点击"进入聊天室"按钮

2. **发送消息**
   - 在消息输入框中输入内容
   - 按回车键或点击"发送"按钮

3. **查看在线用户**
   - 右侧面板显示当前在线用户列表
   - 用户上线/下线会有系统通知

### 功能说明

- **系统消息**：用户进入/离开聊天室时的通知消息
- **用户消息**：用户发送的聊天消息
- **连接状态**：页面顶部显示WebSocket连接状态
- **错误处理**：连接异常时会自动尝试重连

## ⚙️ 配置说明

### application.properties

```properties
# 应用名称
spring.application.name=websocket-samples

# 服务器端口
server.port=8080

# WebSocket配置
spring.websocket.sockjs.enabled=true
# WebSocket会话超时时间（毫秒）
server.servlet.session.timeout=30m

# Tomcat连接器配置
server.tomcat.connection-timeout=20000
server.tomcat.keep-alive-timeout=20000
server.tomcat.max-keep-alive-requests=100

# 日志配置
logging.level.root=info
logging.level.online.willwe.blog.websocket=debug
```

## 🔍 API接口

### HTTP接口

- `GET /` - 重定向到聊天室页面
- `GET /chatroom` - 重定向到聊天室页面
- `GET /health` - 健康检查接口
- `GET /api/status` - 获取服务器状态

### WebSocket接口

- `ws://localhost:8080/chat/{username}` - WebSocket连接端点

## 🐛 故障排查

### 常见问题

1. **WebSocket连接失败**
   - 检查服务器是否正常启动
   - 确认端口8080未被占用
   - 查看浏览器控制台错误信息

2. **404错误**
   - 确认访问路径正确
   - 检查静态资源配置

3. **消息发送失败**
   - 检查WebSocket连接状态
   - 查看服务器日志错误信息

### 调试模式

启用调试日志：
```properties
logging.level.online.willwe.blog.websocket=debug
```

## 🤝 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 项目Issues：[GitHub Issues](https://github.com/your-repo/websocket-samples/issues)
- 邮箱：your-email@example.com

---

⭐ 如果这个项目对你有帮助，请给个Star支持一下！