package gravityshock

import com.badlogic.gdx.physics.box2d

class Spaceship extends Actor {
  override def create() = {
    var bdef = new box2d.BodyDef()
    Glue.setType(bdef, box2d.BodyDef.BodyType.DynamicBody)
    
    body = world.b2world.createBody(bdef)
    shape = new box2d.CircleShape()
    shape.setRadius(10)
    fixture = body.createFixture(shape, 1)
  }
}
