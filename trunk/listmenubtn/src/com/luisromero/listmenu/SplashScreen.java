package com.luisromero.listmenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashScreen extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);
		
		Handler x = new Handler();
		x.postDelayed(new SplashHandler(),2000);
	}
	
	class SplashHandler implements Runnable{
		public void run(){
			startActivity(new Intent(getApplication(), MainActivity.class));
			SplashScreen.this.finish();
		}
	}
}
