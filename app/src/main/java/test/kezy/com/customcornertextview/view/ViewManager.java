package test.kezy.com.customcornertextview.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;


public class ViewManager {
    private final static String WEBVIEW_ACTIVITY = "com.jifen.open.framework.ui.WebViewActivity";

    private final static int DEFAULT_TIME = 5;

    private volatile static ViewManager manager;

    private CoinPetView floatBallView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams floatBallParams;
    private Context mContext;
    private View.OnTouchListener mFloatBallOnTouchListener;


    private static boolean isBeingDrag = false;


    private long lastTime = 0;

    private ViewManager() {

    }

    public static ViewManager getInstance() {
        if (manager == null) {
            synchronized (ViewManager.class) {
                if (manager == null) {
                    manager = new ViewManager();
                }
            }
        }
        return manager;
    }

    public synchronized ViewManager setContext(Context context) {
        if (this.mContext == null) {
            this.mContext = context;
            init();
        }
        return this;
    }

    private void init() {
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        floatBallView = new CoinPetView(mContext);
        mFloatBallOnTouchListener = new View.OnTouchListener() {
            float startX;
            float startY;
            float tempX;
            float tempY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getRawX();
                        startY = event.getRawY();

                        tempX = event.getRawX();
                        tempY = event.getRawY();
                        //发射台出现，开启子线程查询后台运行进程
                        //colloectRunningProccess();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getRawX() - startX;
                        float y = event.getRawY() - startY;

                        floatBallParams.x -= x;
                        floatBallParams.y += y;

                        isBeingDrag = true;

                        //拖动时显示火箭发射台
                        windowManager.updateViewLayout(floatBallView, floatBallParams);

                        startX = event.getRawX();
                        startY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //判断手松开始View的横坐标是靠近屏幕哪一侧，将View移动至哪一侧
                        float endX = event.getRawX();
                        float endY = event.getRawY();

                        isBeingDrag = false;


                        windowManager.updateViewLayout(floatBallView, floatBallParams);
                        //如果初始落点与松手落点的坐标差值超过6个像素，则拦截该点击事件
                        //否则继续传递，将事件交给OnClickListener函数处理
                        if (Math.abs(endX - tempX) > 6 && Math.abs(endY - tempY) > 6) {
                            return true;
                        }
                        break;
                }
                return false;
            }
        };

        initListener();
    }

    private void initListener() {
        if (floatBallView != null) {
            floatBallView.setOnTouchListener(mFloatBallOnTouchListener);
        }
    }


    //显示悬浮球
    public synchronized void showFloatBall() {
        Log.e("--------------------", " mContext: " + mContext + " , windowManager: " + windowManager);
        if (mContext == null || windowManager == null) {
            return;
        }


        Log.e("--------------------", " floatBallView: " + floatBallView);
        if (floatBallView == null) {
            floatBallView = new CoinPetView(mContext);
            initListener();
        }

        if (floatBallParams == null) {
            floatBallParams = new WindowManager.LayoutParams();
            floatBallParams.width = floatBallView.getWidth();
            floatBallParams.height = floatBallView.getHeight();
            floatBallParams.gravity = Gravity.CENTER;
            floatBallParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            floatBallParams.format = PixelFormat.RGBA_8888;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                floatBallParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                floatBallParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
        }
        Log.e("--------------------", " getParent: " + floatBallView.getParent() );
        if (floatBallView.getParent() != null) {
            return;
        }
        windowManager.addView(floatBallView, floatBallParams);

    }


    //隐藏悬浮球
    public void hideFloatBall() {
        if (floatBallView != null && windowManager != null && floatBallView.getParent() != null) {
            windowManager.removeView(floatBallView);
        }
    }


    //获取屏幕宽度
    private int getScreenWidth() {
        Point point = new Point();
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    //获取屏幕高度
    private int getScreenHeight() {
        Point point = new Point();
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    //获取状态栏高度
    private int getStatusHeight() {
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object obj = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");
            int x = (int) field.get(obj);
            return mContext.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    //一键降温

    //释放资源
    public void releaseResource() {
        //移除悬浮球和菜单
        hideFloatBall();

        if (windowManager != null) {
            windowManager = null;
        }
        if (floatBallView != null) {
            floatBallView = null;
        }
        if (floatBallParams != null) {
            floatBallParams = null;
        }
        if (mContext != null) {
            mContext = null;
        }
        if (manager != null) {
            manager = null;
        }
    }

    private static Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    public boolean isIsBeingDrag() {
        return isBeingDrag;
    }


}
