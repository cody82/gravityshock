package gravityshock

import com.badlogic.gdx.physics.box2d
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2

class Spaceship extends Actor {
  override def create() = {
    var bdef = new box2d.BodyDef()
    Glue.setType(bdef, box2d.BodyDef.BodyType.DynamicBody)
    
    body = world.b2world.createBody(bdef)
    //shape = new box2d.CircleShape()
    //shape.setRadius(10)
    var s = new box2d.PolygonShape()
    shape = s
    s.set(Array(new Vector2(0, 5), new Vector2(-10, -5), new Vector2(10, -5)))
    
    fixture = body.createFixture(shape, 1)
  }
  
  override def render() = {
        var cam = new OrthographicCamera(640, 480)
    cam.update()
    var sr = new ShapeRenderer()
    sr.setProjectionMatrix(cam.combined)
    
    sr.begin(ShapeType.Line)
    
    var pos = body.getPosition()
    var rad = body.getAngle()
   
    sr.translate(pos.x, pos.y, 0)
    sr.rotate(0, 0, 1, rad*180f/math.Pi.toFloat)
    
    var array = Array(new Vector2(0, 5), new Vector2(10, -5), new Vector2(-10, -5))
    sr.setColor(1, 1, 0, 1)
    for(i <- 0 until array.length - 1) {
      sr.line(array(i).x, array(i).y, array(i+1).x, array(i+1).y)
    }
    sr.line(array.last.x, array.last.y, array(0).x, array(0).y)
    sr.end()

  }
}
