# 邀请客户端功能实现说明

## 功能概述

本次实现为Android应用添加了完整的邀请客户端功能，包括：

1. **Counsellor端**：可以通过邮箱邀请新客户端
2. **Client端**：可以接收、查看和处理邀请请求

## 实现的功能

### 1. 后端API集成

- **LinkRequestApiService**: 与后端LinkRequest API的接口
- **数据模型**: LinkRequest, CounsellorLinkRequestDto, JournalLinkRequestDto等
- **API端点**:
  - `POST /api/linkrequest/{counsellorId}` - 创建邀请请求
  - `GET /api/linkrequest/journal/all-link-requests/{journalUserId}` - 获取用户的邀请
  - `POST /api/linkrequest/{requestId}/decision/{journalUserId}` - 处理邀请决定

### 2. Counsellor端功能

#### InviteClientScreen
- 输入客户端邮箱地址
- 发送邀请请求
- 显示成功/错误消息
- 邮箱格式验证

#### 导航集成
- CounsellorHomeScreen的"Invite"按钮 → InviteClientScreen
- ClientsScreen右上角的"+"按钮 → InviteClientScreen

### 3. Client端功能

#### InviteNotificationScreen
- 显示所有待处理的邀请请求
- 接受或拒绝邀请
- 显示Counsellor信息和请求时间

#### HomeScreen集成
- 添加了邀请通知图标按钮
- 支持显示未读邀请数量徽章

## 使用流程

### Counsellor邀请Client:
1. Counsellor登录后进入CounsellorHomeScreen
2. 点击"Invite"卡片或进入ClientsScreen点击"+"按钮
3. 在InviteClientScreen中输入客户端邮箱
4. 点击"Send Invitation"发送邀请

### Client处理邀请:
1. Client登录后在HomeScreen看到邀请通知图标
2. 点击通知图标进入InviteNotificationScreen
3. 查看邀请详情，选择"Accept"或"Decline"
4. 处理完成后邀请从列表中移除

## 文件结构

```
app/src/main/java/com/example/nus/
├── api/
│   └── LinkRequestApiService.kt          # API接口定义
├── model/
│   └── LinkRequest.kt                    # 数据模型
├── ui/
│   ├── components/
│   │   └── NotificationBadge.kt          # 通知徽章组件
│   └── screens/
│       ├── InviteClientScreen.kt         # 邀请客户端界面
│       └── InviteNotificationScreen.kt   # 邀请通知界面
├── viewmodel/
│   ├── InviteClientViewModel.kt          # 邀请客户端逻辑
│   └── InviteNotificationViewModel.kt    # 邀请通知逻辑
└── utils/
    └── InviteTestHelper.kt               # 测试工具
```

## 后端依赖

此功能依赖后端的以下API端点：

- `POST /api/linkrequest/{counsellorId}` - 创建邀请
- `GET /api/linkrequest/journal/all-link-requests/{journalUserId}` - 获取邀请列表
- `POST /api/linkrequest/{requestId}/decision/{journalUserId}` - 处理邀请决定

确保后端服务正在运行并且这些端点可用。

## 测试建议

1. 使用InviteTestHelper中的测试方法验证API连接
2. 测试邀请创建、获取和决定的完整流程
3. 验证错误处理和用户反馈
4. 测试不同的邮箱格式和边界情况

## 注意事项

- 邀请功能需要有效的网络连接
- 确保用户已正确登录并有有效的用户ID
- 邮箱地址必须是已注册的用户
- 重复邀请会被后端拒绝
