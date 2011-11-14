package cody.gravityshock

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.OrthographicCamera;

object Main extends ApplicationListener{

  	var spriteBatch: SpriteBatch = _
	var texture: Texture = _
	var font: BitmapFont = _
	//var textPosition: Vector2 = new Vector2(100, 100)
	//var textDirection: Vector2 = new Vector2(1, 1)
        
        var world: World = _
  
      var cam: OrthographicCamera = _

      var player: Spaceship = _
      
	override def create () {
  	  return
		font = new BitmapFont()
		font.setColor(Color.RED)
		//texture = new Texture(Gdx.files.internal("badlogic.jpg"))
		spriteBatch = new SpriteBatch()
                
    world = new World()
    var map = new Map()
    map.load(world, "level1.svg")
    player = new Spaceship()
    world.add(player)
    player.create()
    
    //var musicfile = Gdx.files.internal("data/2ND_PM.ogg")
    //var music = Gdx.audio.newMusic(musicfile)
    //music.play
    
    cam = new OrthographicCamera(640, 480)
	}
        
        override def render () {
  	  return
		//var centerX = Gdx.graphics.getWidth() / 2;
		//var centerY = Gdx.graphics.getHeight() / 2;

		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
    cam.update()
    world.render(cam)

    var t = Gdx.graphics.getDeltaTime()
    world.tick(t)
    
    var v = player.body.getPosition()
    cam.position.x = v.x
    cam.position.y = v.y
    
//		spriteBatch.begin();
//		spriteBatch.setColor(Color.WHITE);
//		spriteBatch.draw(texture, centerX - texture.getWidth() / 2, centerY - texture.getHeight() / 2, 0, 0, texture.getWidth(),
//			texture.getHeight());
//		font.draw(spriteBatch, "Hello World!", textPosition.x, textPosition.y);
//		spriteBatch.end();
	}
        
  	override def resize (width: Int, height: Int) {
  	  return
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}

	override def pause () {

	}

	override def resume () {

	}

	override def dispose () {

	}


}
