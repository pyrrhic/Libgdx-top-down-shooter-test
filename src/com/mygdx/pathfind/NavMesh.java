package com.mygdx.pathfind;

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import com.artemis.Entity;
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
	public NodeList nodeList = new NodeList(30);
	
	public List<Node> path;
	
	//ONLY UP HERE CUZ TESTING
	private PathSmoother pathSmoother;
	
	public NodeList buildNavMesh(TiledMap map) {
		//TiledMap map = new TmxMapLoader().load("map.tmx");
		
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
		    nodeList.addNode(node);
		}
		
		//add neighbors. neighbor means 2 vertices are shared between 2 triangles.
		for (String key : nodeList.getNodes().keySet()) {
			Node currentNode = nodeList.getNodes().get(key);
			float[] v = currentNode.polygon.getTransformedVertices();			
			
			for (String k : nodeList.getNodes().keySet()) {
				Node neighborNode = nodeList.getNodes().get(k);
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
		
		//return nodeList;
		
//		Node start = nodeList.getNode("117736");
//		Node end = nodeList.getNode("55596");
//		//PathFinder pf = new PathFinder(nodeList);
//		PathFinder pf = new PathFinder();
//		path = pf.findPath(start, end);
//		System.out.println(path.size());

		return nodeList;
		
//		pathSmoother = new PathSmoother();
//		pathSmoother.smoothPath(new Vector2(start.x,  start.y), path);
	}
	
	public Node getNodeEntityIsIn(Position position) {
		Vector2 positionVector = new Vector2(position.x, position.y);
		Node currentNode = null;
		
		Set<Entry<String,Node>> nodes = nodeList.getNodes().entrySet();
		for (Entry<String,Node> cursor : nodes) {
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
		
		Set<String> keys = nodeList.getNodes().keySet();
		for (String key : keys) {
			Node node = nodeList.getNode(key);
			
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
		
		Set<String> keys = nodeList.getNodes().keySet();
		for (String key : keys) {
			Node node = nodeList.getNode(key);
			
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
		
		Set<String> keys = nodeList.getNodes().keySet();
		for (String key : keys) {
			Node node = nodeList.getNode(key);

			Set<String> neighborKeys = node.getNeighbors().keySet();
			for (String nKey : neighborKeys) {
				Node neighborNode = node.getNeighbors().get(nKey);				
				lineRenderer.line(node.x, node.y, neighborNode.x, neighborNode.y);
			}
		}
		
		lineRenderer.end();
		lineRenderer.dispose();
	}
	
//	public void drawNodes(OrthographicCamera camera) {
//		ShapeRenderer circleRenderer = new ShapeRenderer();
//		circleRenderer.setProjectionMatrix(camera.combined);
//		circleRenderer.begin(ShapeType.Filled);
//		
//		ShapeRenderer lineRenderer = new ShapeRenderer();
//		lineRenderer.setProjectionMatrix(camera.combined);
//		lineRenderer.begin(ShapeType.Line);
//		
//		ShapeRenderer triangleRenderer = new ShapeRenderer();
//		triangleRenderer.setProjectionMatrix(camera.combined);
//		triangleRenderer.begin(ShapeType.Line);
//		triangleRenderer.setColor(Color.BLUE);
//		
//		ShapeRenderer pathRenderer = new ShapeRenderer();
//		pathRenderer.setProjectionMatrix(camera.combined);
//		pathRenderer.begin(ShapeType.Line);
//		pathRenderer.setColor(Color.RED);
//		
//		ShapeRenderer portalRenderer = new ShapeRenderer();
//		portalRenderer.setProjectionMatrix(camera.combined);
//		portalRenderer.begin(ShapeType.Line);
//		portalRenderer.setColor(Color.YELLOW);
//		
//		SpriteBatch batch = new SpriteBatch();
//		batch.setProjectionMatrix(camera.combined);
//		BitmapFont font = new BitmapFont();
//		
//		Set<String> keys = nodeList.getNodes().keySet();
//		for (String key : keys) {
//			Node node = nodeList.getNode(key);
//			
//			//draw triangle
//			Polygon poly = node.polygon;
//			float[] v = poly.getTransformedVertices();
//			triangleRenderer.triangle(v[0], v[1], v[2], v[3], v[4], v[5]);
//			
//			//draw node and links
//			if (node.x == 32.0 && node.y == 768.0) {
//				circleRenderer.setColor(Color.GREEN);
//			}
//			else {
//				circleRenderer.setColor(Color.WHITE);
//			}
//			circleRenderer.circle(node.x, node.y, 5);
//			
//			batch.begin();
			//font.draw(batch, node.getKey(), node.x, node.y);
			//String kkk = (node.parent == null) ? "null" : node.parent.getKey();
			//font.draw(batch, kkk, node.x, node.y);
//			batch.end();
//			
//			Set<String> neighborKeys = node.getNeighbors().keySet();
//			for (String nKey : neighborKeys) {
//				Node neighborNode = node.getNeighbors().get(nKey);				
//				lineRenderer.line(node.x, node.y, neighborNode.x, neighborNode.y);
//			}
//			
//			for (Node pathNode : path) {
//				float[] vv = pathNode.polygon.getTransformedVertices();
//				pathRenderer.triangle(vv[0], vv[1], vv[2], vv[3], vv[4], vv[5]);
//			}
//		}
//		
//		//draw portals
//		Vector2[][] portals = pathSmoother.buildPortals(path);
//		for (int i = 0; i < portals.length; i++) {
//			portalRenderer.line(portals[i][0].x, portals[i][0].y, portals[i][1].x, portals[i][1].y);
//		}
//		
//		
//		circleRenderer.end();
//		lineRenderer.end();
//		triangleRenderer.end();
//		pathRenderer.end();
//		portalRenderer.end();
//		
//		circleRenderer.dispose();
//		lineRenderer.dispose();
//		
//		portalRenderer.dispose();
//		batch.dispose();
//		font.dispose();
//		triangleRenderer.dispose();
//		pathRenderer.dispose();
//	}
}
