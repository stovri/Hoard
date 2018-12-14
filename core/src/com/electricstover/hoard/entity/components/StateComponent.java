package com.electricstover.hoard.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class StateComponent implements Component, Poolable {
	public static final int NONE = 0;
	public static final int IDLE = 1;
	public static final int MOVING = 2;
	public static final int ATTACKING = 3;
	public static final int TAKING_DAMAGE = 4;
	public static final int DEAD = 5;
	public static final int DIEING = 6;
	
	private int state = 0;
    public float time = 0.0f;
    public boolean isLooping = false;

    public void set(int newState){
        state = newState;
        time = 0.0f;
    }

    public int get(){
        return state;
    }

    public Integer[] getStates() {
    	Integer[] retVal= {NONE,IDLE,MOVING,ATTACKING,TAKING_DAMAGE,DEAD,DIEING};
    	return retVal;
    }
	@Override
	public void reset() {
		state = 0;
	    time = 0.0f;
	    isLooping = false;	
	}
}