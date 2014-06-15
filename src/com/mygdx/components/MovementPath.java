package com.mygdx.components;

import java.util.List;

import com.artemis.Component;
import com.mygdx.pathfind.Node;

public class MovementPath extends Component {
	private List<Node> path;
	
	public MovementPath(List<Node> path) {
		this.path = path;
	}
	
	public List<Node> getPath() {
		return path;
	}
}
