package com.yinaf.dragon.Content.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.yinaf.dragon.R;

/**
 * Created by long on 2018/04/26.
 * 功能：弹出框，显示支付的结果
 */
public class PayStateDialog extends Dialog {


    TextView title;
    Button config;
    String text;


    public PayStateDialog(Context context,String text) {
        super(context);
        this.text = text;
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_pay_state);
        title = findViewById(R.id.dialog_pay_state_tv_title);
        config = findViewById(R.id.dialog_pay_state_btn_confirm);
        title.setText(text);

    }


    /**
     * 确认按钮的点击回调
     * @param listener
     */
    public void  setListener(View.OnClickListener listener){
        config.setOnClickListener(listener);
    }

}
