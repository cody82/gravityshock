package cody.gravityshock;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class MainAndroid extends AndroidApplication {
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize(new Main(), false);
	}
}