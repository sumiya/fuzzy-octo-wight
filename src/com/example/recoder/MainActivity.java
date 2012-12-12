package com.example.recoder;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String AUDIO_FILE_NAME = "Sound.3gp";

	private Button			mRecodeButton;
	private Button			mPlayButton;
	private MediaRecorder	mRecoder;
	private MediaPlayer		mPlayer;
	
	private File			mSound;
	
	/* pull comment */
	
	/* Recode Button OnClickListener */
	public class RBOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			/* 再生ボタン */
			if( mSound.exists() ){
				mPlayButton.setEnabled( true );
			} else {
				mPlayButton.setEnabled( false );
			}

			/* 録音ボタン */
			if( mRecodeButton.getText().equals( getString(R.string.START_RECODE ) ) ){
				/* 録音開始 */
				mRecodeButton.setText( R.string.STOP_RECODE );

				/* MediaRecoder */
				mRecoder.setAudioSource( MediaRecorder.AudioSource.MIC );
				mRecoder.setOutputFormat( MediaRecorder.OutputFormat.THREE_GPP );
				mRecoder.setAudioEncoder( MediaRecorder.AudioEncoder.AMR_NB );
				mRecoder.setOutputFile( mSound.getAbsolutePath() );

				try {
					mRecoder.prepare();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				mRecoder.start();
				
				
			} else {
				/* 録音停止 */
				mRecodeButton.setText( R.string.START_RECODE );
				
				mRecoder.stop();
			}
		}
	}

	/* Play Button OnClickListener */
	public class PBOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			
			if( mPlayButton.getText().equals( getString( R.string.PLAY ) ) ){
				Toast.makeText(getApplicationContext(),"play",Toast.LENGTH_LONG).show();

				/* 再生 */
				mPlayButton.setText( R.string.STOP );

				try {
					mPlayer.setDataSource( mSound.getAbsolutePath() );
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					mPlayer.prepare();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				mPlayer.start();
			
			} else {

				/* 停止 */
				mPlayButton.setText( R.string.PLAY );
				mPlayer.stop();

			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSound = new File( getCacheDir(), AUDIO_FILE_NAME );
		
		mRecodeButton = (Button)findViewById(R.id.RECODE);
		mPlayButton= (Button)findViewById(R.id.PLAY);
		
		mRecodeButton.setOnClickListener(new RBOnClickListener());
		mPlayButton.setOnClickListener( new PBOnClickListener());

		

		if( mSound.exists() ){
			/* Enable Button */
			mPlayButton.setEnabled(true);
		} else {
			mPlayButton.setEnabled(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		/* 復帰時 */
		super.onResume();
		
		mPlayer = new MediaPlayer();
		mRecoder = new MediaRecorder();

		mPlayer.setOnCompletionListener( new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mPlayButton.setText(R.string.PLAY);
			}
		});
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();

		mPlayer.release();
		mRecoder.release();
	}

}
