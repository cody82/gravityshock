package gravityshock

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

object Main extends ApplicationListener{

  	var spriteBatch: SpriteBatch = _
	var texture: Texture = _
	var font: BitmapFont = _
	var textPosition: Vector2 = new Vector2(100, 100)
	var textDirection: Vector2 = new Vector2(1, 1)
        
  
  
  
	override def create () {
		font = new BitmapFont()
		font.setColor(Color.RED)
		texture = new Texture(Gdx.files.internal("data/badlogic.jpg"))
		spriteBatch = new SpriteBatch()
	}
        
  override def render () {
		var centerX = Gdx.graphics.getWidth() / 2;
		var centerY = Gdx.graphics.getHeight() / 2;

		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);

		// more fun but confusing :)
		// textPosition.add(textDirection.tmp().mul(Gdx.graphics.getDeltaTime()).mul(60));
		textPosition.x += textDirection.x * Gdx.graphics.getDeltaTime() * 60;
		textPosition.y += textDirection.y * Gdx.graphics.getDeltaTime() * 60;

		if (textPosition.x < 0) {
			textDirection.x = -textDirection.x;
			textPosition.x = 0;
		}
		if (textPosition.x > Gdx.graphics.getWidth()) {
			textDirection.x = -textDirection.x;
			textPosition.x = Gdx.graphics.getWidth();
		}
		if (textPosition.y < 0) {
			textDirection.y = -textDirection.y;
			textPosition.y = 0;
		}
		if (textPosition.y > Gdx.graphics.getHeight()) {
			textDirection.y = -textDirection.y;
			textPosition.y = Gdx.graphics.getHeight();
		}

		spriteBatch.begin();
		spriteBatch.setColor(Color.WHITE);
		spriteBatch.draw(texture, centerX - texture.getWidth() / 2, centerY - texture.getHeight() / 2, 0, 0, texture.getWidth(),
			texture.getHeight());
		font.draw(spriteBatch, "Hello World!", textPosition.x, textPosition.y);
		spriteBatch.end();
	}
        
  	override def resize (width: Int, height: Int) {
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		textPosition.set(0, 0);
	}

	override def pause () {

	}

	override def resume () {

	}

	override def dispose () {

	}


}
