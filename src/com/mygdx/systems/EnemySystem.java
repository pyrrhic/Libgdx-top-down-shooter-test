package com.mygdx.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.mygdx.components.Enemy;

public class EnemySystem extends EntityProcessingSystem {

	@SuppressWarnings("unchecked")
	public EnemySystem() {
		super(Aspect.getAspectForAll(Enemy.class));
	}

	@Override
	protected void process(Entity e) {
		
		
	}

}
