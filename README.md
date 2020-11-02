
<p align="center"><strong>本库为快速开发常用需求存储</strong></p>

<p align="center">
<a href="https://jitpack.io/#liangjingkanji/Engine"><img src="https://jitpack.io/v/liangjingkanji/Engine.svg"/></a>
<img src="https://img.shields.io/badge/language-kotlin-orange.svg"/>
<img src="https://img.shields.io/badge/license-Apache-blue"/>
<a href="https://jq.qq.com/?_wv=1027&k=vWsXSNBJ"><img src="https://img.shields.io/badge/QQ群-752854893-blue"/></a>
</p>

## 使用

推荐根据需求复制粘贴文件, 当然也提供直接依赖方式

### 基类

| 基类 | 描述 |
|-|-|
| Engine.kt | 初始化 |
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
| KeyBoard.kt              | 键盘相关, 键盘遮挡/显示/隐藏/高度...       |
| Preference.kt            | SharePreference存储                  |
| DigitsInputFilter.kt     | 输入框小数点以及数字位数限制                     |
| Intents.kt               | 意图创建, 开启Activity/Service等组件 |
| JetPack.kt               | JetPack框架相关                      |
| ThrottleClickListener.kt | 点击事件防抖动                       |
| UnitUtils.kt             | 单位换算                             |
| Dialog.kt                | 对话框创建                           |
| DataBindingComponent.kt  | DataBinding常用需求                  |
| DataBindUtils.kt         | 创建DataBinding                      |
| ObservableIml            | 如果不便继承BaseObservable可实现该接口等效于   |
| SpanUtils.kt            | SpannableString工具   |

### UI

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

<br>

## 依赖

在项目根目录的 build.gradle 添加仓库

```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

在 module 的 build.gradle 添加依赖

```groovy
implementation 'com.github.liangjingkanji:Engine:0.0.13'
```

<br>

## License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```