package gravityshock;

import com.badlogic.gdx.physics.box2d.BodyDef;

public class Glue {
    public static void setType(BodyDef def, BodyDef.BodyType t){
        def.type = t;
    }
}
