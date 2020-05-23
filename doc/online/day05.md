# 05-优化获取用户微信信息的逻辑
针对获取用户微信信息做了下面几方面的优化：
- 一个手机号只能绑定在同一个微信号下
- 缓存用户的微信信息，避免每次到用户页面都获取微信登录信息
- 去掉获取微信信息的提示框
- 如果成功获取微信信息，使用微信昵称和头像代替之前的默认姓名和头像