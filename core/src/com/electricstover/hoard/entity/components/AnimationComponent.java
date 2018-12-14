package com.electricstover.hoard.entity.components;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool.Poolable;

public class AnimationComponent implements Component, Poolable {
//    public IntMap<Animation> animations = new IntMap<Animation>();
	 public HashMap<Integer, HashMap<Integer, Animation>> animations=new HashMap<Integer, HashMap<Integer, Animation>>();
	@Override
	public void reset() {
		animations = new HashMap<Integer, HashMap<Integer, Animation>>();
	}
	public void set(Integer sc, HashMap<Integer, Animation> da) {
		animations.put(sc, da);
	}
}