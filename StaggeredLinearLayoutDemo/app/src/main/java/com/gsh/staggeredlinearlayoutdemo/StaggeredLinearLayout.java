package com.gsh.staggeredlinearlayoutdemo;//package com.example.aandroidnotebook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by damon on 3/1/16.
 * 根据子view的长度能自动换行的view
 */
public class StaggeredLinearLayout extends ViewGroup {

    public static final String TAG = "StaggeredLinearLayout";
    /**
     * TextView间的水平间隔
     */
    private int horizontalSpace;

    /**
     * TextView间的垂直间隔
     */
    private int verticalSpace;

//    public static final int ID_FIRST = View.generateViewId();
//    public static final int ID_SECOND = View.generateViewId();

    /**
     * textView的padding值
     * 0: left
     * 1: top
     * 2: right
     * 3: bottom
     */
    private int[] textPadding = new int[4];


    /**
     * TextView 的颜色
     */
    private int textColor;

    /**
     * TextView 的size
     */
    private int textSize;


    /**
     * TextView 的高度
     */
    private int textHeight;


    /**
     * 不解释
     *
     * @param context context
     */
    public StaggeredLinearLayout(Context context) {
        super(context);
        init();
    }

    /**
     * 不解释
     *
     * @param context context
     * @param attrs   attrs
     */
    public StaggeredLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 不解释
     *
     * @param context      context
     * @param attrs        attrs
     * @param defStyleAttr defStyleAttr
     */
    public StaggeredLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        //设置一些默认参数
        float horizontalSpace = getResources().getDimension(R.dimen.pregnancy_wall_text_horizontal_space);
        float verticalSpace = getResources().getDimension(R.dimen.pregnancy_wall_text_vertical_space);
        float textSize = getResources().getDimension(R.dimen.pregnancy_wall_text_size);
        float textPaddingLeftAndRight = getResources().getDimension(R.dimen.pregnancy_wall_text_padding_left_or_right);
        int textColor = getResources().getColor(android.R.color.holo_blue_bright);
        float textHeight = getResources().getDimension(R.dimen.pregnancy_wall_text_height);
        this.horizontalSpace = (int) horizontalSpace;
        this.verticalSpace = (int) verticalSpace;
        this.textSize = (int) textSize;
        setTextPadding((int) textPaddingLeftAndRight, 0, (int) textPaddingLeftAndRight, 0);
        this.textColor = textColor;
        this.textHeight = (int) textHeight;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int lineCount = 0;//行数
        boolean isLineFirst = true;//该child是否是某一行的第一个元素
        int countTemp = 0;//当前行的child已占用了多少宽度了

