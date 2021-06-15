每个项目必不可少的界面基类. 也可以方便快速的创建. 

在Engine库中的所有基类都是以`Engine`为前缀的, 不使用Base为前缀是考虑到如果是旧项目使用可能会发生命名冲突不便于使用

每个界面都应当继承自`Engine<组件类型>`的抽象类

## 标题栏

例如: EngineActivity 提供最基础的界面

```kotlin
class MainActivity : EngineActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun initView() {
    }

    override fun initData() {
    }
}
```

如果你存在标题栏请使用

```kotlin
class MainActivity : EngineToolbarActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun initView() {
    }

    override fun initData() {
    }
}
```

假设你要完全自定义标题栏请在自己项目`layout`目录下创建一个名为`layout_engine_toolbar.xml`的布局文件.或者直接继承`EngineActivity`.

为了不影响EngineToolbarActivity的功能请尽量保持控件Id和engine库下的`layout_engine_toolbar.xml`相同

标题栏使用LinearLayout中包裹三个TextView实现. 具体请打开EngineToolbarActivity的源码查看即可

```kotlin
lateinit var rootViewWithoutToolbar: View
lateinit var toolbar: LinearLayout
lateinit var toolbarLeft: TextView
lateinit var toolbarRight: TextView
lateinit var toolbarTitle: TextView
```