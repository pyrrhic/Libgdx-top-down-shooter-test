package com.mygdx.systems.collisions;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.mygdx.components.Bullet;
import com.mygdx.components.Collision;
import com.mygdx.components.LifeInSeconds;

public class BulletCollisionSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Bullet> bulletMapper;
	@Mapper ComponentMapper<Collision> collisionMapper;
	@Mapper ComponentMapper<LifeInSeconds> lifeInSecondsMapper;
	
	@SuppressWarnings("unchecked")
	public BulletCollisionSystem() {
		super(Aspect.getAspectForAll(Collision.class, Bullet.class, LifeInSeconds.class));
	}

	@Override
	protected void process(Entity e) {
		Collision collision = collisionMapper.get(e);
		
		for(Entity collisionEntity : collision.getEntityCollisions()) {
			Bullet collisionBullet = bulletMapper.get(collisionEntity);
			if (collisionBullet == null) {
//				LifeInSeconds lifeInSeconds = lifeInSecondsMapper.get(e);
//				lifeInSeconds.lifeInSeconds = 0;
//				
//				e.removeComponent(collision);
//				e.changedInWorld();
			}	
		}
	}

}
