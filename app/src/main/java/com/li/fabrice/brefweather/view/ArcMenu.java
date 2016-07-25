package com.li.fabrice.brefweather.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.li.fabrice.brefweather.R;

/**
 * Created by Fabrice on 02/02/2016.
 */
public class ArcMenu extends ViewGroup implements View.OnClickListener {

    private static final int POS_LEFT_TOP = 0,POS_LEFT_BOTTOM = 1,POS_RIGHT_TOP = 2,POS_RIGHT_BOTTOM = 3;
    private Position mPosition = Position.RIGHT_BOTTOM;
    private int mRadius;
    private Status mCurrentMenuStatus = Status.CLOSE;//菜单初始状态为关闭
    private View mCButton;//主按钮
    private OnMenuItemClickListener onMenuItemClickListener;

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    @Override
    public void onClick(View v) {

        rotateMenuButton(v,0f,360f,300);
        toggleMenu(300);


    }
//切换菜单
    public void toggleMenu(int dur) {
        //为menuitem添加平移动画旋转动画
        int count = getChildCount();
        for(int i=0;i<count-1;i++){
            final View childView = getChildAt(i+1);
            childView.setVisibility(VISIBLE);
            //end 0,0
            //start
            int cl = (int) (mRadius*Math.sin(Math.PI/2/(count-2)*i));
            int ct = (int) (mRadius*Math.cos(Math.PI / 2 / (count - 2) * i));
            int xflag = 1;
            int yflag = 1;
            if(mPosition == Position.LEFT_BOTTOM||mPosition == Position.LEFT_TOP){
                xflag = -1;
            }
            if(mPosition == Position.RIGHT_BOTTOM||mPosition == Position.RIGHT_TOP){
                yflag = -1;
            }
            AnimationSet animationSet = new AnimationSet(true);
            Animation tranAnim = null;
            if(mCurrentMenuStatus == Status.CLOSE){
                tranAnim = new TranslateAnimation(xflag*cl,0,yflag*ct,0);
                childView.setClickable(true);
                childView.setFocusable(true);

            }else{
                tranAnim = new TranslateAnimation(0,xflag*cl,0,yflag*ct);
                childView.setClickable(false);
                childView.setFocusable(false);
            }
            tranAnim.setFillAfter(true);
            tranAnim.setDuration(dur);
            tranAnim.setStartOffset((i*100)/count);
            tranAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if(mCurrentMenuStatus == Status.CLOSE){
                        childView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            //旋转动画
            RotateAnimation rotateAnimation = new RotateAnimation(0,720,
                    Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,
                    0.5f);
            rotateAnimation.setDuration(dur);
            rotateAnimation.setFillAfter(true);
            animationSet.addAnimation(rotateAnimation);
            animationSet.addAnimation(tranAnim);


            childView.startAnimation(animationSet);
            final int pos = i + 1;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onMenuItemClickListener !=null)
                        onMenuItemClickListener.onClick(childView,pos);
                        //Log.e("onMenuItemClickListener:", "onMenuItemClickListener");
                        menuItemAnim(pos - 1);
                        changeStatus();

                }
            });

        }
        //切换菜单状态
        changeStatus();
    }

    //添加item的点击动画
    private void menuItemAnim(int pos) {

        for(int i =0;i<getChildCount()-1;i++){
            //Log.e("menuItemAnim:",pos+"pos");
            View childView = getChildAt(i+1);

            if(i==pos){
                childView.startAnimation(scaleBigAnim(300));
            }else{
                childView.startAnimation(scaleSmallAnim(300));
            }
            childView.setClickable(false);
            childView.setFocusable(false);
        }
    }
    //
    private Animation scaleSmallAnim(int duration) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,0.0f,1.0f,0.0f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f,0.0f);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        return animationSet;
    }
    //item变大透明度降低动画
    private Animation scaleBigAnim(int duration) {
        //Log.e("duration:",duration+"ss");
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,4.0f,1.0f,4.0f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f,0.0f);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        return animationSet;
    }

    private void changeStatus() {
        mCurrentMenuStatus = (mCurrentMenuStatus==Status.CLOSE?Status.OPEN:Status.CLOSE);
    }
     public boolean isOpen(){
         return mCurrentMenuStatus == Status.OPEN;
     }

    //菜单点击后旋转
    private void rotateMenuButton(View v, float start, float end, int duration) {
        RotateAnimation anim = new RotateAnimation(start,end,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,
                0.5f);
        anim.setDuration(duration);
        anim.setFillAfter(true);
        v.startAnimation(anim);
    }

    public enum Status{
        OPEN,CLOSE
    }

    //点击子菜单项的回调接口
    public interface OnMenuItemClickListener{
        void onClick(View view,int pos);
    }

    /*
    * 菜单位置枚举
    * */
    public  enum Position{
        LEFT_TOP,LEFT_BOTTOM,RIGHT_TOP,RIGHT_BOTTOM
    }

    public ArcMenu(Context context) {
        this(context,null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //半径默认值
        mRadius = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,getResources().getDisplayMetrics());
        //获取自定义属性值
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenu,defStyleAttr,0);
        int pos = typedArray.getInt(R.styleable.ArcMenu_position,POS_RIGHT_BOTTOM);
        switch (pos){
            case POS_LEFT_TOP:
                mPosition = Position.LEFT_TOP;
                break;
            case POS_LEFT_BOTTOM:
                mPosition = Position.LEFT_BOTTOM;
                break;
            case POS_RIGHT_TOP:
                mPosition = Position.RIGHT_TOP;
                break;
            case POS_RIGHT_BOTTOM:
                mPosition = Position.RIGHT_BOTTOM;
                break;
        }

        mRadius = (int) typedArray.getDimension(R.styleable.ArcMenu_radius,
                                                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,
                                                getResources().getDisplayMetrics()));

        Log.e("position, radius",mPosition+" ; "+mRadius+"");
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for(int i=0;i<count;i++){
            //测量child
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);


        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed){

            layoutMenuButton();
            int count = getChildCount();
            for(int i=0;i <count-1;i++){
                View child = getChildAt(i+1);
                child.setVisibility(View.GONE);
                int cl = (int) (mRadius*Math.sin(Math.PI/2/(count-2)*i));
                int ct = (int) (mRadius*Math.cos(Math.PI/2/(count-2)*i));
                int cw = child.getMeasuredWidth();
                int ch = child.getMeasuredHeight();
                //如果菜单在左下，右下
                if(mPosition == Position.LEFT_BOTTOM||mPosition == Position.RIGHT_BOTTOM){
                    ct = getMeasuredHeight()-ch-ct;

                }

                //如果菜单在右上，左上
                if(mPosition == Position.LEFT_TOP||mPosition == Position.RIGHT_TOP){
                    cl = getMeasuredWidth()-cw-cl;

                }
                child.layout(cl,ct,cl+cw,ct+ch);
            }
        }

    }

    private void layoutMenuButton(){
        mCButton = getChildAt(0);
        mCButton.setOnClickListener(this);

        int l=0;
        int t=0;
        int width =mCButton.getMeasuredWidth();
        int hight = mCButton.getMeasuredHeight();

        switch (mPosition){
            case LEFT_TOP:
                l=0;
                t=0;
                break;
            case LEFT_BOTTOM:
                l=0;
                t=getMeasuredHeight()-hight;
                break;
            case RIGHT_TOP:
                l=getMeasuredWidth()-width;
                t=0;
                break;
            case RIGHT_BOTTOM:
                l = getMeasuredWidth()-width;
                t = getMeasuredHeight()-hight;
                break;
        }

        mCButton.layout(l,t,l+width,t+hight);
    }

}
