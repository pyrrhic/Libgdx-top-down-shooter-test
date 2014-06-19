package com.mygdx.components;

import com.artemis.Component;

public class Movable extends Component {
	public float maxXvelocity;
	public float maxYvelocity;
	
	public Movable(float maxXvelocity, float maxYvelocity) {
		this.maxXvelocity = maxXvelocity;
		this.maxYvelocity = maxYvelocity;
	}
}
