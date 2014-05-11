package com.mygdx.systems;

import java.awt.Point;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.components.Movable;
import com.mygdx.components.Physics;
import com.mygdx.components.Player;

public class PlayerInputSystem extends EntityProcessingSystem implements InputProcessor {
	@Mapper ComponentMapper<Physics> physicsMapper;
	@Mapper ComponentMapper<Movable> movableMapper;
	
	private boolean[] keyDownList = new boolean[255];
	
	private Point mouseLocation = new Point(0, 0);
	
	private boolean[] mouseClickList = new boolean[2]; // 0 = right, 1 = left
	
	@SuppressWarnings("unchecked")
	public PlayerInputSystem() {
		super(Aspect.getAspectForAll(Player.class, Physics.class, Movable.class));
	}
	
	@Override
	protected void process(Entity e) {
		Physics physics = physicsMapper.get(e);
		Movable movable = movableMapper.get(e);
		
		if (keyDownList[Input.Keys.A]) {
			if (physics.body.getLinearVelocity().x > -1 * movable.maxXvelocity / 10) {
				physics.body.applyForceToCenter(movable.maxXvelocity * -1, 0, true);
			}
		}
		
		if (keyDownList[Input.Keys.D]) {
			if (physics.body.getLinearVelocity().x < movable.maxXvelocity / 10) {
				physics.body.applyForceToCenter(movable.maxXvelocity, 0, true);
			}
		}
		
		if (keyDownList[Input.Keys.W]) {
			if (physics.body.getLinearVelocity().y < movable.maxYvelocity / 10) {
				physics.body.applyForceToCenter(0, movable.maxYvelocity, true);
			}
		}
		
		if (keyDownList[Input.Keys.S]) {
			if (physics.body.getLinearVelocity().y > -1 * movable.maxYvelocity / 10) {
				physics.body.applyForceToCenter(0, movable.maxXvelocity * -1, true);
			}
		}
	}

	public Point getMouseLocation() {
		return mouseLocation;
	}
	
	public boolean[] getMouseClick() {
		return mouseClickList;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		keyDownList[keycode] = true;
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		keyDownList[keycode] = false;
		
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		mouseClickList[button] = true;
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		mouseClickList[button] = false;
		
		return false;
	}
	
	//Called when mouse moves and no buttons are being pressed.
	@Override 
	public boolean mouseMoved(int screenX, int screenY) {
		mouseLocation.setLocation(screenX, screenY);
		
		return false;
	}
	
	//Called when mouse moves and a button is pressed.
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		mouseLocation.setLocation(screenX, screenY);
		
		return false;
	}
	
	/**************************
	 * UNUSED FROM HERE ON DOWN 
	 */
	
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
