package com.mygdx.pathfind;

import java.util.HashMap;
import java.util.Set;

public class NodeList {
	private HashMap<String, Node> nodes;
	
	public NodeList(int initialCapacity) {
		nodes = new HashMap<String, Node>(initialCapacity);
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
	
	public HashMap<String, Node> getNodes() {
		return nodes;
	}
}
