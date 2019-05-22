package com.xzq.eg.exercise3.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.xzq.eg.exercise3.Bean.CircleRect;
import com.xzq.eg.exercise3.R;

import java.util.ArrayList;
import java.util.List;

/**
 * created by xzq on 2019/4/24
 */
public class LockView extends View {
    //状态常量
    private static final int STATE_NORMAL = 1;
    private static final int STATE_SELECT = 2;
    private static final int STATE_CORRECT = 3;
    private static final int STATE_WRONG = 4;
    //自定义属性
    private int normalColor = Color.GRAY;
    private int selectColor = Color.YELLOW;
    private int correctColor = Color.GREEN;
    private int wrongColor = Color.RED;
    private int lineWidth = -1;
    //宽高
    private int width;
    private int height;
    private int rectRadius;//每个小圆圈的宽度
    //元素相关
    private List<CircleRect> rectList;//存储所有圆圈对象的列表
    private List<CircleRect> pathList;//存储用户绘制连线上的所有圆圈对象
    //绘制相关
    private Canvas canvas;
    private Bitmap bitmap;
    private Path path;
    private Path tempPath;
    private Paint circlePaint;
    private Paint pathPaint;
    //触摸相关
    private int startX;
    private int startY;
    private boolean isUnlocking;
    //结果相关
    private OnUnLockListener listener;
    private OnInputPwdListener pwdListener;

    public LockView(Context context) {
        super(context);
    }

    public LockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        rectList = new ArrayList<>();
        pathList = new ArrayList<>();
        //获取自定义属性
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.LockView, defStyleAttr, 0);

        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.LockView_normalColor:
                    normalColor = array.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.LockView_selectColor:
                    selectColor = array.getColor(attr, Color.YELLOW);
                    break;
                case R.styleable.LockView_correctColor:
                    correctColor = array.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.LockView_wrongColor:
                    wrongColor = array.getColor(attr, Color.RED);
                    break;
                case R.styleable.LockView_lineWidth:
                    lineWidth = (int) array.getDimension(attr, TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 5,
                            context.getResources().getDisplayMetrics()
                    ));
                    break;
            }
        }
        if (lineWidth == -1) {
            lineWidth = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 5,
                    context.getResources().getDisplayMetrics());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //重置
        rectList.clear();
        pathList.clear();
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //不能在构造方法中构造，因为只有onMeasure后才可以获取宽高
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        circlePaint = new Paint();
        //抗锯齿
        circlePaint.setAntiAlias(true);
        //防抖动
        circlePaint.setDither(true);
        path = new Path();
        tempPath = new Path();
        pathPaint = new Paint();
        pathPaint.setDither(true);
        pathPaint.setAntiAlias(true);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeWidth(lineWidth);

        //初始化一些宽高属性
        int horizontalSpacing;
        int verticalSpacing;
        if (width <= height) {
            horizontalSpacing = 0;
            verticalSpacing = (height - width) / 2;
            rectRadius = width / 14;
        } else {
            horizontalSpacing = (width - height) / 2;
            verticalSpacing = 0;
            rectRadius = height / 14;
        }
        //初始化所有CircleRect对象
        for (int i = 1; i <= 9; i++) {
            int x = ((i - 1) % 3 * 2 + 1) * rectRadius * 2 + horizontalSpacing +
                    getPaddingLeft() + rectRadius;
            int y = ((i - 1) / 3 * 2 + 1) * rectRadius * 2 + verticalSpacing +
                    getPaddingTop() + rectRadius;
            CircleRect rect = new CircleRect(i, x, y, STATE_NORMAL);
            rectList.add(rect);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);
        for (int i = 0; i < rectList.size(); i++) {
            drawCircle(rectList.get(i), rectList.get(i).getState());
        }
        canvas.drawPath(path, pathPaint);
    }


    //根据状态改变路径颜色
    private void setWholePathState(int state) {
        pathPaint.setColor(getColorByState(state));
        for (CircleRect rect : pathList) {
            rect.setState(state);
        }
    }

    private int getColorByState(int state) {
        int color = normalColor;
        switch (state) {
            case STATE_NORMAL:
                color = normalColor;
                break;
            case STATE_SELECT:
                color = selectColor;
                break;
            case STATE_CORRECT:
                color = correctColor;
                break;
            case STATE_WRONG:
                color = wrongColor;
                break;
        }
        return color;
    }

    //画圆
    private void drawCircle(CircleRect rect, int state) {
        circlePaint.setColor(getColorByState(state));
        canvas.drawCircle(rect.getX(), rect.getY(), rectRadius, circlePaint);
    }

    //判断点是否在圆内
    private CircleRect getOuterRect(int x, int y) {
        for (int i = 0; i < rectList.size(); i++) {
            CircleRect rect = rectList.get(i);
            if (((x - rect.getX()) * (x - rect.getX()) + (y - rect.getY()) * (y - rect.getY())) <=
                    rectRadius * rectRadius) {
                if (rect.getState() != STATE_SELECT) {
                    return rect;
                }
            }
        }
        return null;
    }

    public String getResult() {
        StringBuilder result = new StringBuilder();
        for (CircleRect aRectList : pathList) {
            result.append(aRectList.getCode());
        }
        Log.d("test", "" + result);
        return result.toString();
    }

    //密码监听接口
    public void setOnInputPwdListener(OnInputPwdListener pwdListener) {
        this.pwdListener = pwdListener;
    }

    //结果监听剪口
    public void setOnUnlockListener(OnUnLockListener listener) {
        this.listener = listener;
    }

    //重置
    public void reset() {
        setWholePathState(STATE_NORMAL);
        pathPaint.setColor(selectColor);
        path.reset();
        tempPath.reset();
        pathList = new ArrayList<>();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int currX = (int) event.getX();
        int currY = (int) event.getY();
        CircleRect rect = getOuterRect(currX, currY);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //保证手指按下后所有元素都是初始状态
                this.reset();
                //判断手指落点是否在某个圆圈中，如果是则设置该圆圈为选中状态
                if (rect != null) {
                    rect.setState(STATE_SELECT);
                    startX = rect.getX();
                    startY = rect.getY();
                    tempPath.moveTo(startX, startY);
                    pathList.add(rect);
                    isUnlocking = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isUnlocking) {
                    path.reset();
                    path.addPath(tempPath);
                    path.moveTo(startX, startY);
                    path.lineTo(currX, currY);
                    if (rect != null) {
                        rect.setState(STATE_SELECT);
                        startX = rect.getX();
                        startY = rect.getY();
                        tempPath.lineTo(startX, startY);
                        pathList.add(rect);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                performClick();//强迫症为了消除performClick警告
                isUnlocking = false;
                if (pathList.size() > 0) {
                    path.reset();
                    path.addPath(tempPath);
                    String result = getResult();
                    if (pwdListener != null) {
                        pwdListener.onInputPwd(result);
                        setWholePathState(STATE_CORRECT);
                    }
                    if (listener != null) {
                        if (listener.isUnlockSuccess(result)) {
                            listener.onSuccess();
                            setWholePathState(STATE_CORRECT);
                        } else {
                            listener.onFailure();
                            setWholePathState(STATE_WRONG);
                        }
                    }
                    break;
                }
        }
        invalidate();
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void clear() {
        setWholePathState(STATE_NORMAL);
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    //解锁接口
    public interface OnUnLockListener {
        boolean isUnlockSuccess(String result);

        void onSuccess();

        void onFailure();
    }

    //记录密码的接口
    public interface OnInputPwdListener {
        void onInputPwd(String pwd);
    }
}
