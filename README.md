[![](https://jitpack.io/v/liujingxing/Indicator.svg)](https://jitpack.io/#liujingxing/Indicator)

# 1、简介

**1、滑块可设置圆形、矩形、圆角矩形，仅需调整滑块宽高及圆角半径即可**

**2、支持`worm/smooth/scale`3中滑动模式，任意模式下均支持设置滑块宽高**

**3、支持`orientation/gravity/layoutDirection/padding`等系统内置属性**

**4、支持绑定ViewPager/ViewPager2，自动监听数据源变化及滑动事件，并自动调整滑块数量及位置**

# 2、依赖

```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}

dependencies {
    implementation 'com.github.liujingxing:Indicator:1.0.0'  
}
```

# 3、方法/属性介绍

| 方法                                   | xml属性                           | 介绍                     |
|--------------------------------------|---------------------------------|------------------------|
| scrollSlider(Int,Float)              | 无                               | 滑动滑块，有滑动事件时可调用         |
| setupWithViewPager(ViewPager)        | 无                               | 监听数据源及滑动事件，自动调整滑块数量及位置 |
| setupWithViewPager2(ViewPager2)      | 无                               | 监听数据源及滑动事件，自动调整滑块数量及位置 |
| setGravity(Int)                      | `android:gravity`               | 设置对齐方式，左中右、上中下对齐       | 
| setOrientation(Int)                  | `android:orientation`           | 设置横向、竖向                | 
| setLayoutDirection(Int)              | `android:layoutDirection`       | 设置从左往右或从右往左            | 
| setReverseLayout(Boolean)            | `slider_reverseLayout`          | 设置是否反向布局               | 
| setSlideMode(Int)                    | `slide_mode`                    | 设置滑动模式                 | 
| setSliderCount(Int)                  | `slider_count`                  | 设置滑块数量                 | 
| setSliderGap(Float)                  | `slider_gap`                    | 设置滑块间距                 | 
| setSliderRoundRadius(Float)          | `slider_round_radius`           | 设置滑块圆角半径               |
| setSliderColor(Int,Int)              | 无                               | 设置滑块正常、选中颜色            |
| setSliderColor(Int)                  | `slider_normal_color`           | 设置滑块正常颜色               | 
| setSliderCheckedColor(Int)           | `slider_checked_color`          | 设置滑块选中颜色               | 
| setSliderPrimarySize(Float,Float)    | 无                               | 设置主方向滑块正常、选中大小         |
| setSliderPrimarySize(Float)          | `slider_primary_size`           | 设置主方向滑块正常大小            |
| setSliderPrimaryCheckedSize(Float)   | `slider_primary_checked_size`   | 设置主方向滑块选中大小，横向布局时为宽度   |
| setSliderSecondarySize(Float,Float)  | 无                               | 设置次方向滑块正常、选中大小         |
| setSliderSecondarySize(Float)        | `slider_secondary_size`         | 设置次方向滑块正常大小            |
| setSliderSecondaryCheckedSize(Float) | `slider_secondary_checked_size` | 设置次方向滑块选中大小，横向布局时为高度   |

# 4、Demo演示

<img src="https://github.com/liujingxing/Indicator/blob/master/screen/indicator.gif" width = "300" height = "666" />

> [下载apk体验](https://github.com/liujingxing/Indicator/blob/master/screen/app-debug.apk)

# 5、致谢

部分思想借鉴[zhpanvip/viewpagerindicator](https://github.com/zhpanvip/viewpagerindicator)，感谢
