package com.mygdx.components;

import com.artemis.Component;

public class Size extends Component {
	private int width;
	private int height;
	
	public Size(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
