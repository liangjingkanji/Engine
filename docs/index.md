加速Android开发的`引擎`
<br>

<p align="center"><strong>非常欢迎共同贡献代码/修订文档, 点击文档右上角小铅笔可直接修订文档 ↗</strong></p>
<br>

本项目存在很多独立依赖的第三方库, 为避免耦合发生并不会默认集成


使用前请初始化框架
```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Engine.initialize(this)
    }
}
```

## 其他依赖
Engine库中使用的独立依赖存在于Demo中名为sample的Module的`build.gradle`下, 要求开发者自己可选依赖

| 函数 | 描述 |
|-|-|
| [Net](https://github.com/liangjingkanji/Net) | 🍉 Android 最强大的创新式协程并发网络请求 [开发必备] |
| [BRV](https://github.com/liangjingkanji/BRV) | 🌽 Android 最强大的RecyclerView库, 比BRVAH更优雅和强大 |
| [soft-input-event](https://github.com/liangjingkanji/soft-input-event) | Android软键盘遮挡/平滑动画最佳解决方案 |
| [spannable](https://github.com/liangjingkanji/spannable) | Android最好的Spannable构建工具, 首个支持正则匹配/GIF动画/图文混排 |
| [Serialize](https://github.com/liangjingkanji/Serialize) | 应用数据管理的神器, 比数据库更方便, 可创建自动读写本地/意外销毁恢复的字段. 支持组件传递数据 |
| [StatusBar](https://github.com/liangjingkanji/StatusBar) | 透明状态栏 |
| [StateLayout](https://github.com/liangjingkanji/StateLayout) | 缺省页 |
| [Channel](https://github.com/liangjingkanji/Channel) | 事件分发框架, 类似于EventBus |
| [Tooltip](https://github.com/liangjingkanji/Tooltip) | 吐司/常见对话框工具 |
| [debugKit](https://github.com/liangjingkanji/debugkit) | 快速创建调试窗口 |
| [LogCat](https://github.com/liangjingkanji/LogCat) | 日志输出 |
