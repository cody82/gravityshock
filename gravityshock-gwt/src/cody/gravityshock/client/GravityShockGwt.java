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
      return new MainGame();
    }
}
