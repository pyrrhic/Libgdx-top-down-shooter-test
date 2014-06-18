package com.mygdx.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.mygdx.components.Physics;
import com.mygdx.components.Position;
import com.mygdx.components.Size;
import com.mygdx.game.UnitConverter;

public class PositionSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Position> positionMapper;
	@Mapper ComponentMapper<Physics> physicsMapper;
	@Mapper ComponentMapper<Size> sizeMapper;
	
	@SuppressWarnings({ "unchecked" })
	public PositionSystem() {
		super(Aspect.getAspectForAll(Position.class, Physics.class, Size.class));
	}

	/**
	 * As the box2d body moves, update the position component.
	 */
	@Override
	protected void process(Entity e) {
		Physics physics = physicsMapper.get(e);
		Size entitySize = sizeMapper.get(e);
		int width = entitySize.getWidth();
		float libX = UnitConverter.box2dCoordToLibCoord(physics.body.getPosition().x, UnitConverter.convertPixelsToMeters(width));
		float libY = UnitConverter.box2dCoordToLibCoord(physics.body.getPosition().y, UnitConverter.convertPixelsToMeters(width));
		
		Position position = positionMapper.get(e);
		position.x = libX + width / 2;
		
		int height = entitySize.getHeight();
		position.y = libY + height / 2;
	}

}
