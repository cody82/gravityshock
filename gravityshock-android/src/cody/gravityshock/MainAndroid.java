package cody.gravityshock;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class MainAndroid extends AndroidApplication {
	MainGame maingame;
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize(maingame = new MainGame(new AndroidData(this)), true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.equals(exit)) {
			maingame.beginMainMenu();
		}
		return true;
		
	}
	MenuItem exit;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		exit = menu.add("Main menu");
		
		return true;
	}
}