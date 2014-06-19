package com.mygdx.systems;

import java.awt.Point;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.components.Gun;
import com.mygdx.components.Physics;
import com.mygdx.components.Player;
import com.mygdx.game.AssetManager;
import com.mygdx.game.EntityFactory;
import com.mygdx.game.UnitConverter;

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
		Sound laserSound = AssetManager.getInstance().getSound("gun2");
		
		PlayerInputSystem playerInputSystem = this.world.getSystem(PlayerInputSystem.class);
			
		Player player = playerMapper.get(e);
		boolean[] isButtonPressed = playerInputSystem.getMouseClick();
		if (isButtonPressed[Input.Buttons.LEFT]) {
			player.currentState = Player.State.SHOOTING;
		} else {
			player.currentState = Player.State.IDLE;
		}
		
		Gun gun = gunMapper.get(e);
		if (gun.timeTillNextBurst <= 0) {					
			if (gun.timeTillNextShot <= 0) {
				if (player.currentState == Player.State.SHOOTING || gun.shotsLeftInBurst < gun.getShotsPerBurst()) {
					Body playerBody = physicsMapper.get(e).body;
					fireLaser(playerBody, playerInputSystem.getMouseLocation());
					
					laserSound.play();
					
					
					
					gun.timeTillNextShot = gun.getShotDelay();
					gun.shotsLeftInBurst -= 1;
					
					if (gun.shotsLeftInBurst <= 0) { 
						player.currentState = Player.State.IDLE;
						gun.reset();
					}	
				}
			} else {
				gun.timeTillNextShot -= this.world.getDelta();
			}
		} else {
			gun.timeTillNextBurst -= this.world.getDelta();
		}
	}
	
	public void fireLaser(Body playerBody, Point mouseClickLoc) {						
		float velocity = 120f;
		float angleInRadians = playerBody.getAngle();
		
		float velocityX = -1 * velocity * (float)Math.cos(angleInRadians);
		float velocityY = -1 * velocity * (float)Math.sin(angleInRadians);
		
		float gunOffset = .6f;
	
		float x = playerBody.getPosition().x - ((float)Math.cos(angleInRadians) * gunOffset);
		float y = playerBody.getPosition().y - ((float)Math.sin(angleInRadians) * gunOffset);
		
		EntityFactory.getInstance().createBullet(x, y, UnitConverter.convertPixelsToMeters(velocityX), UnitConverter.convertPixelsToMeters(velocityY));
	}
}
