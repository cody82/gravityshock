package cody.gravityshock.client;

import cody.gravityshock.*;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GravityShockGwt extends GwtApplication {
  @Override
    public GwtApplicationConfiguration getConfig () {
      GwtApplicationConfiguration config = new GwtApplicationConfiguration(800, 600);
      return config;
    }

  @Override
    public ApplicationListener getApplicationListener () {
	  
	  String value = com.google.gwt.user.client.Window.Location.getParameter("audio");
	  if(value != null){
		  Assets.audio = value.compareTo("false") != 0;
	  }
	  else {
		  Assets.audio = false;
	  }
	  
	  MainGame game = new MainGame();
	  
	  value = com.google.gwt.user.client.Window.Location.getParameter("level");
	  if(value != null){
		  game.level = Integer.parseInt(value);
	  }
	  
	  value = com.google.gwt.user.client.Window.Location.getParameter("bloom");
	  if(value != null){
		  game.enable_bloom = value.compareTo("false") != 0;
	  }
	  
      return game;
    }
}
