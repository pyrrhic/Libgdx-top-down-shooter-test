package com.mygdx.pathfind;

import java.util.HashMap;

import com.badlogic.gdx.math.Polygon;

public class Node {
	public int x;
	public int y;
	public Polygon polygon;
	public float cost = 9999;
	public Node parent = null;
	
	private HashMap<String, Node> neighbors = new HashMap<String, Node>();
	private String key;
	
	public Node(float x, float y) {
		int intX = Math.round(x);
		int intY = Math.round(y);
		
		this.x = intX;
		this.y = intY;
		key = "" + intX + intY;
	}
	
	public String getKey() {
		return key;
	}
	
	public void addNeighbor(Node node) {
		if (!neighbors.containsKey(node.getKey())) {
			neighbors.put(node.getKey(), node);
		}
	}
	
	public HashMap<String, Node> getNeighbors() {
		return neighbors;
	}
}
