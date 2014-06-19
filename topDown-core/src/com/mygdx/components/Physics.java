package com.mygdx.components;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class Physics extends Component{
	public Body body;
	
	public Physics(Body body) {
		this.body = body;
	}
}
