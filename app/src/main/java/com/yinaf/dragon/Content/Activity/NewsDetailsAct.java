package com.yinaf.dragon.Content.Activity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.yinaf.dragon.Content.Activity.family_set.model.AddressBookSetModel;
import com.yinaf.dragon.Content.Bean.NewsDetailsModel;
import com.yinaf.dragon.Content.Net.NewsDetailsAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.TipUtils;
import com.zzhoujay.richtext.RichText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 资讯详情页
 */
public class NewsDetailsAct extends BasicAct implements NewsDetailsAPI.NewsDetailsListener {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;

    public NewsDetailsAct() {
        super(R.layout.anews_details_act, R.string.news_details_set, 0
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);

    }

    @Override
    public void initView() {

        String newId = getIntent().getStringExtra("id");

        new NewsDetailsAPI(this, newId);
    }

    @Override
    public void newsDetailsSuccess(NewsDetailsModel content) {
        NewsDetailsModel.ObjBean obj = content.getObj();
        tvTitle.setText(obj.getNew_title());
        tvTime.setText(obj.getUpdate_time());

        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        RichText.fromHtml(obj.getNew_content()).into(tvContent);


    }

    @Override
    public void newsDetailsError(long code, String msg) {
        TipUtils.showTip(msg);
    }


}
