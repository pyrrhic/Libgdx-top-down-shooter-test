package com.mygdx.game;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.components.Collision;

public class CollisionHandler implements ContactListener {
	
	@Override
	public void beginContact(Contact contact) {
		Entity e1 = (Entity)contact.getFixtureA().getBody().getUserData();
		Entity e2 = (Entity)contact.getFixtureB().getBody().getUserData();
		
		ComponentMapper<Collision> collisionMapper = e1.getWorld().getMapper(Collision.class);
		
		Collision existingCollision1 = collisionMapper.getSafe(e1);
		if (existingCollision1 != null) e1.removeComponent(existingCollision1);
		
		Collision collision1 = new Collision();
		collision1.getEntityCollisions().add(e2);
		e1.addComponent(collision1);
		e1.changedInWorld();
		
		Collision existingCollision2 = collisionMapper.getSafe(e2);
		if (existingCollision2 != null) e2.removeComponent(existingCollision2);
		
		Collision collision2 = new Collision();
		collision2.getEntityCollisions().add(e1);
		e2.addComponent(collision2);
		e2.changedInWorld();
	}
	
	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
