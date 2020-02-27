package test.kezy.com.customcornertextview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 文本更新TextView
 * @author changed by calex_feng
 * @version V1.1
 * @description 比如显示“我的口袋”中的每日收益，
 * 				 {@link NumChangeTextView#startValue} startValue是否冗余？唯一的好处是它是string类型
 * @date 2015-06-02
 */
@SuppressLint("AppCompatCustomView")
public class NumChangeTextView extends TextView
{
	private static final int REFRESH = 1;
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
	}

	public NumChangeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public NumChangeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	//处理动画的主要handler
	private Handler mHandler = new Handler() {
		@Override
        public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH:
				if (mCurValue < endValue) {
					setText(fnum.format(mCurValue));
					mCurValue += mRate;
					mHandler.sendEmptyMessageDelayed(REFRESH, refreshTime);
				} else if (endfnum!=null){
					setNumTextComplete(endfnum.format(endValue));
				}else{
					setNumTextComplete(startValue);
				}
				break;
			default:
				setNumTextComplete(startValue);
				break;
			}
		};
	};

	/**
	 * 设置文本文框的内容和更新参数
	 * @param value 要设置的数值
	 */
	public void setNumText(String value, DecimalFormat decimalFormat) {
		if("0.00".equals(value)||"0".equals(value)){
			setText(value);
		}if(TextUtils.isEmpty(value)){
			setText("");
		}else{
			fnum = decimalFormat;
			endfnum =decimalFormat;
			setNumText(value, 17, 30);
		}
	}

	/**
	 * 设置文本文框的内容和更新参数
	 * @param value 要设置的数值
	 */
	public void setNumText(String value) {
		if("0.00".equals(value)||"0".equals(value)){
			setText(value);
		}else{
			setNumText(value, 17, 30);
		}
	}

	/**
	 * 设置文本文框的内容和更新参数
	 * @param value 要设置的数值
	 * @param rate 数值改变的次数
	 * @param refreshTime 刷新的时间 (毫秒)
	 */
	public void setNumText(String value, int rate, int refreshTime) {
		try
		{
			this.startValue = value;
			this.mCurValue = 0.00;
			this.endValue = Double.parseDouble(value);
			mHandler.removeMessages(REFRESH);
			// 设置更新速率
			if(endValue>10){
                this.mRate = (double) (endValue / rate+Math.random());
            }else if(endValue<1){
                this.mRate=endValue;
            }else{
                this.mRate = (double) (endValue / rate);
            }
			// 设置更新时长
			this.refreshTime = refreshTime;
			BigDecimal b = new BigDecimal(mRate);
			mRate = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			mHandler.sendEmptyMessageDelayed(REFRESH, 0);
			setIsrefresh(true);
		} catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 设置文本框文本
	 * @param value 文本值
	 */
	private void setNumTextComplete(String value){
		this.setIsrefresh(false);
		setText(value);
	}
}