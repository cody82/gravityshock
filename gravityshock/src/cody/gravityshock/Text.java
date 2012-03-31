package cody.gravityshock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class Text extends Actor {
	public String text;
	public Text(World _world, String _text) {
		super(_world);
		text = _text;
	}
	
	@Override
	void create() {
		BodyDef bdef = new BodyDef();
	    bdef.type = BodyDef.BodyType.StaticBody;
	    
	    body = world.b2world.createBody(bdef);
	}

	static SpriteBatch batch = new SpriteBatch();
	
	@Override
	void render(OrthographicCamera cam) {
		BitmapFont font = Assets.getFont();
		Vector2 pos = body.getPosition();
		batch.begin();
		batch.setProjectionMatrix(cam.combined);
		batch.enableBlending();
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		font.draw(batch, text, pos.x, pos.y);
		batch.end();
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
	}
}
