package gravityshock

import com.badlogic.gdx.physics.box2d

class Actor {
  var body: box2d.Body = _
  var shape: box2d.Shape = _
  var fixture: box2d.Fixture = _
  var world: World = _
  
  def Actor(_world: World) = {
    world = _world
    world.add(this)
    create()
  }

  def create() = {
  }
  
  def render() = {
    
  }
  
  
  def tick(dtime: Float) = {
  }
}
