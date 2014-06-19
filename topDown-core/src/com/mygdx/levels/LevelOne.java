package com.mygdx.levels;

import java.awt.Point;

import com.artemis.World;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.mygdx.game.AssetManager;
import com.mygdx.game.EntityFactory;
import com.mygdx.game.Map;
import com.mygdx.game.UnitConverter;
import com.mygdx.systems.CameraSystem;
import com.mygdx.systems.EnemySystem;
import com.mygdx.systems.LifeInSecondsSystem;
import com.mygdx.systems.PlayerInputSystem;
import com.mygdx.systems.PlayerOrientationSystem;
import com.mygdx.systems.PlayerShootingSystem;
import com.mygdx.systems.PositionSystem;
import com.mygdx.systems.collisions.BulletCollisionSystem;

public class LevelOne implements Screen {
	private World world;
	private com.badlogic.gdx.physics.box2d.World box2dWorld;
	
	private Box2DDebugRenderer debugRenderer;
	private Matrix4 debugMatrix;
	
	private float accumulator;
	private float step = 1f / 60f;
	
	private OrthographicCamera camera;
	
	private FPSLogger fps = new FPSLogger();
	
	public LevelOne(Game game) {		
		camera = AssetManager.getInstance().getCamera();
		
		//worlds
		box2dWorld = AssetManager.getInstance().getBox2dWorld();
		world = AssetManager.getInstance().getWorld();
		EntityFactory.getInstance().setBox2dWorld(box2dWorld);
		EntityFactory.getInstance().setEntityWorld(world);
		
		//systems
		PlayerInputSystem inputSystem = new PlayerInputSystem();
		world.setSystem(inputSystem);
		world.setSystem(new PlayerOrientationSystem(camera));
		world.setSystem(new PlayerShootingSystem());
		world.setSystem(new BulletCollisionSystem());
		world.setSystem(new CameraSystem());
		world.setSystem(new PositionSystem());
		world.setSystem(new LifeInSecondsSystem());
		
		//map and a system
		Map map = new Map("map.tmx");
		world.setSystem(new EnemySystem(map));
		
		//tags
		world.setManager(new TagManager());
		
		world.initialize();
		
		//some of the input
		Gdx.input.setInputProcessor(inputSystem);
		
		//misc like player, map, enemies, etc		
		Point playerSpawn = map.getSpawnLoc("player");
		EntityFactory.getInstance().createPlayer(playerSpawn.x, playerSpawn.y, map.getGround());
		
		Point enemySpawn = map.getSpawnLoc("enemy");
		EntityFactory.getInstance().createEnemy(enemySpawn.x, enemySpawn.y, map.getGround());
		
		
		//debug render
		debugRenderer = new Box2DDebugRenderer();
		debugMatrix = new Matrix4(camera.combined);
		//debugMatrix.scale(UnitConverter.getNumPixelsInMeter(), UnitConverter.getNumPixelsInMeter(), 1f);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		accumulator += delta;
		while (accumulator >= step) {
			box2dWorld.step(step, 5, 8);
			accumulator -= step;
		}
		
		world.setDelta(delta);		
		world.process();
		
		camera.update();
		
		debugMatrix.set(camera.combined);
		debugMatrix.scale(UnitConverter.getNumPixelsInMeter(), UnitConverter.getNumPixelsInMeter(), 1f);
		debugRenderer.render(box2dWorld, debugMatrix);
		
		//fps.log();
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}
