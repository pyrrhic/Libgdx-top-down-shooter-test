package com.mygdx.systems;

import java.awt.Point;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.components.Gun;
import com.mygdx.components.Physics;
import com.mygdx.components.Player;
import com.mygdx.game.EntityFactory;

public class PlayerShootingSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Player> playerMapper;
	@Mapper ComponentMapper<Physics> physicsMapper;
	@Mapper ComponentMapper<Gun> gunMapper;
	
	@SuppressWarnings("unchecked")
	public PlayerShootingSystem() {
		super(Aspect.getAspectForAll(Player.class, Physics.class, Gun.class));

	}

	@Override
	protected void process(Entity e) {
			PlayerInputSystem playerInputSystem = this.world.getSystem(PlayerInputSystem.class);
			
			Player player = playerMapper.get(e);
			boolean[] isButtonPressed = playerInputSystem.getMouseClick();
			if (isButtonPressed[Input.Buttons.LEFT]) player.currentState = Player.State.SHOOTING;
			
			if (player.currentState == Player.State.SHOOTING) {
				Gun gun = gunMapper.get(e);
				if (gun.timeTillNextBurst <= 0) {
					if (gun.timeTillNextShot <= 0) {
						Body playerBody = physicsMapper.get(e).body;
						fireLaser(playerBody, playerInputSystem.getMouseLocation());
						
						gun.shotsLeftInBurst -= 1;
						
						if (gun.shotsLeftInBurst <= 0) { 
							player.currentState = Player.State.IDLE;
							gun.reset();
						}
					} else {
						gun.timeTillNextShot -= this.world.getDelta();
					}
				} else {
					gun.timeTillNextBurst -= this.world.getDelta();
				}
			}
	}
	
	public void fireLaser(Body playerBody, Point mouseClickLoc) {						
		float velocity = 2f;
		float angleInRadians = playerBody.getAngle();
		
		float velocityX = -1 * velocity * (float)Math.cos(angleInRadians);
		float velocityY = -1 * velocity * (float)Math.sin(angleInRadians);
		
		int gunOffset = 1;
	
		float x = playerBody.getPosition().x - ((float)Math.cos(angleInRadians) * gunOffset);
		float y = playerBody.getPosition().y - ((float)Math.sin(angleInRadians) * gunOffset);
		
		EntityFactory.getInstance().createBullet(x, y, velocityX, velocityY);
	}
}
