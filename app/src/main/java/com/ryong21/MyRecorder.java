package com.ryong21;

import com.ryong21.io.PcmRecorder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyRecorder extends Activity implements OnClickListener {

	public static final int STOPPED = 0;
	public static final int RECORDING = 1;

	PcmRecorder recorderInstance = null;

	Button startButton = null;
	Button stopButton = null;
	Button exitButon = null;
	TextView textView = null;
	int status = STOPPED;

	public void onClick(View v) {
		if (v == startButton) {
			startButton.setText("开始录音了！");

			if(recorderInstance == null){
				recorderInstance = new PcmRecorder();
				Thread th = new Thread(recorderInstance);
				th.start();
			}
			recorderInstance.setRecording(true);
		}
		if (v == stopButton) {
			startButton.setText("Start");
			recorderInstance.setRecording(false);
		}
		if (v == exitButon) {
			recorderInstance.setRecording(false);
			System.exit(0);
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		startButton = new Button(this);
		stopButton = new Button(this);
		exitButon = new Button(this);
		textView = new TextView(this);

		startButton.setText("Start");
		stopButton.setText("Stop");
		exitButon.setText("退出");
		textView.setText("android 录音机：\n(1)录制pcm到文件." +
				"\n(2)使用speex编码");

		startButton.setOnClickListener(this);
		stopButton.setOnClickListener(this);
		exitButon.setOnClickListener(this);

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(textView);
		layout.addView(startButton);
		layout.addView(stopButton);
		layout.addView(exitButon);
		this.setContentView(layout);
	}
}