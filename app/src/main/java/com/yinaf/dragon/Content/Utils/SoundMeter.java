package com.yinaf.dragon.Content.Utils;

import android.media.MediaRecorder;
import android.os.Environment;
import java.io.IOException;


public class SoundMeter {
    static final private double EMA_FILTER = 0.6;

    private MediaRecorder mRecorder;
    private double mEMA = 0.0;
    private boolean isStart;

    public void start(String name) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return;
        }
        recordInit(name);
        mRecorder.start();
        isStart=true;
    }

    public void stop() {
        if (mRecorder != null) {
            try {
                if (isStart) {
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                    isStart=false;
                }
            }catch(RuntimeException e){
                    e.printStackTrace();
                }
        }
    }

    public double getAmplitude() {
        double mR;
        if (mRecorder != null){
            mR= (mRecorder.getMaxAmplitude() / 2700.0);
            if(mR>11)
                return 11;
            return mR;
        }
        else
            return 0;
    }

    public double getAmplitudeEMA() {
        double amp = getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }

    private void recordInit(String fileName) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setAudioChannels(1);
        mRecorder.setOutputFile(fileName);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
