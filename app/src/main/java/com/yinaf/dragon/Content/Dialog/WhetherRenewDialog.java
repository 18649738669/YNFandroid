package com.yinaf.dragon.Content.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yinaf.dragon.R;

import butterknife.BindView;

/**
 * Created by long on 2018/05/07.
 * 功能：是否续费的选择弹窗
 */

public class WhetherRenewDialog extends Dialog {


    TextView dialogWhetherRenewTvTitle;
    Button dialogWhetherRenewBtnConfirm;
    Button dialogWhetherRenewBtnCancel;

    public WhetherRenewDialog(Context context) {
        super(context);
        initView();
    }

    public WhetherRenewDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public WhetherRenewDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_whether_renew);
        dialogWhetherRenewBtnCancel = findViewById(R.id.dialog_whether_renew_btn_cancel);
        dialogWhetherRenewBtnConfirm = findViewById(R.id.dialog_whether_renew_btn_confirm);
    }

    /**
     * 确定按钮的点击回调
     *
     * @param confirmListener
     */
    public void setListenerConfirm(View.OnClickListener confirmListener) {
        dialogWhetherRenewBtnConfirm.setOnClickListener(confirmListener);
    }

    /**
     * 取消按钮的点击回调
     *
     * @param cancelListener
     */
    public void setListenerCancel(View.OnClickListener cancelListener) {
        dialogWhetherRenewBtnCancel.setOnClickListener(cancelListener);
    }
}
