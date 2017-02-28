package com.life360.android.protomap.example;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by thomas on 2/27/17.
 */

public class AppBarLayoutTouchIntercept extends AppBarLayout {

    public AppBarLayoutTouchIntercept(Context context) {
        this(context, null);
    }

    public AppBarLayoutTouchIntercept(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean disregardTouches = false;

    /*
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int x = Math.round(ev.getX());
        int y = Math.round(ev.getY());
        for (int i=0; i < getChildCount(); i++){
            View child = getChildAt(i);
            if(x > child.getLeft() && x < child.getRight() && y > child.getTop() && y < child.getBottom()){
                //touch is within this child
                if(ev.getAction() == MotionEvent.ACTION_DOWN){
                    //touch has ended
                    System.out.println("Child touched down: " + child);
                    if (child instanceof CollapsingToolbarLayout) {
                        System.out.println("Found map");
                        disregardTouches = true;
                        break;
                    } else {
                        disregardTouches = false;
                        break;
                    }
                }
            }
        }

        return super.dispatchTouchEvent(ev);
    }
    */

    @Override
    public boolean onInterceptTouchEvent (MotionEvent ev) {
        int x = Math.round(ev.getX());
        int y = Math.round(ev.getY());
        for (int i=0; i < getChildCount(); i++){
            View child = getChildAt(i);
            if(x > child.getLeft() && x < child.getRight() && y > child.getTop() && y < child.getBottom()){
                //touch is within this child
                if(ev.getAction() == MotionEvent.ACTION_DOWN){
                    //touch has ended
                    System.out.println("Child touched down: " + child);
                    if (child instanceof CollapsingToolbarLayout) {
                        System.out.println("Found map");
                        disregardTouches = true;
                        break;
                    } else {
                        disregardTouches = false;
                        break;
                    }
                }
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    public boolean getDisregardTouches() {
        return disregardTouches;
    }
}
