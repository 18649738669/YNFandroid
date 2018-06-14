package com.yinaf.dragon.Content.Utils;

import android.os.Handler;
import android.widget.ImageView;


import com.yinaf.dragon.Content.Adapter.ChatListAdapter;
import com.yinaf.dragon.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by long on 18/5/10.
 */

public class VoicePlayingUtil {

    private Handler handler;

    private ImageView imageView;

    private ImageView lastImageView;

    private static VoicePlayingUtil voicePlayingUtil;

    private Timer timer = new Timer();
    private TimerTask timerTask;
    private TimerTask stopTask;

    private int i;

    private int modelType;

    private double length;

    private boolean isStart;

    private int[] reciverVoiceBg = new int[]{R.drawable.receiver_four_img, R.drawable.receiver_three_img, R.drawable.receiver_two_img, R.drawable.receiver_one_img};

    private int[] sendVoiceBg = new int[]{R.drawable.send_four_img, R.drawable.send_three_img, R.drawable.send_two_img, R.drawable.send_one_img};

    public VoicePlayingUtil(Handler handler) {
        super();
        this.handler = handler;
    }

    public static VoicePlayingUtil voicePlayInstance(Handler handler) {
        if (voicePlayingUtil == null) {
            voicePlayingUtil = new VoicePlayingUtil(handler);
        }
        return voicePlayingUtil;
    }

    public void voicePlay() {
        if (imageView == null) {
            return;
        }
        if (isStart) {
            stopPlay();
            return;
        }
        i = 0;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (imageView != null) {
                    if (i > 3)
                        i = 0;
                    if (modelType == ChatListAdapter.USER_CLICK) {
                        changeBg(sendVoiceBg[i], false);
                    } else {
                        changeBg(reciverVoiceBg[i], false);
                    }
                } else {
                    return;
                }
                i++;
            }
        };

        stopTask = new TimerTask() {
            @Override
            public void run() {
                stopPlay();
            }
        };
        isStart = true;
        timer.schedule(timerTask, 0, 300);

        timer.schedule(stopTask, (long) (length * 1000), (long) (length * 1000));
    }

    public void stopPlay(){
        lastImageView = imageView;
        if (lastImageView != null) {
            if (modelType == ChatListAdapter.WATCH_CLICK)
                changeBg(R.drawable.receiver_one_img, true);
            else
                changeBg(R.drawable.send_one_img, true);

            if (timerTask != null) {
                timerTask.cancel();
            }
            if (stopTask != null) {
                stopTask.cancel();
            }
        }
        isStart = false;
    }

    private void changeBg(final int id, final boolean isStop) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isStop) {
                    lastImageView.setImageResource(id);
                } else {
                    imageView.setImageResource(id);
                }
            }
        });
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setModelType(int modelType) {
        this.modelType = modelType;
    }

    public void setLength(double length) {
        this.length = length;
    }


}
