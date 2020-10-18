
<p align="center"><strong>本库为快速开发常用需求存储</strong></p>

<p align="center">
<a href="https://jitpack.io/#liangjingkanji/Engine"><img src="https://jitpack.io/v/liangjingkanji/Engine.svg"/></a>
<img src="https://img.shields.io/badge/language-kotlin-orange.svg"/>
<img src="https://img.shields.io/badge/license-Apache-blue"/>
<a href="https://jq.qq.com/?_wv=1027&k=vWsXSNBJ"><img src="https://img.shields.io/badge/QQ群-752854893-blue"/></a>
</p>

## 使用

推荐根据需求复制粘贴文件, 当然也提供直接依赖方式


| 工具                     |                                      |
| ------------------------ | ------------------------------------ |
| KeyBoard.kt              | 键盘相关, 键盘遮挡/显示/隐藏等       |
| Preference.kt            | SharePreference存储                  |
| DigitsInputFilter.kt     | 输入框小数点限制                     |
| Intents.kt               | 意图创建, 开启Activity/Service等组件 |
| JetPack.kt               | JetPack框架相关                      |
| ThrottleClickListener.kt | 点击事件防抖动                       |
| UnitUtils.kt             | 单位换算                             |
| Dialog.kt                | 对话框创建                           |
| DataBindingComponent.kt  | DataBinding常用需求                  |
| DataBindUtils.kt         | 创建DataBinding                      |
| ObservableIml            | 实现接口但等效于继承BaseObservable   |

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
implementation 'com.github.liangjingkanji:Engine:0.0.10'
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