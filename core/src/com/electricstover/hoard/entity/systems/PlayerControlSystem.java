package com.electricstover.hoard.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.electricstover.hoard.LevelFactory;
import com.electricstover.hoard.controller.KeyboardController;
import com.electricstover.hoard.entity.components.AnimationComponent;
import com.electricstover.hoard.entity.components.B2DBodyComponent;
import com.electricstover.hoard.entity.components.DirectionComponent;
import com.electricstover.hoard.entity.components.PlayerComponent;
import com.electricstover.hoard.entity.components.StateComponent;
import com.electricstover.hoard.entity.components.TransformComponent;

public class PlayerControlSystem extends IteratingSystem{

	private LevelFactory lvlFactory;
	ComponentMapper<PlayerComponent> pm;
	ComponentMapper<B2DBodyComponent> bodm;
	ComponentMapper<StateComponent> sm;
	ComponentMapper<TransformComponent> tm;
	KeyboardController controller;
	ComponentMapper<AnimationComponent> am;
	private ComponentMapper<DirectionComponent> dm;


	@SuppressWarnings("unchecked")
	public PlayerControlSystem(KeyboardController keyCon, LevelFactory lvlf) {
		super(Family.all(PlayerComponent.class).get());
		controller = keyCon;
		lvlFactory = lvlf;
		pm = ComponentMapper.getFor(PlayerComponent.class);
		bodm = ComponentMapper.getFor(B2DBodyComponent.class);
		sm = ComponentMapper.getFor(StateComponent.class);
		tm=ComponentMapper.getFor(TransformComponent.class);
		am=ComponentMapper.getFor(AnimationComponent.class);
		dm=ComponentMapper.getFor(DirectionComponent.class);
	}
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		B2DBodyComponent b2body = bodm.get(entity);
		StateComponent state = sm.get(entity);
		PlayerComponent player = pm.get(entity);
		DirectionComponent dirCom = dm.get(entity);
		AnimationComponent aniCom = am.get(entity);
		dirCom.updateId(b2body.body.getAngle());
		player.cam.position.y = b2body.body.getPosition().y;
		player.cam.position.x = b2body.body.getPosition().x;
		int theState=state.get();
		switch(theState) {
		case StateComponent.ATTACKING:
			if(aniCom.animations.get(state.get()).get(dirCom.getId()).isAnimationFinished(state.time))
				state.set(StateComponent.IDLE);
			break;
		case StateComponent.DIEING:
			if(aniCom.animations.get(state.get()).get(dirCom.getId()).isAnimationFinished(state.time))
				state.set(StateComponent.DEAD);
			break;
		case StateComponent.DEAD:
			player.isDead=true;
			b2body.isDead=true;
		default:
			checkControls(player,b2body,state);
		}

	}
	private void checkControls(PlayerComponent player, B2DBodyComponent b2body, StateComponent state) {
		// TODO Auto-generated method stub
		if(controller.left){
			b2body.body.applyAngularImpulse(0.05f, true);
		}
		if(controller.right){
			b2body.body.applyAngularImpulse(-0.05f, true);
		}

		if(controller.up){
			Vector2 direction=new Vector2(1f,0f);
			direction.setAngle(b2body.body.getAngle()*MathUtils.radDeg);
			b2body.body.applyLinearImpulse(direction.x, direction.y, b2body.body.getWorldCenter().x,b2body.body.getWorldCenter().y, true);
		}

		if(controller.down){
			Vector2 direction=new Vector2(-1,0);
			direction.setAngle(b2body.body.getAngle()*MathUtils.radDeg);
			direction.rotate(180f);
			b2body.body.applyLinearImpulse(direction.x,direction.y, b2body.body.getWorldCenter().x,b2body.body.getWorldCenter().y, true);
		}

		if(controller.isMouse1Down){ // if mouse button is pressed
			// user wants to fire
			//player can shoot so do player shoot
			float speed = 3f;  // set the speed of the bullet
			float shooterX = b2body.body.getPosition().x; // get player location
			float shooterY = b2body.body.getPosition().y; // get player location
			float shooterA = b2body.body.getAngle()*MathUtils.radDeg;
			Vector2 vect=new Vector2(speed,0);
			vect.setAngle(shooterA);
			// create a bullet
			lvlFactory.createBullet(shooterX, shooterY, vect.x, vect.y);
			vect.rotate(180f);
			b2body.body.applyLinearImpulse(vect.x,vect.y, b2body.body.getWorldCenter().x,b2body.body.getWorldCenter().y, true);

			//reset timeSinceLastShot
			state.set(StateComponent.ATTACKING);
		}
	}
}
