package com.mygdx.components;

import java.util.LinkedList;
import java.util.List;

import com.artemis.Component;
import com.artemis.Entity;

public class Collision extends Component {
	
	private List<Entity> entities;
	
	public Collision() {
		super();
		entities = new LinkedList<Entity>();
	}
	
	public List<Entity> getEntityCollisions() {
		return entities;
	}
}
