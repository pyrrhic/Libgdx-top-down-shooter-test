package com.mygdx.systems;

import java.awt.Point;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.components.Physics;
import com.mygdx.components.Player;
import com.mygdx.components.Position;
import com.mygdx.game.UnitConverter;

public class PlayerOrientationSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Position> positionMapper;
	@Mapper ComponentMapper<Physics> physicsMapper;
	
	private OrthographicCamera camera;
	
	@SuppressWarnings("unchecked")
	public PlayerOrientationSystem(OrthographicCamera camera) {
		super(Aspect.getAspectForAll(Player.class, Position.class, Physics.class));
		
		this.camera = camera;
	}

	@Override
	protected void process(Entity e) {
		Position position = positionMapper.get(e);
		Vector3 positionVec = new Vector3(position.x + 16f, position.y + 16f, 0);  // add half the player sprite width. should not be hard coded.
		
		PlayerInputSystem inputSystem = this.world.getSystem(PlayerInputSystem.class);
		Point mousePosition = inputSystem.getMouseLocation();
		Vector3 mouseVec = new Vector3(mousePosition.x, mousePosition.y, 0);
		camera.unproject(mouseVec);
		
		positionVec.sub(mouseVec);
		Vector2 faceMouse = new Vector2(positionVec.x, positionVec.y);
		
		Physics physics = physicsMapper.get(e);
		Vector2 bodyPosition = physics.body.getPosition();
		float angle = (float)Math.toRadians(faceMouse.angle());
		physics.body.setTransform(bodyPosition.x, bodyPosition.y, angle);
	}
}
