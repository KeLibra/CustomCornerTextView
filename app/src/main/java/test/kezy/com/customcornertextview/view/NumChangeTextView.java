package test.kezy.com.customcornertextview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 文本更新TextView
 */
@SuppressLint("AppCompatCustomView")
public class NumChangeTextView extends TextView {

    /**
     * 刷新类型
     * up ： 增长
     * down ： 减少
     */
    public enum REFRESH_TYPE {
        TYPE_UP,
        TYPE_DOWN;
    }

    private static final int UPREFRESH = 1; // 增长更新msg
    private static final int DOWNREFRESH = 2; // 减少更新msg
    // 递增 的变量值
    private double mRate;
    //初始值
    private String startValue;
    // 当前显示的值
    private double mCurValue;
    // 当前变化后最终状态的目标值
    private double endValue;
    //刷新时间
    private int refreshTime;
    //显示始终保持两位小数
    DecimalFormat fnum = new DecimalFormat("0.00");
    private boolean isrefresh;
    //当有这个fnum的时候设置成最后输出的格式
    private DecimalFormat endfnum;

    private double changeValue; // 用于计算 更新变量值的

    public DecimalFormat getEndfnum() {
        return endfnum;
    }

    public void setEndfnum(DecimalFormat endfnum) {
        this.endfnum = endfnum;
    }

    public boolean isrefresh() {
        return isrefresh;
    }

    public void setIsrefresh(boolean isrefresh) {
        this.isrefresh = isrefresh;
    }

    public DecimalFormat getFnum() {
        return fnum;
    }

    public void setFnum(DecimalFormat fnum) {
        this.fnum = fnum;
    }

    public NumChangeTextView(Context context) {
        super(context);
        initHandler(context);
    }


    public NumChangeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHandler(context);
    }

    public NumChangeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initHandler(context);
    }

    private void initHandler(Context context) {
        if (mHandler == null) {
            mHandler = new MyHandler(context);
        }
    }

    private MyHandler mHandler;

    /**
     * 声明一个静态的Handler内部类，并持有外部类的弱引用
     */
    private class MyHandler extends Handler {

        private final WeakReference<Context> mActivty;

        private MyHandler(Context context) {
            this.mActivty = new WeakReference<Context>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Context context = mActivty.get();
            if (context != null) {
                switch (msg.what) {
                    case UPREFRESH:
                        if (mCurValue < endValue) {
                            setText(fnum.format(mCurValue));
                            mCurValue += mRate;
                            if (mHandler != null) {
                                mHandler.sendEmptyMessageDelayed(UPREFRESH, refreshTime);
                            }
                        } else if (endfnum != null) {
                            setNumTextComplete(endfnum.format(endValue));
                        } else {
                            setNumTextComplete(startValue);
                        }
                        break;

                    case DOWNREFRESH:
                        if (mCurValue > endValue) {
                            setText(fnum.format(mCurValue));
                            mCurValue -= mRate;
                            if (mHandler != null) {
                                mHandler.sendEmptyMessageDelayed(DOWNREFRESH, refreshTime);
                            }
                        } else if (endfnum != null) {
                            setNumTextComplete(endfnum.format(endValue));
                        } else {
                            setNumTextComplete(startValue);
                        }
                        break;
                    default:
                        setNumTextComplete(startValue);
                        break;
                }
            }
        }
    }

    /**
     * 设置文本文框的内容和更新参数
     *
     * @param value 要设置的数值
     */
    public void setNumText(String value, DecimalFormat decimalFormat, REFRESH_TYPE refreshType) {
        if ("0.00".equals(value) || "0".equals(value)) {
            setText(value);
        }
        if (TextUtils.isEmpty(value)) {
            setText("");
        } else {
            fnum = decimalFormat;
            endfnum = decimalFormat;
            setNumText(refreshType, value, 17, 30);
        }
    }

    /**
     * 设置文本文框的内容和更新参数
     *
     * @param value 要设置的数值
     */
    public void setNumText(String value, REFRESH_TYPE refreshType) {
        if ("0.00".equals(value) || "0".equals(value)) {
            setText(value);
        } else {
            setNumText(refreshType, value, 17, 30);
        }
    }

    /**
     * 设置文本文框的内容和更新参数
     *
     * @param refreshType 刷新类型： up 增长， down， 减少，  默认增长
     * @param value       要设置的数值
     * @param rate        数值改变的次数
     * @param refreshTime 刷新的时间 (毫秒)
     */
    public void setNumText(REFRESH_TYPE refreshType, String value, int rate, int refreshTime) {
        try {

            int handlerType;
            if (mHandler != null) {
                mHandler.removeMessages(UPREFRESH);
                mHandler.removeMessages(DOWNREFRESH);
            }
            if (refreshType == REFRESH_TYPE.TYPE_DOWN) {
                setText(value);
                this.startValue = "0.00";
                this.mCurValue = Double.parseDouble(value);
                this.endValue = 0.00;
                this.changeValue = Double.parseDouble(value);
                handlerType = DOWNREFRESH;
            } else {
                this.startValue = value;
                this.mCurValue = 0.00;
                this.endValue = Double.parseDouble(value);
                this.changeValue = Double.parseDouble(value);
                handlerType = UPREFRESH;
            }

            // 设置更新速率
            if (changeValue > 10) {
                this.mRate = (double) (changeValue / rate + Math.random());
            } else if (changeValue < 1) {
                this.mRate = changeValue;
            } else {
                this.mRate = (double) (changeValue / rate);
            }
            // 设置更新时长
            this.refreshTime = refreshTime;
            BigDecimal b = new BigDecimal(mRate);
            mRate = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (mHandler != null) {
                mHandler.sendEmptyMessageDelayed(handlerType, 0);
            }
            setIsrefresh(true);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置文本框文本
     *
     * @param value 文本值
     */
    private void setNumTextComplete(String value) {
        this.setIsrefresh(false);
        setText(value);
        if (mHandler != null) {
            mHandler.removeMessages(UPREFRESH);
            mHandler.removeMessages(DOWNREFRESH);
        }
    }
}