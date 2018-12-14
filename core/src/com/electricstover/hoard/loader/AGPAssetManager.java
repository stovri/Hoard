package com.electricstover.hoard.loader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader.ParticleEffectParameter;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;

public class AGPAssetManager {
	public final AssetManager manager = new AssetManager();
	// Sounds
	public final String jumpSound = "sounds/jumping.wav";
	public final String hitSound = "sounds/hit.wav";

	// Textures
	public final String playerImage = "images/link.png";
	public final String enemyImage = "images/knight.png";

	public final String playingSong = "music/fieldsong.mp3";
	public final String introSong = "music/bass.mp3";

	// Skin
	public final String skin = "skin/uiskin.json";

	// Textures
	public final String gameImages = "images/game.pack";
	public final String loadingImages = "images/loading.pack";

	public final String smokeEffect = "particles/smoke.pe";
	public final String waterEffect = "particles/water.pe";
	public final String fireEffect = "particles/fire.pe";
	public final String fireballEffect = "particles/fireball.pe";

	public void queueAddImages(){
		manager.load(gameImages, TextureAtlas.class);
	}
	public TextureAtlas getImages() {
		return manager.get(gameImages);
	}
	// a small set of images used by the loading screen
	public void queueAddLoadingImages(){
		manager.load(loadingImages, TextureAtlas.class);
	}
	public void queueAddSkin(){
		SkinParameter params = new SkinParameter("skin/uiskin.atlas");
		manager.load(skin, Skin.class, params);
	}

	public void queueAddMusic(){
		manager.load(playingSong, Music.class);
		manager.load(introSong, Music.class);
	}
	public void queueAddSounds(){
		manager.load(jumpSound,Sound.class);
		manager.load(hitSound,Sound.class);
	}
	/*	public void queueAddImages(){
		manager.load(playerImage, Texture.class);
		manager.load(enemyImage, Texture.class);
	}
	public Texture getPlayerTexture() {
		// TODO Auto-generated method stub
		return this.manager.get(playerImage);
	}*/



	public void queueAddParticleEffects(){
		ParticleEffectParameter pep = new ParticleEffectParameter();
		pep.atlasFile = "images/game.pack";
		manager.load(smokeEffect, ParticleEffect.class, pep);
		manager.load(waterEffect, ParticleEffect.class, pep);
		manager.load(fireEffect, ParticleEffect.class, pep);
		manager.load(fireballEffect, ParticleEffect.class, pep);
	}	


	public void queueAddFonts() {
		// TODO Auto-generated method stub

	}
	public AtlasRegion getPlayerTex() {
		// TODO Auto-generated method stub
		return getImages().findRegion("link");
	}
}
