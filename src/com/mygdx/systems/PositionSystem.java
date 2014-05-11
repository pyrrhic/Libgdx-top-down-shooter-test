package com.mygdx.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.mygdx.components.Physics;
import com.mygdx.components.Player;
import com.mygdx.components.Position;
import com.mygdx.game.UnitConverter;

public class PositionSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Position> positionMapper;
	@Mapper ComponentMapper<Physics> physicsMapper;
	
	@SuppressWarnings({ "unchecked" })
	public PositionSystem() {
		super(Aspect.getAspectForAll(Player.class, Position.class, Physics.class));
	}

	/**
	 * As the box2d body moves, update the position component.
	 */
	@Override
	protected void process(Entity e) {
		Position position = positionMapper.get(e);
		Physics physics = physicsMapper.get(e);
		
		int playerWidth = 32;
		
		position.x = UnitConverter.box2dCoordToLibCoord(physics.body.getPosition().x, UnitConverter.convertPixelsToMeters(playerWidth));
		position.y = UnitConverter.box2dCoordToLibCoord(physics.body.getPosition().y, UnitConverter.convertPixelsToMeters(playerWidth));
	}

}
