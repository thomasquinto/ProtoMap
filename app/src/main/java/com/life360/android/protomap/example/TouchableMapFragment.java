package com.life360.android.protomap.example;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by thomas on 2/27/17.
 */

public class TouchableMapFragment extends SupportMapFragment {

    private View originalContentView;
    private FrameLayout touchView;
    private View.OnTouchListener onTouchListener;

    public void setOnTouchListener(View.OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    private class TouchableWrapper extends FrameLayout {

        public TouchableWrapper(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (onTouchListener != null) {
                onTouchListener.onTouch(touchView, ev);
            }

            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        originalContentView = super.onCreateView(inflater, parent, savedInstanceState);

        touchView = new TouchableWrapper(getActivity());
        touchView.addView(originalContentView);

        return touchView;
    }

    @Override
    public View getView() {
        return originalContentView;
    }
}
