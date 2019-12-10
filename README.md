# CustomCornerTextView
自定义 圆角矩形背景的TextView 
v1版本

demo 示例

![image] (https://github.com/KeLibra/CustomCornerTextView/blob/master/image/TIM%E5%9B%BE%E7%89%8720191210110546.jpg)



代码示例
···
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".MainActivity">


    <test.kezy.com.customcornertextview.view.CustomCornerTextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="10dp"
        android:layout_marginTop="10dp"
        app:bg_color="#fd5353"
        app:text="有背景色无边框无圆角，无内paddingtext"
        app:text_color="#ffffff"
        app:text_size="14sp" />

    <test.kezy.com.customcornertextview.view.CustomCornerTextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="10dp"
        android:layout_marginTop="10dp"
        android:padding="18dp"
        app:bg_color="#ffffff"
        app:border_color="#000000"
        app:corner_radius="12dp"
        app:radius_border_width="1dp"
        app:text="有边框有圆角"
        app:text_size="30sp" />

    <test.kezy.com.customcornertextview.view.CustomCornerTextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="10dp"
        android:layout_marginTop="10dp"
        android:padding="4dp"
        app:bg_color="#f23c6c"
        app:border_color="#0000ff"
        app:radius_border_width="2dp"
        app:radius_bottom_left="0dp"
        app:radius_bottom_right="15dp"
        app:radius_top_left="15dp"
        app:radius_top_right="0dp"
        app:text="有背景有边框有部分圆角"
        app:text_color="#ffffff"
        app:text_size="14sp" />

    <test.kezy.com.customcornertextview.view.CustomCornerTextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp" />

    <test.kezy.com.customcornertextview.view.CustomCornerTextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        app:text="textview基线测试" />
</ LinearLayout>
···
