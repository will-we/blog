// WebSocket聊天室客户端JavaScript代码

class ChatClient {
    constructor() {
        this.websocket = null;
        this.username = '';
        this.currentChatTarget = null; // null表示群聊，字符串表示私聊对象
        this.onlineUsers = new Set();
        
        this.initializeElements();
        this.bindEvents();
    }

    // 初始化DOM元素引用
    initializeElements() {
        // 登录相关
        this.loginForm = document.getElementById('loginForm');
        this.usernameInput = document.getElementById('usernameInput');
        this.loginBtn = document.getElementById('loginBtn');
        
        // 聊天界面相关
        this.chatContainer = document.getElementById('chatContainer');
        this.messageArea = document.getElementById('messageArea');
        this.messageInput = document.getElementById('messageInput');
        this.sendBtn = document.getElementById('sendBtn');
        
        // 状态相关
        this.statusDot = document.getElementById('statusDot');
        this.statusText = document.getElementById('statusText');
        this.currentUserEl = document.getElementById('currentUser');
        this.onlineCountEl = document.getElementById('onlineCount');
        this.userListEl = document.getElementById('userList');
        
        // 聊天标题
        this.chatTitle = document.getElementById('chatTitle');
        this.chatSubtitle = document.getElementById('chatSubtitle');
        
        // 群聊按钮
        this.allUsersBtn = document.getElementById('allUsersBtn');
    }

