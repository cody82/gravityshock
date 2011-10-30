package gravityshock

import com.badlogic.gdx.physics.box2d
import com.badlogic.gdx.math.Vector2

class World() {
  var b2world: box2d.World = new box2d.World(new Vector2(0, 9.81f), true)
  var actors: Array[Actor] = new Array[Actor](0)
  var map: Map = _
  
  def add(actor: Actor) = {
    actors = actors :+ actor
  }
  
  def dispose = {
    b2world.dispose()
  }
  
  def tick(dtime: Float) = {
    b2world.step(dtime, 1, 1)
  }
  
  def render() = {
    map.render()
  }
}
