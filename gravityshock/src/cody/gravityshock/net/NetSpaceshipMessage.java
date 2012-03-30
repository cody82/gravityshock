package cody.gravityshock.net;

import cody.gravityshock.*;

public class NetSpaceshipMessage {
	public Object create(World world) {
		return new Spaceship(world);
	}
	public void apply(World world) {
		for(Actor a : world.actors) {
			if(a.net != null) {
				apply(world,a);
				return;
			}
		}
		
		Actor a = (Actor)create(world);
		apply(world,a);
	}
	public void apply(World world, Object obj) {
		Spaceship s = (Spaceship)obj;
		s.body.setTransform(position.x, position.y, position.a);
	}
	
	public NetObjectMessage object;
	public NetPositionMessage position;
}