    // 绑定事件监听器
    bindEvents() {
        // 登录事件
        this.loginBtn.addEventListener('click', () => this.login());
        this.usernameInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.login();
        });
        
        // 发送消息事件
        this.sendBtn.addEventListener('click', () => this.sendMessage());
        this.messageInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.sendMessage();
        });
        
        // 群聊模式切换
        this.allUsersBtn.addEventListener('click', () => this.switchToGroupChat());
        
        // 页面关闭时断开连接
        window.addEventListener('beforeunload', () => {
            if (this.websocket) {
                this.websocket.close();
            }
        });
    }

    // 用户登录
    login() {
        const username = this.usernameInput.value.trim();
        if (!username) {
            alert('请输入用户名');
            return;
        }
        
        if (username.length > 20) {
            alert('用户名不能超过20个字符');
            return;
        }
        
        this.username = username;
        
        // 先检查服务器状态
        this.checkServerStatus().then(() => {
            this.connectWebSocket();
        }).catch(error => {
            console.error('服务器状态检查失败:', error);
            this.addSystemMessage('服务器可能未启动，尝试连接WebSocket...');
            this.connectWebSocket();
        });
    }

    // 检查服务器状态
    checkServerStatus() {
        return new Promise((resolve, reject) => {
            const protocol = window.location.protocol;
            const host = window.location.host;
            const checkUrl = `${protocol}//${host}/health`;
            
            fetch(checkUrl, {
                method: 'GET',
                timeout: 5000
            })
            .then(response => {
                if (response.ok) {
                    console.log('服务器状态正常');
                    resolve();
                } else {
                    reject(new Error(`服务器返回状态码: ${response.status}`));
                }
            })
            .catch(error => {
                reject(error);
            });
        });
    }

    // 连接WebSocket
    connectWebSocket() {
        try {
            // 构建WebSocket URL
            const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
            const wsUrl = `${protocol}//${window.location.host}/chat/${encodeURIComponent(this.username)}`;
            
            console.log('尝试连接WebSocket:', wsUrl);
            this.addSystemMessage(`正在连接到服务器...`);
            
            this.websocket = new WebSocket(wsUrl);
            
            // WebSocket事件处理
            this.websocket.onopen = (event) => this.onWebSocketOpen(event);
            this.websocket.onmessage = (event) => this.onWebSocketMessage(event);
            this.websocket.onclose = (event) => this.onWebSocketClose(event);
            this.websocket.onerror = (event) => this.onWebSocketError(event);
            
        } catch (error) {
            console.error('WebSocket连接失败:', error);
            this.addSystemMessage(`连接失败: ${error.message}`);
            alert('连接失败，请检查网络连接');
        }
    }

    // WebSocket连接成功
    onWebSocketOpen(event) {
        console.log('WebSocket连接成功');
        
        // 更新UI状态
        this.updateConnectionStatus(true);
        this.loginForm.classList.add('hidden');
        this.chatContainer.classList.remove('hidden');
        this.currentUserEl.textContent = this.username;
        
        // 启用输入框和发送按钮
        this.messageInput.disabled = false;
        this.sendBtn.disabled = false;
        this.messageInput.focus();
        
        // 显示连接成功消息
        this.addSystemMessage('连接成功，欢迎来到聊天室！');
    }

    // 接收WebSocket消息
    onWebSocketMessage(event) {
        try {
            const message = JSON.parse(event.data);
            console.log('收到消息:', message);
            
            switch (message.type) {
                case 'CHAT':
                    if (message.sender === '在线用户') {
                        this.updateOnlineUsers(message.content);
                    } else {
                        this.displayMessage(message);
                    }
                    break;
                case 'JOIN':
                    this.addSystemMessage(message.content);
                    break;
                case 'LEAVE':
                    this.addSystemMessage(message.content);
                    break;
                default:
                    this.displayMessage(message);
            }
        } catch (error) {
            console.error('解析消息失败:', error);
        }
    }

    // WebSocket连接关闭
    onWebSocketClose(event) {
        console.log('WebSocket连接关闭');
        this.updateConnectionStatus(false);
        
        // 禁用输入框和发送按钮
        this.messageInput.disabled = true;
        this.sendBtn.disabled = true;
        
        // 显示断开连接消息
        this.addSystemMessage('连接已断开');
        
        // 如果不是正常关闭，尝试重连
        if (event.code !== 1000) {
            setTimeout(() => {
                if (this.username) {
                    this.addSystemMessage('尝试重新连接...');
                    this.connectWebSocket();
                }
            }, 3000);
        }
    }

    // WebSocket连接错误
    onWebSocketError(event) {
        console.error('WebSocket错误:', event);
        console.error('WebSocket状态:', this.websocket ? this.websocket.readyState : 'undefined');
        
        let errorMsg = '连接发生错误';
        if (this.websocket) {
            switch (this.websocket.readyState) {
                case WebSocket.CONNECTING:
                    errorMsg = '正在连接中，请稍候...';
                    break;
                case WebSocket.OPEN:
                    errorMsg = '连接已建立但发生错误';
                    break;
                case WebSocket.CLOSING:
                    errorMsg = '连接正在关闭';
                    break;
                case WebSocket.CLOSED:
                    errorMsg = '连接已关闭，请检查服务器状态';
                    break;
                default:
                    errorMsg = '未知连接状态';
            }
        }
        
        this.addSystemMessage(errorMsg);
        this.updateConnectionStatus(false);
    }

    // 发送消息
    sendMessage() {
        const content = this.messageInput.value.trim();
        if (!content || !this.websocket || this.websocket.readyState !== WebSocket.OPEN) {
            return;
        }
        
        const message = {
            type: 'CHAT',
            content: content,
            sender: this.username,
            timestamp: new Date().toISOString(),
            isPrivate: this.currentChatTarget !== null,
            receiver: this.currentChatTarget
        };
        
        try {
            this.websocket.send(JSON.stringify(message));
            this.messageInput.value = '';
        } catch (error) {
            console.error('发送消息失败:', error);
            this.addSystemMessage('发送消息失败');
        }
    }

    // 显示消息
    displayMessage(message) {
        const messageEl = document.createElement('div');
        messageEl.className = 'message';
        
        // 判断是否为自己发送的消息
        if (message.sender === this.username) {
            messageEl.classList.add('own');
        }
        
        // 创建消息内容
        const contentEl = document.createElement('div');
        contentEl.className = 'message-content';
        
        // 如果是私聊消息，添加私聊标识
        if (message.isPrivate) {
            const privateIndicator = document.createElement('div');
            privateIndicator.className = 'private-chat-indicator';
            privateIndicator.textContent = message.sender === this.username ? 
                `私聊给 ${message.receiver}` : `${message.sender} 私聊`;
            contentEl.appendChild(privateIndicator);
        }
        
        // 添加发送者信息（群聊时显示）
        if (!message.isPrivate && message.sender !== this.username) {
            const senderEl = document.createElement('div');
            senderEl.className = 'message-sender';
            senderEl.textContent = message.sender;
            contentEl.appendChild(senderEl);
        }
        
        // 添加消息文本
        const textEl = document.createElement('div');
        textEl.textContent = message.content;
        contentEl.appendChild(textEl);
        
        // 添加时间戳
        const timeEl = document.createElement('div');
        timeEl.className = 'message-time';
        timeEl.textContent = this.formatTime(message.timestamp);
        contentEl.appendChild(timeEl);
        
        messageEl.appendChild(contentEl);
        this.messageArea.appendChild(messageEl);
        
        // 滚动到底部
        this.scrollToBottom();
    }

    // 添加系统消息
    addSystemMessage(content) {
        const messageEl = document.createElement('div');
        messageEl.className = 'message system';
        
        const contentEl = document.createElement('div');
        contentEl.className = 'message-content';
        contentEl.textContent = content;
        
        messageEl.appendChild(contentEl);
        this.messageArea.appendChild(messageEl);
        
        this.scrollToBottom();
    }

    // 更新在线用户列表
    updateOnlineUsers(userListStr) {
        const users = userListStr ? userListStr.split(',').filter(u => u.trim()) : [];
        this.onlineUsers = new Set(users);
        
        // 更新在线用户数
        this.onlineCountEl.textContent = users.length;
        
        // 更新用户列表
        this.userListEl.innerHTML = '';
        users.forEach(user => {
            if (user !== this.username) { // 不显示自己
                const userEl = document.createElement('div');
                userEl.className = 'user-item';
                userEl.textContent = user;
                userEl.addEventListener('click', () => this.switchToPrivateChat(user));
                this.userListEl.appendChild(userEl);
            }
        });
    }

    // 切换到私聊模式
    switchToPrivateChat(targetUser) {
        this.currentChatTarget = targetUser;
        this.chatTitle.textContent = `与 ${targetUser} 私聊`;
        this.chatSubtitle.textContent = '私人对话';
        this.messageInput.placeholder = `向 ${targetUser} 发送私聊消息...`;
        
        // 更新用户列表选中状态
        document.querySelectorAll('.user-item').forEach(el => {
            el.classList.remove('selected');
            if (el.textContent === targetUser) {
                el.classList.add('selected');
            }
        });
        this.allUsersBtn.classList.remove('selected');
    }

    // 切换到群聊模式
    switchToGroupChat() {
        this.currentChatTarget = null;
        this.chatTitle.textContent = '群聊';
        this.chatSubtitle.textContent = '与所有人聊天';
        this.messageInput.placeholder = '输入消息...';
        
        // 更新选中状态
        document.querySelectorAll('.user-item').forEach(el => {
            el.classList.remove('selected');
        });
        this.allUsersBtn.classList.add('selected');
    }

    // 更新连接状态
    updateConnectionStatus(connected) {
        if (connected) {
            this.statusDot.classList.add('connected');
            this.statusText.textContent = '已连接';
        } else {
            this.statusDot.classList.remove('connected');
            this.statusText.textContent = '未连接';
        }
    }

    // 格式化时间
    formatTime(timestamp) {
        const date = new Date(timestamp);
        return date.toLocaleTimeString('zh-CN', {
            hour: '2-digit',
            minute: '2-digit'
        });
    }

    // 滚动到底部
    scrollToBottom() {
        this.messageArea.scrollTop = this.messageArea.scrollHeight;
    }
}

// 页面加载完成后初始化聊天客户端
document.addEventListener('DOMContentLoaded', () => {
    new ChatClient();
});