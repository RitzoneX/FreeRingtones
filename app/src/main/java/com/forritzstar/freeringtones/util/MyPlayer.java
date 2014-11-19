package com.forritzstar.freeringtones.util;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * 自定义播放器
 * 
 * @author Administrator
 * 
 */
public class MyPlayer {

	static MediaPlayer mediaPlayer = new MediaPlayer();
	static String previousPath;

	public static void playOrPause(String path) {
		// 判断是否上一个铃声
		if (path.equals(previousPath))
			same();
		else {
			play(path);
			previousPath = path;
		}
	}

	/**
	 * 相同铃声操作
	 */
	static void same() {
		if (mediaPlayer.isPlaying())
			mediaPlayer.pause();
		else {
			// mediaPlayer.seekTo(0);
			mediaPlayer.start();
		}
	}

	public static void play(String path) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare(); // might take long! (for buffering, etc)
			mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 停止铃声
	 */
	public static void stop() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			previousPath = null;
		}
	}

	/**
	 * 回收资源
	 */
	static void release() {
		mediaPlayer.release();
		mediaPlayer = null;
	}

}
