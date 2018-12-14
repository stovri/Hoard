package com.electricstover.hoard.views;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LoadingBarPart extends Actor {

	private AtlasRegion image;
	private Animation flameAnimation;
	private AtlasRegion currentFrame;
	private float stateTime;
	public LoadingBarPart() {
		// TODO Auto-generated constructor stub
	}
	public LoadingBarPart(AtlasRegion ar, Animation flameAnimation2) {
		super();
		image = ar;
		flameAnimation = flameAnimation2;
		this.setWidth(30);
		this.setHeight(25);
		this.setVisible(false);
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(image, getX(),getY(), 30, 30);
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		batch.draw(currentFrame, getX()-5,getY(), 40, 40);
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta; // Accumulate elapsed animation time
		currentFrame = (AtlasRegion)flameAnimation.getKeyFrame(stateTime, true);
	}


}
