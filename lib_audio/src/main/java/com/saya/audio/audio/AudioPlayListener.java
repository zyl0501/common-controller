package com.saya.audio.audio;

abstract class AudioPlayListener {
  /**
   * 播放出错
   */
  public void onError(Exception e){}

  /**
   * 播放停止
   */
  public abstract void onStop();

  /**
   * 暂停播放
   */
  public void onPause(int progress){}

  /**
   * 开始播放
   */
  public void onStart(){}
}
