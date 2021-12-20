加速Android开发的`引擎`
<br>

<p align="center"><strong>非常欢迎共同贡献代码/修订文档, 点击文档右上角小铅笔可直接修订文档 ↗</strong></p>
<br>

本项目存在很多独立依赖的第三方库, 为避免耦合发生并不会默认集成

### 基类

| 基类 | 描述 |
|-|-|
| Engine.kt | 当前Activity, 当前Application |
| EngineActivity | Activity |
| EngineSwipeActivity | 可侧滑返回的Activity |
| EngineToolbarActivity | 拥有工具栏的Activity |
| EngineFragment | Fragment |
| EngineNavFragment | 适用于Navigation导航框架的Fragment基类 |
| EngineDialog | 对话框基类 |
| EngineDialogFragment | DialogFragment基类 |
| EngineBottomSheetDialogFragment | BottomSheetDialogFragment基类 |

### 工具

| 工具                     |                                      |
| ------------------------ | ------------------------------------ |
| SpanBuilder.kt            | 快速实现SpannableString, 支持正则/匹配替换   |
| KeyBoard.kt              | 键盘相关, 键盘遮挡/显示/隐藏/高度...       |
| Preference.kt            | SharePreference存储, 委托属性                  |
| DigitsInputFilter.kt     | 输入框小数点以及数字位数限制                     |
| Intents.kt               | 意图创建, 开启Activity/Service等组件 |
| JetPack.kt               | JetPack框架相关                      |
| ThrottleClickListener.kt | 点击事件防抖动                       |
| UnitUtils.kt             | 单位换算                             |
| Dialog.kt                | 对话框创建                           |
| DataBindingComponent.kt  | DataBinding常用需求                  |
| DataBindUtils.kt         | 创建DataBinding                      |
| ObservableIml            | 如果不便继承BaseObservable可实现该接口等效于   |

### 视图

| 组件 | 描述 |
|-|-|
| FixedViewPager | 修复崩溃和可关闭划动的ViewPager |
| ViewPagerForMaxHeight | 使用最高页面height的ViewPager |
| GridPasswordView | 交易密码输入框 |
| ScrollPickerView | 上下划动选择器 |
| IndexSideBar | 字母索引列表 |
| MarqueeTextView | 文字跑马灯 |
| NestedRadioGroup | 支持任意嵌套的RadioButton单选 |
| SmoothCheckBox | 漂亮的平滑切换开关 |
| WaveView | 水纹波动 |
| FilterCheckBox | 过滤是否可选的CheckBox |
| FilterSeekBar | 过滤是否可滑动进度条SeekBar |
| VerificationCodeEditText | 验证码输入框 |
| FloatMenu | 浮动菜单 |

## 独立依赖
Engine库中使用的独立依赖存在于Demo中名为sample的Module的`build.gradle`下

| 函数 | 描述 |
|-|-|
| [Net](https://github.com/liangjingkanji/Net) | 基于协程的并发网络请求库 |
| [BRV](https://github.com/liangjingkanji/BRV) | 基于DSL作用域快速创建列表 |
| [Serialize](https://github.com/liangjingkanji/Serialize) | 应用数据管理的神器, 比数据库更方便, 可创建自动读写本地/意外销毁恢复的字段. 支持组件传递数据 |
| [StatusBar](https://github.com/liangjingkanji/StatusBar) | 透明状态栏 |
| [StateLayout](https://github.com/liangjingkanji/StateLayout) | 缺省页 |
| [Channel](https://github.com/liangjingkanji/Channel) | 事件分发框架, 类似于EventBus |
| [Tooltip](https://github.com/liangjingkanji/Tooltip) | 吐司/常见对话框工具 |
| [debugKit](https://github.com/liangjingkanji/debugkit) | 快速创建调试窗口 |
| [LogCat](https://github.com/liangjingkanji/LogCat) | 日志输出 |
