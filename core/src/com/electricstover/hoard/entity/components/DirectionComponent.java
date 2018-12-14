package com.electricstover.hoard.entity.components;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool.Poolable;

public class DirectionComponent implements Poolable, Component {
	public static final int NORTH=8;
	public static final int SOUTH=2;
	public static final int EAST=6;
	public static final int WEST=4;
	public static final int NORTH_EAST=9;
	public static final int SOUTH_EAST=3;
	public static final int NORTH_WEST=7;
	public static final int SOUTH_WEST=1;
//	private static final HashMap<Integer, DirectionComponent> VALUES = new HashMap<Integer, DirectionComponent>();
	private int id;

	public DirectionComponent(int id) {
		this.id = id;
	}
	public void setId(int id) {
		this.id=id;
	}
	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	public int getId() {
		return id;
	}
	public void updateId(float rotation) {
		int[] rotations= {9,8,7,4,1,2,3,6};
		int chopper=(int)(rotation*MathUtils.radiansToDegrees)/360;
		float range=(rotation*MathUtils.radiansToDegrees/360-chopper)*360;
		if(range<0)
			range+=360;
		float degrees=360/rotations.length;
		int direction=(int) (range-(degrees/2)); 
		if(direction>=0)
			id=rotations[direction/(int)degrees];
		else
			id=rotations[rotations.length-1];
	}	

}
