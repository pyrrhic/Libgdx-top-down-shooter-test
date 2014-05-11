package com.mygdx.game;

import java.util.HashMap;

import box2dLight.RayHandler;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class AssetManager {
	private static AssetManager instance;
	
	public static AssetManager getInstance() {
		if (instance == null) instance = new AssetManager();		
		return instance;
	}
	
	private TextureAtlas atlas;
	private HashMap<String, TextureRegion> texRegionCache;
	private HashMap<String, Sound> soundCache;	
	
	private OrthographicCamera camera;
	
	private World world;
	private com.badlogic.gdx.physics.box2d.World box2dWorld;
	
	private RayHandler rayHandler;
	
	private AssetManager() {
		preload();
	}
	
	public OrthographicCamera getCamera() {
		if (camera == null) {
			camera = new OrthographicCamera();
			camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		
		return camera;
	}
	
	public World getWorld() {
		if (world == null) {
			world = new World();
		}
		return world;
	}
	
	public com.badlogic.gdx.physics.box2d.World getBox2dWorld() {
		if (box2dWorld == null) {
			box2dWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), false);
			box2dWorld.setContactListener(new CollisionHandler());
		}
		return box2dWorld;
	}
	
	public RayHandler getRayHandler() {
		if (rayHandler == null) {
			rayHandler = new RayHandler(box2dWorld);
			rayHandler.setAmbientLight(1, 1, 1, .3f);
			//rayHandler.useDiffuseLight(true);
			
		}
		
		return rayHandler;
	}
	
	public enum TexRegion {
		IDLE, WALK, FIRE, 
		TILESET, 
		BOSS, BOSSWALK, 
		BULLET, PHOTON;
	}
	
	public TextureRegion getTextureRegion(TexRegion texReg) {
		return getTextureRegion(texReg, -1);
	}
	
	public TextureRegion getTextureRegion(TexRegion texReg, int index) {
		String key = (texReg.name() + index).toLowerCase();
		TextureRegion tr = texRegionCache.get(key);
		if (tr == null) {
			TextureRegion newTexRegion = atlas.findRegion(texReg.name().toLowerCase(), index);
			texRegionCache.put(key, newTexRegion);
			tr = newTexRegion;
			
			if (tr == null) throw new NullPointerException();
		}
		return tr;
	}
	
	public Sound getSound(String key) {
		key = key.toLowerCase();
		Sound sound = soundCache.get(key);
		
		if (sound == null)  {
			Sound newSound = Gdx.audio.newSound(Gdx.files.internal(key + ".wav"));;
			soundCache.put(key, newSound);
			sound = newSound;
			
			if (sound == null) throw new NullPointerException();
		}
		return sound;
	}
	
	private void preload() {
		//atlas = new TextureAtlas("ld29.txt");
		texRegionCache = new HashMap<String, TextureRegion>();
		soundCache = new HashMap<String, Sound>();
	}
}
