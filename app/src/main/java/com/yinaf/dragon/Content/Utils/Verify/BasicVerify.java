package com.yinaf.dragon.Content.Utils.Verify;

/**
 * Created by chen on 2016/7/26.
 */
public class BasicVerify {
    private VerifyResult verifyResult = new VerifyResult();

    public VerifyResult success(){
        verifyResult.setResult(true);
        return verifyResult;
    }

    public VerifyResult error(String errorMsg){
        verifyResult.setResult(false);
        verifyResult.setErrorMsg(errorMsg);
        return verifyResult;
    }
}

