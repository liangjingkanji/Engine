/*
 * Copyright (C) 2018 Drake, Inc. https://github.com/liangjingkanji
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("unused")

package com.drake.engine.utils

import android.content.res.Resources

//<editor-fold desc="Dimension">

/*converts dp value into px*/
val Number.dp
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.density).toInt()

/*converts sp value into px*/
val Number.sp
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.scaledDensity).toInt()

//</editor-fold>


//<editor-fold desc="Memory">

val Number.KB: Long
    get() = this.toLong() * 1024L

val Number.MB: Long
    get() = this.KB * 1024

val Number.GB: Long
    get() = this.MB * 1024

//</editor-fold>


