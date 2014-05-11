package com.mygdx.components;

import com.artemis.Component;

public class Player extends Component {
	public State currentState = State.IDLE;
	
	public enum State {
		SHOOTING,
		IDLE
	}
}