        int childCount = getChildCount();
        for (int i = 0; i < getChildCount(); i++) {
            TextView child = (TextView) getChildAt(i);
            // 这里要改为了隔次测量. 暂不隔次测量.
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            int childMeasuredWidth = child.getMeasuredWidth();
            if (childMeasuredWidth > (widthSize - getPaddingLeft() - getPaddingRight())) {
                Log.e(TAG, "child的宽度超过了parent的宽度,-->>" + child.getText());
                continue;
            }

            int countTemp1 = countTemp + childMeasuredWidth;
            if (!isLineFirst) {
                countTemp1 += horizontalSpace;
            } else {
//                PregnancyMainActivity.logCallback.logPreg(TAG,", lineCount-->>"+lineCount);
                lineCount++;
            }

            int marginLeft;
            int marginTop;

            //第一排
            if (isLineFirst) {
                //第一个元素
                marginLeft = 0;//默认
            } else {
                //非第一个元素
                marginLeft = countTemp + horizontalSpace;
            }
            countTemp = countTemp1;

            int index = (lineCount - 1);//第一个marginTop为0;
            marginTop = index * (verticalSpace + textHeight) + getPaddingTop();
            marginLeft += getPaddingLeft();
            ChildMargin tag = new ChildMargin(marginLeft, marginTop);
            child.setTag(R.id.id_first,tag);

            //判断下一个child是否需要另起一行.
            int nextPosition = i + 1;
            if (nextPosition < childCount) {
                TextView childNext = (TextView) getChildAt(nextPosition);
                measureChild(childNext, widthMeasureSpec, heightMeasureSpec);
                int childMeasuredWidth1 = childNext.getMeasuredWidth();
                int widthCountTemp1 = countTemp + childMeasuredWidth1 + horizontalSpace + getPaddingRight();
//        PregnancyMainActivity.logCallback.logPreg(TAG,", widthCountTemp1-->>"+widthCountTemp1);
                if (widthCountTemp1 > widthSize) {
                    isLineFirst = true;
                    countTemp = 0;//初始值
                } else {
                    isLineFirst = false;
                }
            }
        }
        int paddingVertical = getPaddingTop() + getPaddingBottom();
        int height = lineCount * (verticalSpace + textHeight) - verticalSpace + paddingVertical;
//        PregnancyMainActivity.logCallback.logPreg(TAG, "widthSize-->>" + widthSize + ", height-->>" + height + ", paddingVertical-->>" + paddingVertical + ",childCount-->>" + childCount);
        if (childCount == 0) {
            //没有child时设置height设置为0
            height = 0;
        }
        setMeasuredDimension(widthSize, height);
    }


    /**
     * {@inheritDoc}
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            TextView child = (TextView) getChildAt(i);
            ChildMargin tag = (ChildMargin) child.getTag(R.id.id_first);
            int left = tag.marginLeft;
            int top = tag.marginTop;
            int right = left + child.getMeasuredWidth();
            int bottom = top + textHeight;
            child.layout(left, top, right, bottom);
        }
    }


    /**
     * @param childData 子元素 字符串集合
     */
    public void setChildList(List<ThinkLabelsEntity> childData) {
        if (childData ==null||childData.size()==0) {
            return;
        }
        //0: 不用解释吧
        for (int i = 0; i < childData.size(); i++) {
            ThinkLabelsEntity thinkLabelsEntity = childData.get(i);
            TextView tv = new TextView(getContext());
            tv.setText(thinkLabelsEntity.labels);
            tv.setTextColor(textColor);
            tv.setTextSize(textSize);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(textPadding[0], textPadding[1], textPadding[2], textPadding[3]);
            StateListDrawable drawable = (StateListDrawable) getResources().getDrawable(R.drawable.selector_lablel);
            tv.setBackground(drawable);
            tv.setTag(R.id.id_second, thinkLabelsEntity);
            tv.setOnClickListener(new TextViewClickListener());
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, textHeight);
            addView(tv, params);
        }
    }

    /**
     * TextView的clickListener
     */
    private class TextViewClickListener implements OnClickListener {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            v.setSelected(!v.isSelected());
        }
    }

    /**
     * 获取选中子元素的集合的position集合
     *
     * @return 没有则返回size==0的集合
     */
    public List<ThinkLabelsEntity> getSelectedChildList() {
        List<ThinkLabelsEntity> list = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            TextView child = (TextView) getChildAt(i);
            if (child.isSelected()) {
                list.add((ThinkLabelsEntity) child.getTag(R.id.id_second));
            }
        }
        return list;
    }

    /**
     * 设置TextView的padding值
     *
     * @param l left
     * @param t top
     * @param r right
     * @param b bottom
     */
    public void setTextPadding(int l, int t, int r, int b) {
        //0,1,2,3 不用解释吧...
        textPadding[0] = l;
        textPadding[1] = t;
        textPadding[2] = r;
        textPadding[3] = b;
    }

    /**
     * 设置TextView的颜色
     *
     * @param color color
     */
    public void setTextColor(int color) {
        textColor = color;
    }

    /**
     * 设置TextView的size
     *
     * @param size textSize
     */
    public void setTextSize(int size) {
        textSize = size;
    }

    public void setTextHeight(int height) {
        textHeight = height;
    }


    /**
     * 设置 child的水平的space
     *
     * @param horizontalSpace horizontalSpace
     */
    public void setTextHorizontalSpace(int horizontalSpace) {
        this.horizontalSpace = horizontalSpace;
    }


    /**
     * 设置 child的垂直的space
     *
     * @param verticalSpace verticalSpace
     */
    public void setTextVerticalSpcae(int verticalSpace) {
        this.verticalSpace = verticalSpace;
    }

    /**
     * 子元素的margin数据
     */
    private class ChildMargin {

        public ChildMargin(int marginLeft, int marginTop) {
            this.marginLeft = marginLeft;
            this.marginTop = marginTop;
        }

        /**
         * 距离左边
         */
        public int marginLeft;
        /**
         * 距离顶部
         */
        public int marginTop;
    }
    /**
     * Created by damon on 3/14/16.
     * 感想标签数据(与服务器数据字段一致)
     */
    public static class ThinkLabelsEntity implements Serializable {

        public ThinkLabelsEntity(String labels, int labelsId) {
            this.labels = labels;
            this.labelsId = labelsId;
        }

        public ThinkLabelsEntity() {
        }

        /**
         * 该标签的Id
         */
        public int labelsId;

        /**
         * 该标签的内容
         */
        public String labels;


        @Override
        public String toString() {
            return "ThinkLabelsEntity{" +
                    "labelsId=" + labelsId +
                    ", labels='" + labels + '\'' +
                    '}';
        }
    }
}
