package com.yinaf.dragon.Content.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yinaf.dragon.Content.Activity.MedicalReportAct;
import com.yinaf.dragon.Content.Activity.MedicalReportListAct;
import com.yinaf.dragon.Content.Bean.ThreeTextData;
import com.yinaf.dragon.Content.Bean.TwoTextData;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAdapter;
import com.yinaf.dragon.Tool.Activity.BasicViewHolder;

import java.util.List;

/**
 * Created by long on 2018-4-14.
 * 功能：体检报告列表的适配器
 */

public class MedicalReportListAdapter extends BasicAdapter{

    private List<TwoTextData> date;
    Context context;

    public MedicalReportListAdapter(Context context, List<TwoTextData> data) {
        super(data, R.layout.item_medical_report );
        this.date = data;
        this.context = context;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void bind(BasicViewHolder viewHolder, int position) {

        final TwoTextData twoTextData = date.get(position);
        TextView time = viewHolder.getTextView(R.id.medical_report_item_tv_time);
        TextView number = viewHolder.getTextView(R.id.medical_report_item_tv_number);
        LinearLayout linearLayout = viewHolder.getLinearLayout(R.id.medical_report_item_ll);
        time.setText(twoTextData.getTime());
        number.setText(twoTextData.getText());
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MedicalReportAct.startActivity(context,twoTextData.getType());

            }
        });

    }
}
