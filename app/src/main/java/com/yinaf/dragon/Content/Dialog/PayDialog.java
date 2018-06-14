package com.yinaf.dragon.Content.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yinaf.dragon.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by long on 2018/05/07.
 * 功能：支付方式的选择弹窗
 */

public class PayDialog extends Dialog {
    TextView dialogPayTvTitle;
    Button dialogPayBtnWx;
    Button dialogPayBtnZfb;
    Button dialogPayBtnCancel;

    public PayDialog(Context context) {
        super(context);
        initView();
    }

    public PayDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public PayDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_pay);
        dialogPayBtnCancel = findViewById(R.id.dialog_pay_btn_cancel);
        dialogPayBtnWx = findViewById(R.id.dialog_pay_btn_wx);
        dialogPayBtnZfb = findViewById(R.id.dialog_pay_btn_zfb);
    }

    /**
     * 微信按钮的点击回调
     * @param wxListener
     */
    public void  setListenerWx(View.OnClickListener wxListener){
        dialogPayBtnWx.setOnClickListener(wxListener);
    }
    /**
     * 支付宝按钮的点击回调
     * @param zfbListener
     */
    public void  setListenerZfb(View.OnClickListener zfbListener){
        dialogPayBtnZfb.setOnClickListener(zfbListener);
    }
    /**
     * 取消按钮的点击回调
     * @param cancelListener
     */
    public void  setListenerCancel(View.OnClickListener cancelListener){
        dialogPayBtnCancel.setOnClickListener(cancelListener);
    }
}
