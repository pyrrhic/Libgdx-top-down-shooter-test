package com.mygdx.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.components.Player;
import com.mygdx.components.Position;
import com.mygdx.game.AssetManager;

public class CameraSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Player> playerMapper;
	@Mapper ComponentMapper<Position> positionMapper;
	
	private OrthographicCamera camera = AssetManager.getInstance().getCamera();
	
	@SuppressWarnings("unchecked")
	public CameraSystem() {
		super(Aspect.getAspectForOne(Player.class));	
	}

	@Override
	protected void process(Entity e) {
		Position position = positionMapper.getSafe(e);
		
		camera.position.x = position.x;
		camera.position.y = position.y;
	}

}
