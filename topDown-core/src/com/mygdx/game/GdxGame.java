package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.mygdx.levels.LevelOne;

public class GdxGame extends Game {	
	private Screen levelOne;
	
	@Override
	public void create () {		
		levelOne = new LevelOne(this);
		setScreen(levelOne);
	}
}
