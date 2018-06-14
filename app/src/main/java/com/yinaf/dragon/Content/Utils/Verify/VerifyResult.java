package com.yinaf.dragon.Content.Utils.Verify;

/**
 * Created by chen on 2016/7/26.
 */
public class VerifyResult {

    private boolean result;
    private String errorMsg;

    public VerifyResult(){
        result = false;
        errorMsg = "";
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
