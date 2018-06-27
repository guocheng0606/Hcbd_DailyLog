package com.android.hcbd.dailylog.view;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.android.hcbd.dailylog.MyApplication;
import com.android.hcbd.dailylog.R;
import com.blankj.utilcode.util.SizeUtils;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by guocheng on 2017/12/20.
 */

public class SlideFromBottomPopup extends BasePopupWindow implements View.OnClickListener {
    private View popupView;
    private Context context;

    public SlideFromBottomPopup(Activity context) {
        super(context);
        this.context = context;
        bindEvent();
    }

    @Override
    protected Animation initShowAnimation() {
        return getTranslateAnimation(250 * 2, 0, 300);
    }

    @Override
    public View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_slide_from_bottom, null);
        RelativeLayout rl = (RelativeLayout) popupView.findViewById(R.id.click_to_dismiss);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            setMargins(rl,0, SizeUtils.dp2px(55), 0, 0);
        }else{
            setMargins(rl,0, SizeUtils.dp2px(55)+MyApplication.getInstance().getStatusBarHeight(), 0, 0);
        }
        return popupView;
    }

    private static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.tx_1).setOnClickListener(this);
            popupView.findViewById(R.id.tx_2).setOnClickListener(this);
            popupView.findViewById(R.id.tx_3).setOnClickListener(this);
            popupView.findViewById(R.id.tv_cancel).setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tx_1:
            case R.id.tx_2:
            case R.id.tx_3:
            case R.id.tv_cancel:
                if( clickListener != null ){
                    clickListener.onClick(v);
                }
                break;
        }

    }

    public interface onViewClickListener {
        void onClick(View view);
    }

    private onViewClickListener clickListener = null;

    public void setOnViewClickListener(onViewClickListener listener) {
        this.clickListener = listener;
    }

}
