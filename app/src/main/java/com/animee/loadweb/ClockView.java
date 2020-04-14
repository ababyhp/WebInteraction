package com.animee.loadweb;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

public class ClockView extends View {
    Paint paint;  //绘制钟表的画笔
    int hours, minute, seconds;   //时分秒

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
//                重新获取时间
                getTime();
//                重新绘制界面
                invalidate();
                handler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    };

    public ClockView(Context context) {
        super(context);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true); //设置抗锯齿
//        paint.setColor(Color.RED); //设置画笔的颜色
        getTime();
//       获取在布局当中设置的自定义属性，设置给view
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClockView);
        int color = typedArray.getColor(R.styleable.ClockView_clockColor, Color.BLACK);
        paint.setColor(color);
//        回收属性
        typedArray.recycle();
    }

    /* 获取当前时间的方法*/
    public void getTime() {
        Calendar calendar = Calendar.getInstance();
        hours = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
        seconds = calendar.get(Calendar.SECOND);

    }

    //     显示的内容就在onDraw方法中进行绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE);  //设置空心
        paint.setStrokeWidth(8); //设置线条宽度
//        设置内边距
        setPadding(20, 20, 20, 20);
//        绘制外层的大圈
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - 20, paint);
//      设置画笔的粗细
        paint.setStrokeWidth(4);
//        绘制内层的圆形
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - 30, paint);
//        绘制表中间轴心
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 10, paint);
//        绘制表的刻度  12个  直线   因为起始点和结尾点的坐标不好确定，需要通过旋转画布实现
        for (int i = 1; i <= 12; i++) {
//            保存画布的原有状态
            canvas.save();
//            旋转到指定的角度
            canvas.rotate(360 / 12 * i, getWidth() / 2, getHeight() / 2);
            canvas.drawLine(getWidth() / 2, 40, getWidth() / 2, 50, paint);
//            恢复旋转之前的状态
            canvas.restore();
//            提醒一秒钟之后，刷新界面
            handler.sendEmptyMessageDelayed(1, 1000);
        }

//        绘制时分秒针
//        时针
        paint.setStrokeWidth(8);
//        旋转画布，旋转的度数有当前时间决定，1个小时是30度，1分钟是0.5度
        canvas.save();
        canvas.rotate(30 * hours + 0.5f * minute, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - getHeight() / 5, paint);
        canvas.restore();

//         分针   1分钟代表6度
        paint.setStrokeWidth(5);
        canvas.save();
        canvas.rotate(6 * minute, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - getHeight() / 4, paint);
        canvas.restore();

//        秒针    1秒为6度
        paint.setStrokeWidth(3);
        canvas.save();
        canvas.rotate(6 * seconds, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - getHeight() / 3, paint);
        canvas.restore();


    }

    //    显示的尺寸，和使用时传入的宽高相关，因为整体为圆形，所以要保证宽高相同
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        1.获取传入宽高的模式
        int wmode = MeasureSpec.getMode(widthMeasureSpec);
        int hmode = MeasureSpec.getMode(heightMeasureSpec);
//        2.获取宽度和高度的最大尺寸
        int wsize = MeasureSpec.getSize(widthMeasureSpec);
        int hsize = MeasureSpec.getSize(heightMeasureSpec);
//        3.判断模式,获取最终显示的尺寸
        int size = 400;
        if (wmode == MeasureSpec.EXACTLY) {  //当宽度在布局使用时，设定为精确值
            if (hmode == MeasureSpec.EXACTLY) {
//                宽高都为具体值，谁小就取谁
                size = Math.min(hsize, wsize);
            } else {
//                宽度为精确值，高度为wrap_content
                size = wsize;
            }
        } else {
//            当宽度在布局使用时，被设定为wrap_content
            if (hmode == MeasureSpec.EXACTLY) {
//                高度是精确值
                size = hsize;
            } else {
//                宽高都是wrap_content
                size = 400;
            }
        }
//        4.将测量好的值设置给控件宽高
        setMeasuredDimension(size, size);
    }
}
