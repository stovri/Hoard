package com.electricstover.hoard.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.electricstover.hoard.entity.components.AnimationComponent;
import com.electricstover.hoard.entity.components.DirectionComponent;
import com.electricstover.hoard.entity.components.StateComponent;
import com.electricstover.hoard.entity.components.TextureComponent;

public class AnimationSystem extends IteratingSystem {
	ComponentMapper<TextureComponent> tm;
	ComponentMapper<AnimationComponent> am;
	ComponentMapper<StateComponent> sm;
	ComponentMapper<DirectionComponent> dm;

	@SuppressWarnings("unchecked")
	public AnimationSystem(){
		super(Family.all(TextureComponent.class,
				AnimationComponent.class,
				StateComponent.class).get());

		tm = ComponentMapper.getFor(TextureComponent.class);
		am = ComponentMapper.getFor(AnimationComponent.class);
		sm = ComponentMapper.getFor(StateComponent.class);
		dm = ComponentMapper.getFor(DirectionComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		//Animation tmpAni
		AnimationComponent ani = am.get(entity);
		StateComponent state = sm.get(entity);
		DirectionComponent dir = dm.get(entity);
		if(ani.animations.containsKey(state.get())){
			if(ani.animations.get(state.get()).containsKey(dir.getId())) {
				if(ani.animations.get(state.get()).get(dir.getId()).getKeyFrames().length>0) {
					TextureComponent tex = tm.get(entity);
					tex.region = (TextureRegion) ani.animations.get(state.get()).get(dir.getId()).getKeyFrame(state.time, state.isLooping);
				}
			}
		}

		state.time += deltaTime;
	}
}
