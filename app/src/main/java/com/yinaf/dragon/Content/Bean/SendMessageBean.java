package com.yinaf.dragon.Content.Bean;

import com.google.gson.annotations.Expose;
import com.yinaf.dragon.Tool.Bean.BaseResponse;

/**
 * Created by zhangli on 17/3/3.
 * ‘code'                  : <int> //请求状态
 * 'message_id'            : <str> //消息id
 * 'content'               : <str> //音频链接
 */

public class SendMessageBean extends BaseResponse {
    @Expose
    private String message_id;
    @Expose
    private String content;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
