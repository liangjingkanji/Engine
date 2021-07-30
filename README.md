
<p align="center"><strong>本库为快速开发而生</strong></p>

<p align="center"><a href="http://liangjingkanji.github.io/Engine/">使用文档</a>
 | <a href="https://serverless-page-bucket-5ka5085e-1252757332.cos-website.ap-shanghai.myqcloud.com">备用访问</a>
</p>

<p align="center">
<a href="https://jitpack.io/#liangjingkanji/Engine"><img src="https://jitpack.io/v/liangjingkanji/Engine.svg"/></a>
<img src="https://img.shields.io/badge/language-kotlin-orange.svg"/>
<img src="https://img.shields.io/badge/license-Apache-blue"/>
<a href="https://liangjingkanji.github.io/Engine/api/"><img src="https://img.shields.io/badge/api-%E5%87%BD%E6%95%B0%E6%96%87%E6%A1%A3-red"/></a>
<a href="https://jq.qq.com/?_wv=1027&k=vWsXSNBJ"><img src="https://img.shields.io/badge/QQ群-752854893-blue"/></a>
</p>

<br>
<p align="center"><strong>欢迎贡献代码/问题</strong></p>
<br>



## 使用

根据需求复制粘贴文件, 当然也提供直接依赖方式.  只有共同维护本项目才能满足不断变化的开发需求


## 安装

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
implementation 'com.github.liangjingkanji:Engine:0.0.27'
```


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
