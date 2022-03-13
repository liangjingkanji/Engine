每个项目必不可少的界面基类. 也可以方便快速的创建. 

在Engine库中的所有基类都是以`Engine`为前缀的, 不使用Base为前缀是考虑到如果是旧项目使用可能会发生命名冲突不便于使用

每个界面都应当继承自`Engine<组件类型>`的抽象类

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

## 基类

例如: EngineActivity 提供最基础的界面

```kotlin
class MainActivity : EngineActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun initView() {
    }

    override fun initData() {
    }
}
```

## 标题栏基类

如果你存在标题栏请继承`EngineToolbarActivity`

```kotlin
class MainActivity : EngineToolbarActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun initView() {
    }

    override fun initData() {
    }
}
```

<img src="https://i.loli.net/2021/10/10/SG5KjaJe2o8MOX6.png" width="250"/>

标题栏使用ConstraintLayout中包裹三个TextView实现. 具体请打开`layout_engine_toolbar`的源码查看即可

```kotlin
lateinit var actionbar: LinearLayout
lateinit var actionLeft: TextView
lateinit var actionRight: TextView
lateinit var actionTitle: TextView
```

1. 你可以直接使用其视图引用修改标题样式
2. 复制Engine库中的`layout_engine_toolbar.xml`到项目中修改. 如果不想影响原有视图引用或者左侧图标的点击finish事件建议保持Id相同
3. 实现EngineToolbarActivity的函数`onCreateToolbar`
3. 直接继承`EngineActivity`创建自己的Toolbar

> 避免过度封装