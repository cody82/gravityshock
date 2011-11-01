package gravityshock

import com.badlogic.gdx.physics.box2d
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.graphics
import com.badlogic.gdx.graphics.glutils.IndexData;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexData;
import com.badlogic.gdx
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.OrthographicCamera

class Map {
  var world: World = _
  var shapes: Array[box2d.ChainShape] = new Array[box2d.ChainShape](0)
  var body: box2d.Body = _
  var fixture: box2d.Fixture = _
  var mesh: graphics.Mesh = _
  var shader: ShaderProgram = _
  
  def load(_world: World, filename: String) = {
    world = _world
    world.map = this
    create(filename)
  }

  def create(filename: String) = {
    var bdef = new box2d.BodyDef()
    Glue.setType(bdef, box2d.BodyDef.BodyType.StaticBody)
    
    body = world.b2world.createBody(bdef)
    
    var shape = new box2d.ChainShape()
    var array = Array(new Vector2(-100, -100), new Vector2(100, -100), new Vector2(100, 100), new Vector2(-100, 100))
    shape.createLoop(array)
    shapes = shapes :+ shape
    fixture = body.createFixture(shape, 1)
    
    var gl = gdx.Gdx.graphics.isGL20Available();
    if(false) {
    
    var a = new graphics.VertexAttribute(graphics.VertexAttributes.Usage.Position, 3, "pos")
    mesh = new graphics.Mesh(true, 4, 4, a)
    
          var vertexShader =  "attribute vec4 a_position;    \n" + 
                                     "void main()                  \n" +
                                     "{                            \n" +
                                     "   gl_Position = a_position;  \n" +
                                     "}                            \n";
      var fragmentShader = "precision mediump float;\n" +
                                        "void main()                                  \n" +
                                        "{                                            \n" +
                                        "  gl_FragColor = vec4 ( 1.0, 0.0, 0.0, 1.0 );\n" +
                                        "}";  
    shader = new ShaderProgram(vertexShader, fragmentShader)
    } 
    
    //var gl1 = Gdx.graphics.getGL10()
    //gl1.
  }
  
  def render() = {
    //mesh.render(shader, graphics.GL20.GL_LINE_STRIP)
    //var gl = Gdx.graphics.getGL10()
    var cam = new OrthographicCamera(640, 480)
    cam.update()
    var sr = new ShapeRenderer()
    sr.setProjectionMatrix(cam.combined)
    
    sr.begin(ShapeType.Line)
    sr.setColor(1, 1, 0, 1)
    sr.line(0f, 0f, 100f, 100f)
    sr.end()
  }
}
