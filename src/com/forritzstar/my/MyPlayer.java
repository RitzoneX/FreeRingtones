package com.forritzstar.my;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * 自定义播放类
 * 
 * @author Administrator
 * 
 */
public class MyPlayer {
	private static MediaPlayer mediaPlayer = new MediaPlayer();
	private Uri previousUri;
	private Context context;

	public MyPlayer(Context context) {
		this.context = context;
	}

	/**
	 * 播放或暂停铃声
	 * 
	 * @param uri
	 * @param position
	 */
	public void playOrPause(Uri uri) {
		// 判断是否上一个铃声
		if (uri.equals(previousUri))
			same();
		else {
			play(uri);
			previousUri = uri;
		}
	}

	/**
	 * 相同铃声操作
	 */
	private void same() {
		if (mediaPlayer.isPlaying())
			mediaPlayer.pause();
		else {
			mediaPlayer.seekTo(0);
			mediaPlayer.start();
		}
	}

	/**
	 * 播放铃声
	 * 
	 * @param uri
	 */
	private void play(Uri uri) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(context, uri);
			mediaPlayer.prepare();
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
	public void stop() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			previousUri = null;
		}
	}

	/**
	 * 设置MediaPlayer的streamtype
	 * 
	 * @param streamtype
	 */
	public void setAudioStreamType(int streamtype) {
		mediaPlayer.setAudioStreamType(streamtype);
	}
}
