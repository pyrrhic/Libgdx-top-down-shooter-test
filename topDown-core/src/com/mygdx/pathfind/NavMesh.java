package com.mygdx.pathfind;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.components.Position;

public class NavMesh {
	private HashMap<String, Node> nodes;
	
	public NavMesh(int initialCapacity) {
		nodes = new HashMap<String, Node>(initialCapacity);
	}
	
	public void buildNavMesh(TiledMap map) {		
		MapLayer layer = map.getLayers().get("nav mesh");
		MapObjects objects = layer.getObjects();
		
		//calc center point of triangle, make it the node.
		for (MapObject mo : objects) {
			PolygonMapObject polygonMapObject = (PolygonMapObject)mo;
			Polygon polygon = polygonMapObject.getPolygon();
			
			float[] vertices = polygon.getTransformedVertices();
			Vector2 center = GeometryUtils.triangleCentroid(vertices[0], vertices[1], vertices[2], vertices[3], vertices[4], vertices[5], new Vector2(polygon.getX(), polygon.getY()));
			
			Node node = new Node(center.x, center.y);
			node.polygon = polygon;
		    addNode(node);
		}
		
		//add neighbors. neighbor means 2 vertices are shared between 2 triangles.
		for (String key : getNodes().keySet()) {
			Node currentNode = getNodes().get(key);
			float[] v = currentNode.polygon.getTransformedVertices();			
			
			for (String k : getNodes().keySet()) {
				Node neighborNode = getNodes().get(k);
				Polygon poly = neighborNode.polygon;
				
				int numSharedVertices = 0;
				int x = 0;
				int y = 1;
				for (int i = 0; i < (v.length / 2); i++) {
					if (PolygonUtils.triangleContains(new Vector2(v[x], v[y]), poly)) numSharedVertices++;
					x+=2;
					y+=2;
				}
				
				if (numSharedVertices >= 2) {
					currentNode.addNeighbor(neighborNode);
				}	
			}
		}
	}
	
	public void addNode(Node newNode) {
		if (!nodes.containsKey(newNode.getKey())) {
			nodes.put(newNode.getKey(), newNode);
		}
		else {
			//add any "new" neighbors
			Node oldNode = nodes.get(newNode.getKey());
			Set<String> neighborKeys = newNode.getNeighbors().keySet();
			for (String key : neighborKeys) {
				oldNode.addNeighbor(newNode.getNeighbors().get(key));
			}
		}
	}
	
	public Node getNode(String key) {
		Node returnNode = null;
		if (nodes.containsKey(key)) {
			returnNode = nodes.get(key);
		}
		
		return returnNode;
	}
	
	public Map<String, Node> getNodes() {
		return nodes;
	}
	
	public void resetParentsAndCosts() {
		for (Entry<String, Node> entry : nodes.entrySet()) {
			Node node = entry.getValue();
			node.parent = null;
			node.cost = 9999;
			entry.setValue(node);
		}
	}
	
	public Node getNodeEntityIsIn(Position position) {
		Vector2 positionVector = new Vector2(position.x, position.y);
		Node currentNode = null;
		
		for (Entry<String,Node> cursor : nodes.entrySet()) {
			Polygon polygon = cursor.getValue().polygon;
			
			if (PolygonUtils.triangleContains(positionVector, polygon)) currentNode = cursor.getValue();
			
			if (currentNode != null) break;
		}
		
		return currentNode;
	}
	
	public void drawNavMesh(OrthographicCamera camera) {
		ShapeRenderer triangleRenderer = new ShapeRenderer();
		triangleRenderer.setProjectionMatrix(camera.combined);
		triangleRenderer.begin(ShapeType.Line);
		triangleRenderer.setColor(Color.BLUE);
		
		Set<String> keys = nodes.keySet();
		for (String key : keys) {
			Node node = nodes.get(key);
			
			//draw triangle
			Polygon poly = node.polygon;
			float[] v = poly.getTransformedVertices();
			triangleRenderer.triangle(v[0], v[1], v[2], v[3], v[4], v[5]);
		}
		
		triangleRenderer.end();
		triangleRenderer.dispose();
	}
	
	public void drawNodes(OrthographicCamera camera) {
		SpriteBatch batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		BitmapFont font = new BitmapFont();
		
		ShapeRenderer circleRenderer = new ShapeRenderer();
		circleRenderer.setProjectionMatrix(camera.combined);
		circleRenderer.begin(ShapeType.Filled);
		
		Set<String> keys = nodes.keySet();
		for (String key : keys) {
			Node node = nodes.get(key);
			
			//draw node and links
			if (node.x == 32.0 && node.y == 768.0) {
				circleRenderer.setColor(Color.GREEN);
			}
			else {
				circleRenderer.setColor(Color.WHITE);
			}
			circleRenderer.circle(node.x, node.y, 5);
			
			batch.begin();
			font.draw(batch, node.getKey(), node.x, node.y);
			batch.end();
		}
		
		circleRenderer.end();
		circleRenderer.dispose();
		
		batch.dispose();
	}
	
	public void drawNeighbors(OrthographicCamera camera) {
		ShapeRenderer lineRenderer = new ShapeRenderer();
		lineRenderer.setProjectionMatrix(camera.combined);
		lineRenderer.begin(ShapeType.Line);
		
		Set<String> keys = nodes.keySet();
		for (String key : keys) {
			Node node = nodes.get(key);

			Set<String> neighborKeys = node.getNeighbors().keySet();
			for (String nKey : neighborKeys) {
				Node neighborNode = node.getNeighbors().get(nKey);				
				lineRenderer.line(node.x, node.y, neighborNode.x, neighborNode.y);
			}
		}
		
		lineRenderer.end();
		lineRenderer.dispose();
	}
}
