package com.mygdx.game;

import java.awt.Point;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

public class Map {
	private TiledMap map = new TmxMapLoader().load("map.tmx");	
	private Body groundBody;
	
	public Map() {
		createCollisionMap();
		createGround();
	}
	
	public Body getGround() {
		if (groundBody == null) {
			createGround();
		}
		
		return groundBody;
	}
	
	public Point getSpawnLoc(String spawn) {
		MapLayer layer = map.getLayers().get("spawns");

		Point playerSpawnLoc = new Point();
		for (MapObject mo : layer.getObjects()) {
			if (mo.getName() != null && mo.getName().equals(spawn)) {
				RectangleMapObject rmo = (RectangleMapObject) mo;
				Rectangle rectangle = rmo.getRectangle();
				playerSpawnLoc = new Point();
				playerSpawnLoc.x = (int)Math.round(rectangle.x);
				playerSpawnLoc.y = (int)Math.round(rectangle.y);
			}	
		}
		
		return playerSpawnLoc;
	}
	
	private void createCollisionMap() {				
		MapLayer layer = map.getLayers().get("walls");

		for (MapObject mo : layer.getObjects()) {
			if (mo.getName() == null) {
				RectangleMapObject rmo = (RectangleMapObject) mo;
				Rectangle rectangle = rmo.getRectangle();
				EntityFactory.getInstance().createCollisionBox(rectangle, false);
			}	
		}
	}
	
	private void createGround() {
		MapProperties mapProperties = map.getProperties();
		
		int numTilesWidth = mapProperties.get("width", Integer.class);
		int numTilesHeight = mapProperties.get("height", Integer.class);
		
		int tileSize = mapProperties.get("tilewidth", Integer.class);
		
		int width = numTilesWidth * tileSize;
		int height = numTilesHeight * tileSize;
		
		Rectangle r = new Rectangle();
		r.height = height;
		r.width = width;
		r.x = 0;
		r.y = 0;
		
		groundBody = EntityFactory.getInstance().createCollisionBox(r, true);
	}
}
