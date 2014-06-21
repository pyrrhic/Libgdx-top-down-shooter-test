package com.mygdx.pathfind;

import java.util.ArrayList;
import java.util.List;

public class PathFinder {		
	public PathFinder() {
	}
	
	public List<Node> findPath(Node startNode, Node goalNode) {		
		if (startNode.x == goalNode.x && startNode.y == goalNode.y) {
			return new ArrayList<Node>();
		}
		
		ArrayList<Node> reachable = new ArrayList<Node>();
		ArrayList<Node> explored = new ArrayList<Node>();
		
		startNode.cost = 1;
		reachable.add(startNode);
		
		while (reachable.size() > 0) {
			Node node = chooseNode(reachable);
			
			reachable.remove(node);
			explored.add(node);
			
			List<Node> newReachable = getUnexploredNeighbors(node, explored);
			for (Node newNode : newReachable) {
				if (!reachable.contains(newNode)) {
					reachable.add(newNode);
				}
				
				float tempCost = node.cost + calculateCost(node, newNode);
				if (tempCost < newNode.cost) {
					newNode.parent = node;
					newNode.cost = tempCost;
				}
			}
			
			if (node.getKey().equals(goalNode.getKey())) {
				return buildPath(goalNode);
			}
		}
		
		return new ArrayList<Node>();
	}
	
	private float calculateCost(Node startNode, Node endNode) {		
		return Math.abs(startNode.x - endNode.x) + Math.abs(startNode.y - endNode.y);
	}
	
	private List<Node> getUnexploredNeighbors(Node node, List<Node> explored) {
		ArrayList<Node> unexploredNeighbors = new ArrayList<Node>();
		
		for (Node neighbor : node.getNeighbors().values()) {
			if (!doesListContainNode(neighbor, explored)) {
				unexploredNeighbors.add(neighbor);
			}	
		}
		
		return unexploredNeighbors;
	}
	
	private boolean doesListContainNode(Node node, List<Node> listOfNodes) {
		String key = node.getKey();
		
		for(int i = 0; i < listOfNodes.size(); i++) {
			if (key.equals(listOfNodes.get(i).getKey())) {
				return true;
			}
		}
		
		return false;
	}
	
	private Node chooseNode(ArrayList<Node> reachable) {
		float minCost = 99999;
		Node bestNode = null;
		
		for (Node node : reachable) {			
			if (node.cost < minCost) {
				minCost = node.cost;
				bestNode = node;
			}
		}
		
		return bestNode;
	}
	
	private List<Node> buildPath(Node goalNode) {
		ArrayList<Node> path = new ArrayList<Node>();
		path.add(goalNode);
		Node parent = goalNode.parent;
		
		while (parent != null) {
			path.add(parent);
			parent = parent.parent;
		}
		
		return path;
	}
}
