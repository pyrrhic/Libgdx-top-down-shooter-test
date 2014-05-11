package com.mygdx.components;

import com.artemis.Component;

public class Gun extends Component {
	private float shotDelay = .25f; // in seconds, for each shot within a burst
	private int shotsPerBurst;
	private int burstDelay = 0; // in seconds, for bursts
	
	public float timeTillNextBurst = burstDelay;
	public float timeTillNextShot = shotDelay;
	public int shotsLeftInBurst = shotsPerBurst;
	
	public Gun(int burstDelay, int shotsPerBurst) {
		if (burstDelay < (shotDelay * shotsPerBurst)) throw new IllegalArgumentException("Burst rate of fire can not be shorter than total time to shoot a burst.");
		
		this.burstDelay = burstDelay;
		this.shotsPerBurst = shotsPerBurst;
	}
	
	public void reset() {
		timeTillNextBurst = burstDelay;
		timeTillNextShot = shotDelay;
		shotsLeftInBurst = shotsPerBurst;
	}
}
