# Counsellor Login Implementation

## 概述
成功实现了在现有LoginScreen中添加用户类型切换功能，支持User和Counsellor两种登录方式。

## 实现的功能

### 1. 用户类型切换器
- 在登录界面添加了User/Counsellor切换按钮
- 采用现代化的切换器设计，选中状态为黑色背景
- 位置：在密码输入框和错误消息之间

### 2. 动态API调用
- User类型：调用 `/api/user/login`
- Counsellor类型：调用 `/api/counsellor/login`
- 使用相同的LoginRequest和LoginResponse数据模型

### 3. UI适配
- **User模式**：显示Login和Sign Up两个按钮，以及Google登录选项
- **Counsellor模式**：只显示Login按钮（全宽），不显示注册和Google登录选项
- 登录按钮文本根据用户类型动态变化

### 4. 错误处理
- 错误消息根据用户类型显示（"User not found" vs "Counsellor not found"）
- 切换用户类型时自动清除之前的错误信息

## 修改的文件

### 1. `CounsellorApiService.kt` (新建)
- 定义了counsellor相关的API接口
- 包含login和logout方法

### 2. `LoginViewModel.kt`
- 添加了UserType枚举和userType状态
- 修改login方法支持两种用户类型
- 添加toggleUserType方法
- 更新错误处理逻辑

### 3. `LoginScreen.kt`
- 添加用户类型切换器UI组件
- 根据用户类型动态显示不同的按钮布局
- 更新登录按钮文本

### 4. `ApiClient.kt`
- 添加counsellorApiService实例

### 5. `LoginResponse.kt`
- 添加@SerializedName注解确保与后端DTO兼容

## 后端对接
- 后端CounsellorController已有完整的login实现
- 使用相同的LoginRequestDto和LoginResponseDto
- API端点：`POST /api/counsellor/login`

## 使用方法
1. 打开登录界面
2. 点击"User"或"Counsellor"切换用户类型
3. 输入邮箱和密码
4. 点击对应的登录按钮
5. 系统会根据选择的类型调用相应的API端点

## 注意事项
- Counsellor登录不显示注册选项（通常counsellor账户由管理员创建）
- 两种用户类型使用相同的认证机制和session管理
- 错误处理和网络请求逻辑保持一致
