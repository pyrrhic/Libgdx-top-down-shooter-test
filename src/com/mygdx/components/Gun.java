package com.mygdx.components;

import com.artemis.Component;

public class Gun extends Component {
	private float shotDelay = .05f; // in seconds, for each shot within a burst
	private int shotsPerBurst;
	private float burstDelay = 0; // in seconds, for bursts
	
	public float timeTillNextBurst = 0;
	public float timeTillNextShot = 0;
	public int shotsLeftInBurst;
	
	public Gun(float burstDelay, int shotsPerBurst) {
		//if (burstDelay < (shotDelay * shotsPerBurst)) throw new IllegalArgumentException("Burst rate of fire can not be shorter than total time to shoot a burst.");
		
		this.burstDelay = burstDelay;
		this.shotsPerBurst = shotsPerBurst;
		this.shotsLeftInBurst = shotsPerBurst;
	}
	
	public void reset() {
		timeTillNextBurst = burstDelay;
		timeTillNextShot = shotDelay;
		shotsLeftInBurst = shotsPerBurst;
	}
	
	public float getBurstDelay() {
		return burstDelay;
	}
	
	public float getShotDelay() {
		return shotDelay;
	}
	
	public int getShotsPerBurst() {
		return shotsPerBurst;
	}
}
