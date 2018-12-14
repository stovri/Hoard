package com.electricstover.hoard.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.electricstover.hoard.entity.components.B2DBodyComponent;
import com.electricstover.hoard.entity.components.BulletComponent;
import com.electricstover.hoard.entity.components.Mapper;

public class BulletSystem extends IteratingSystem {
	private Entity player;

	@SuppressWarnings("unchecked")
	public BulletSystem(Entity player){
		super(Family.all(BulletComponent.class).get());
		this.player = player;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		//get box 2d body and bullet components
		B2DBodyComponent b2body = Mapper.b2dCom.get(entity);
		BulletComponent bullet = Mapper.bulletCom.get(entity);
		// apply bullet velocity to bullet body
		// get player pos
		B2DBodyComponent playerBodyComp = Mapper.b2dCom.get(player); 
		float px = playerBodyComp.body.getPosition().x;
		float py = playerBodyComp.body.getPosition().y;

		//get bullet pos
		float bx = b2body.body.getPosition().x;
		float by = b2body.body.getPosition().y;

		// if bullet is 20 units away from player on any axis then it is probably off screen
		if(bx - px > 20 || by - py > 20){ 
			bullet.isDead = true;
		}

		//check if bullet is dead
		//check if bullet is dead
		if(bullet.isDead){
			//System.out.println("Bullet died");
			if(Mapper.peCom.get(bullet.particleEffect)!=null) {
				Mapper.peCom.get(bullet.particleEffect).isDead = true;
				Mapper.peCom.get(bullet.particleEffect).timeTilDeath=0f;
			}
			b2body.isDead = true;
		}
	}
}
