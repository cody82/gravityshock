package cody.gravityshock.net;

import cody.gravityshock.*;

public class NetSerializeVisitor extends Visitor {
	public Object message;
	public short id;
	
	public void visit(Spaceship obj) {
		NetSpaceshipMessage msg = new NetSpaceshipMessage();
		msg.position.x = obj.body.getPosition().x;
		msg.position.y = obj.body.getPosition().y;
		msg.position.a = obj.body.getAngle();
		msg.object.id = id;
		msg.object.type = "Spaceship";
		message = msg;
	}
	
	public void visit(Actor obj) {
	}
	
	public void visit(Pickup obj) {
		
	}

	public void visit(Object obj) {
		
	}
}
