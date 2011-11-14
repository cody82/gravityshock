package cody.gravityshock

import com.badlogic.gdx.backends.android.AndroidApplication

class MainAndroid extends AndroidApplication {
        override def onCreate (savedInstanceState: android.os.Bundle) : Unit = {
                super.onCreate(savedInstanceState);
                initialize(Main, false);
        }
}