package com.bete.lamp.tts;

import android.speech.tts.TextToSpeech;

import com.utils.LogUtils;

public class TTSListener implements TextToSpeech.OnInitListener {
    private static final String TAG = "TTSListener";
    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            LogUtils.i(TAG, "onInit: TTS引擎初始化成功");
        } else {
            LogUtils.i(TAG, "onInit: TTS引擎初始化失败");
        }
    }
}