package com.mygdx.game;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import com.mygdx.components.Bullet;
import com.mygdx.components.Enemy;
import com.mygdx.components.Gun;
import com.mygdx.components.LifeInSeconds;
import com.mygdx.components.Movable;
import com.mygdx.components.Physics;
import com.mygdx.components.Player;
import com.mygdx.components.Position;
import com.mygdx.game.CollisionHandler.EntityCategory;


public class EntityFactory {
	private static EntityFactory instance;
	
	public static EntityFactory getInstance() {
		if (instance == null) instance = new EntityFactory();
		return instance;
	}
	
	private World world;
	private com.badlogic.gdx.physics.box2d.World box2dWorld;
	
	public void setEntityWorld(World world) {
		this.world = world;
	}
	
	public void setBox2dWorld(com.badlogic.gdx.physics.box2d.World box2dWorld) {
		this.box2dWorld = box2dWorld;
	}
	
	public void createPlayer(float x, float y, Body ground) {
		float width = 32;
		float height = 32;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(UnitConverter.libCoordToBox2dCoord(x, width), UnitConverter.libCoordToBox2dCoord(y, height));
		Body body = box2dWorld.createBody(bodyDef);
		
		FixtureDef fixDef = new FixtureDef();
		PolygonShape playerRectangle = new PolygonShape();
		playerRectangle.setAsBox(UnitConverter.convertPixelsToMeters(width) / 2, UnitConverter.convertPixelsToMeters(height) / 2);
		fixDef.shape = playerRectangle;
		fixDef.density = 1;
		fixDef.friction = .2f;
		fixDef.filter.categoryBits = EntityCategory.BEING.getValue();
		fixDef.filter.maskBits = (short) (EntityCategory.PROJECTILE.getValue() ^ EntityCategory.WALL.getValue() ^ EntityCategory.BEING.getValue());
		body.createFixture(fixDef);		
		
		FrictionJointDef frictionJointDef = new FrictionJointDef();
		frictionJointDef.initialize(body, ground, body.getWorldCenter());
		FrictionJoint frictionJoint = (FrictionJoint) box2dWorld.createJoint(frictionJointDef);
		frictionJoint.setMaxForce(50);
		frictionJoint.setMaxTorque(50);
		
		Entity e = world.createEntity();
		e.addComponent(new Player());
		e.addComponent(new Physics(body));
		e.addComponent(new Movable(100 ,100));
		e.addComponent(new Position(x, y));
		
		e.addComponent(new Gun(.7f, 3)); //move this
		
		body.setUserData(e);
		
		e.addToWorld();
	}
	
	public void createEnemy(float x, float y, Body ground) {
		float width = 32;
		float height = 32;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(UnitConverter.libCoordToBox2dCoord(x, width), UnitConverter.libCoordToBox2dCoord(y, height));
		Body body = box2dWorld.createBody(bodyDef);
		
		FixtureDef fixDef = new FixtureDef();
		PolygonShape playerRectangle = new PolygonShape();
		playerRectangle.setAsBox(UnitConverter.convertPixelsToMeters(width) / 2, UnitConverter.convertPixelsToMeters(height) / 2);
		fixDef.shape = playerRectangle;
		fixDef.density = 1;
		fixDef.friction = .2f;
		fixDef.filter.categoryBits = EntityCategory.BEING.getValue();
		fixDef.filter.maskBits = (short) (EntityCategory.PROJECTILE.getValue() ^ EntityCategory.WALL.getValue() ^ EntityCategory.BEING.getValue());
		body.createFixture(fixDef);		
		
		FrictionJointDef frictionJointDef = new FrictionJointDef();
		frictionJointDef.initialize(body, ground, body.getWorldCenter());
		FrictionJoint frictionJoint = (FrictionJoint) box2dWorld.createJoint(frictionJointDef);
		frictionJoint.setMaxForce(50);
		frictionJoint.setMaxTorque(50);
		
		Entity e = world.createEntity();
		e.addComponent(new Enemy());
		e.addComponent(new Physics(body));
		e.addComponent(new Movable(100 ,100));
		e.addComponent(new Position(x, y));
		
		e.addComponent(new Gun(.7f, 3)); //move this
		
		body.setUserData(e);
		
		e.addToWorld();
	}
	
	public Body createCollisionBox(Rectangle r, boolean isFloor) {	
		BodyDef bodyDef = new BodyDef(); 
	    bodyDef.type = BodyType.StaticBody;
	    bodyDef.position.set(UnitConverter.libCoordToBox2dCoord(r.x, r.width), UnitConverter.libCoordToBox2dCoord(r.y, r.height));
	    Body body = box2dWorld.createBody(bodyDef);
	    
	    FixtureDef fixDef = new FixtureDef();
	    PolygonShape wallRectangle = new PolygonShape();
	    wallRectangle.setAsBox(UnitConverter.convertPixelsToMeters(r.width)/2, UnitConverter.convertPixelsToMeters(r.height)/2);
	    fixDef.shape = wallRectangle;
	    if (isFloor) {
	    	fixDef.filter.categoryBits = EntityCategory.GROUND.getValue();
	    } else {
	    	fixDef.filter.categoryBits = EntityCategory.WALL.getValue();
	    	fixDef.filter.maskBits = (short) (EntityCategory.PROJECTILE.getValue() ^ EntityCategory.BEING.getValue());
	    }
	    body.createFixture(fixDef);
	    
	    Entity e = world.createEntity();
	    e.addComponent(new Physics(body));
	    
	    body.setUserData(e);
	    
	    e.addToWorld();
	    
	    return body;
	}
	
	public void createBullet(float x, float y, float velocityX, float velocityY) {
		float width = UnitConverter.convertPixelsToMeters(1f);
		float height = UnitConverter.convertPixelsToMeters(1f);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.bullet = true;
		bodyDef.gravityScale = 0;
		
		bodyDef.position.set(x, y);
		Body body = box2dWorld.createBody(bodyDef);
		
		FixtureDef fixDef = new FixtureDef();
		PolygonShape rectangle = new PolygonShape();
		rectangle.setAsBox(width, height);
		fixDef.shape = rectangle;
		fixDef.density = 1;
		fixDef.filter.categoryBits = EntityCategory.PROJECTILE.getValue();
		fixDef.filter.maskBits = (short) (EntityCategory.WALL.getValue() ^ EntityCategory.BEING.getValue());
		body.createFixture(fixDef);
		
		body.applyForceToCenter(velocityX, velocityY, true);
		
		Entity e = world.createEntity();
		e.addComponent(new Bullet());
		e.addComponent(new Physics(body));
		e.addComponent(new Position(x, y));
		e.addComponent(new LifeInSeconds(3));
		
		body.setUserData(e);

		
		e.addToWorld();
	}
}
