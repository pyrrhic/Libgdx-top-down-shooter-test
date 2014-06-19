package com.mygdx.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.mygdx.components.LifeInSeconds;
import com.mygdx.components.Physics;
import com.mygdx.game.AssetManager;

public class LifeInSecondsSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<LifeInSeconds> lifeInSecondsMapper;
	@Mapper ComponentMapper<Physics> physicsMapper;
	
	@SuppressWarnings("unchecked")
	public LifeInSecondsSystem() {
		super(Aspect.getAspectForAll(LifeInSeconds.class, Physics.class));
	}

	@Override
	protected void process(Entity e) {
		LifeInSeconds lifeInSeconds = lifeInSecondsMapper.get(e);
		if (lifeInSeconds.lifeInSeconds <= 0) {
			e.deleteFromWorld();
			
			Physics physics = physicsMapper.get(e);
			AssetManager.getInstance().getBox2dWorld().destroyBody(physics.body);
		}
	}

}
