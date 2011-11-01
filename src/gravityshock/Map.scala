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

import scala.xml.pull._
import scala.io.Source
import scala.xml.XML

class Map {
  var world: World = _
  var shapes: Array[box2d.ChainShape] = new Array[box2d.ChainShape](0)
  var body: box2d.Body = _
  var fixtures: Array[box2d.Fixture] = new Array[box2d.Fixture](0)
  var mesh: graphics.Mesh = _
  var shader: ShaderProgram = _
  var points: Array[Array[Vector2]] = new Array[Array[Vector2]](0)
  
  def load(_world: World, filename: String) = {
    world = _world
    world.map = this
    create(filename)
  }

  def create(filename: String) = {
    var file = gdx.Gdx.files.internal(filename)
    var data = file.readString()
    //val src = Source.fromString(data)
    var xml = XML.loadString(data)
    var groups = xml.child.filter((x)=> x.label == "g")
    var paths = groups.flatMap((x) => x.child.filter((x) => x.label == "path"))
    
    var bdef = new box2d.BodyDef()
    Glue.setType(bdef, box2d.BodyDef.BodyType.StaticBody)
    
    body = world.b2world.createBody(bdef)

    for(p <- paths) {
      var d = p.attribute("d").get.text
      var coords = d.split(' ').filter((x) => x.length > 1)
      var array = coords.map((x) => {
          var v = x.split(',').map(y => y.toFloat)
          new Vector2(v(0)*0.1f, v(1)*0.1f)
        })
      
      points = points :+ array
      var shape = new box2d.ChainShape()
      shape.createLoop(array)
      shapes = shapes :+ shape
      var fixture = body.createFixture(shape, 1)
      fixtures :+ fixture
    }
    
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
    for(array <- points) {
      for(i <- 0 until array.length - 1) {
        sr.line(array(i).x, array(i).y, array(i+1).x, array(i+1).y)
      }
      sr.line(array.last.x, array.last.y, array(0).x, array(0).y)
    }
      sr.end()
  }
}
