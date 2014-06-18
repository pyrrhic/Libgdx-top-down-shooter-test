package com.mygdx.components;

import java.util.List;

import com.artemis.Component;
import com.mygdx.pathfind.Node;

public class MovementPath extends Component {
	private List<Node> path;
	private Node currentNode;
	
	public MovementPath(List<Node> path) {
		if (path.size() < 1) throw new IllegalArgumentException("path.size() must be greater than 1.");
		
		this.path = path;
		setCurrentNodeToNextNode();
	}
	
	public List<Node> getPath() {
		return path;
	}
	
	public Node getCurrentNode() {
		return currentNode;	
	}
	
	public void setCurrentNodeToNextNode() {
		if (path.size() > 0) {
			int index = path.size() - 1;
			currentNode = path.get(index);
			path.remove(index);
		}
		else {
			currentNode = null;
		}
	}
}
