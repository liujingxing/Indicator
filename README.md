[![](https://jitpack.io/v/liujingxing/Indicator.svg)](https://jitpack.io/#liujingxing/Indicator) 

# 1、简介

**1、滑块可设置圆形、矩形、圆角矩形，仅需调整滑块宽高及圆角半径即可**

**2、支持`worm/smooth/scale`3中滑动模式，任意模式下均支持设置滑块宽高**

**3、支持`orientation/gravity/layoutDirection/padding`等系统内置属性**


# 2、xml属性介绍


```xml
<com.ljx.view.IndicatorView                            
    android:id="@+id/indicator_view2"                  
    android:layout_width="wrap_content"  //wrap_content、match_parent及任意固定值  
    android:layout_height="wrap_content"               
    android:layout_gravity="center"                    
    android:background="#66333333"                     
    android:gravity="center"         //start、left、top等指
    android:layoutDirection="ltr"      //ltr、rtl                
    android:orientation="horizontal"    //horizontal、vertical               
    android:padding="10dp"
    app:slider_reverseLayout="false"    // 布局反转                             
    app:slide_mode="smooth"        //worm、smooth、scale
    app:slider_normal_color="@color/teal_700"     //滑块正常颜色
    app:slider_checked_color="@color/purple_500"  //滑块选中颜色     
    app:slider_gap="5dp"         //滑块之间的间距
    app:slider_round_radius="5dp"       //滑块圆角半径
    app:slider_primary_size="10dp"            //主方向滑块正常大小                            
    app:slider_primary_checked_size="15dp"    //主方向滑块选中大小，orientation为horizontal时，就是滑块选中的宽度       
    app:slider_secondary_size="10dp"          //次方向滑块正常大小         
    app:slider_secondary_checked_size="10dp"  //次方向滑块选中大小，orientation为horizontal时，就是滑块选中的高度         
    app:slider_count="5" />                   //滑块数量       
```

# 3、IndicatorView类方法介绍

```java
setGravity(Int)    //对齐方式，左中右、上中下对齐
setLayoutDirection(Int)  //ltr、rtl
setOrientation(Int)    //横向、竖向
setReverseLayout(Boolean)  //是否反向布局
setSlideMode(Int)  //滑动模式 WORM、SMOOTH、SCALE
setSliderCount(Int)  //滑块数量
setSliderGap(Float)   //滑块间距
setRoundRadius(Float)  //滑块圆角半径
setSliderNormalColor(Int)  //滑块正常颜色
setSliderCheckedColor(Int)  //滑块选中颜色
setPrimarySize(Float)          //主方向滑块正常大小                   
setPrimaryCheckedSize(Float)   //主方向滑块选中大小，orientation为horizontal时，就是滑块选中的宽度           
setSecondarySize(Float)        //次方向滑块正常大小         
setSecondaryCheckedSize(Float) //次方向滑块选中大小，orientation为horizontal时，就是滑块选中的高度         
scrollSlider(Int,Float)  //滑动滑块，监听到ViewPager、ViewPager2滑动时调用
```

# 4、Demo演示


