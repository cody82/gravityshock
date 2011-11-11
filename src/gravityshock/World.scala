package gravityshock

import com.badlogic.gdx.physics.box2d
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera;

class World() {
  var b2world: box2d.World = new box2d.World(new Vector2(0, -9.81f), true)
  var actors: Array[Actor] = new Array[Actor](0)
  var map: Map = _
  
  def add(actor: Actor) = {
    actors = actors :+ actor
    actor.world = this
  }
  
  def dispose = {
    b2world.dispose()
  }
  
  def tick(dtime: Float) = {
    b2world.step(dtime, 1, 1)
    for(a <- actors) {
      a.tick(dtime)
    }
  }
  
  def render(cam: OrthographicCamera) = {
    map.render(cam)
    for(a <- actors) {
      a.render(cam)
    }
  }
}
