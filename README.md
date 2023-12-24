[![](https://jitpack.io/v/liujingxing/Indicator.svg)](https://jitpack.io/#liujingxing/Indicator) 

# 1、简介

**1、滑块可设置圆形、矩形、圆角矩形，仅需调整滑块宽高及圆角半径即可**

**2、支持`worm/smooth/scale`3中滑动模式，任意模式下均支持设置滑块宽高**

**3、支持`orientation/gravity/layoutDirection/padding`等系统内置属性**

**4、仅一个`IndicatorView`类，500行代码实现**

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

| 方法                             | xml属性                         | 可选值                         | 介绍                                |
|--------------------------------|-------------------------------|-----------------------------|-----------------------------------|
| setGravity(Int)                | gravity                       | top、left等                   | 对齐方式，左中右、上中下对齐                    | 
| setLayoutDirection(Int)        | layoutDirection               | ltr、rtl                     | 设置从左往右或从右往左                       | 
| setOrientation(Int)            | orientation                   | horizontal、vertical         | 横向、竖向                             | 
| setReverseLayout(Boolean)      | slider_reverseLayout          | true、false                  | 是否反向布局                            | 
| setSlideMode(Int)              | slide_mode                    | worm、smooth、scale           | 滑动模式                              | 
| setSliderCount(Int)            | slider_count                  | 大于等于0                       | 滑块数量                              | 
| setSliderGap(Float)            | slider_gap                    | 任意数值                        | 滑块间距                              | 
| setRoundRadius(Float)          | slider_round_radius           | 大于等于0                       | 滑块圆角半径                            | 
| setSliderNormalColor(Int)      | slider_normal_color           | 任意颜色值                       | 滑块正常颜色                            | 
| setSliderCheckedColor(Int)     | slider_checked_color          | 任意颜色值                       | 滑块选中颜色                            | 
| setPrimarySize(Float)          | slider_primary_size           | 大于等于0                       | 主方向滑块正常大小                         |             
| setPrimaryCheckedSize(Float)   | slider_primary_checked_size   | 大于等于0                       | 主方向滑块选中大小，横向布局时为宽度                |  
| setSecondarySize(Float)        | slider_secondary_size         | 大于等于0                       | 次方向滑块正常大小                         | 
| setSecondaryCheckedSize(Float) | slider_secondary_checked_size | 大于等于0                       | 次方向滑块选中大小，横向布局时为高度                | 
| scrollSlider(Int,Float)        | 无                             | 第一个参数大雨等于0，第二个参数 [0.0, 1.0) | 滑动滑块，监听到ViewPager、ViewPager2滑动时调用 | 


# 4、Demo演示

<img src="https://github.com/liujingxing/Indicator/blob/master/screen/indicator.gif" width = "300" height = "666" />

> [下载apk体验](https://github.com/liujingxing/Indicator/blob/master/screen/app-debug.apk)


# 5、致谢

部分思想借鉴[zhpanvip/viewpagerindicator](https://github.com/zhpanvip/viewpagerindicator)，感谢
