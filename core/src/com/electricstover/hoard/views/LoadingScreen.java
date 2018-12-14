/*
 * 
 */
package com.electricstover.hoard.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.electricstover.hoard.Hoard;

/**
 * The Class LoadingScreen. Provides a screen while the game assets are loaded
 */
public class LoadingScreen implements Screen {

	private static final int IMAGE = 0;
	private static final int FONT = 1;
	private static final int PARTY = 2;
	private static final int SOUND = 3;
	private static final int MUSIC = 4;
	/** The parent - a field to store our ochestrator. */
	private Hoard parent; // a field to store our orchestrator
	private TextureAtlas atlas;
	private AtlasRegion title;
	private AtlasRegion dash;
	private int currentLoadingStage=0;
	private float countDown=5f;
	private Animation<TextureRegion> flameAnimation;
	private Stage stage;
	private Image titleImage;
	private Table table;
	private Table loadingTable;
	private AtlasRegion background;
	private AtlasRegion copyright;
	private Image copyrightImage;

	// our constructor with an Hoard argument
	/**
	 * Instantiates a new loading screen.
	 *
	 * @param agp the orchestrator
	 */
	public LoadingScreen(Hoard agp){
		parent = agp;     // setting the argument to our field.
		stage = new Stage(new ScreenViewport());

		loadAssets();
		System.out.println("Load Images here:");
		// initiate queueing of images but don't start loading
		parent.assetMan.queueAddImages();
		System.out.println("Loading images....");
	}
	public void show() {

		titleImage = new Image(title);
		copyrightImage = new Image(copyright);
		table = new Table();
		table.setFillParent(true);
		table.setDebug(false);
		table.setBackground(new TiledDrawable(background));
		loadingTable = new Table();
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));


		table.add(titleImage).align(Align.center).pad(10, 0, 0, 0).colspan(10); 
		table.row(); // move to next row
		table.add(loadingTable).width(400);
		table.row();
		table.add(copyrightImage).align(Align.center).pad(200, 0, 0, 0).colspan(10);
		stage.addActor(table);		
	}
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (parent.assetMan.manager.update()) { // Load some, will return true if done loading
			currentLoadingStage+= 1;
			if(currentLoadingStage <= 5){
				loadingTable.getCells().get((currentLoadingStage-1)*2).getActor().setVisible(true);  // new
				loadingTable.getCells().get((currentLoadingStage-1)*2+1).getActor().setVisible(true); 
			}
			switch(currentLoadingStage){
			case FONT:
				System.out.println("Loading fonts....");
				parent.assetMan.queueAddFonts();
				break;
			case PARTY:	
				System.out.println("Loading Particle Effects....");
				parent.assetMan.queueAddParticleEffects();
				break;
			case SOUND:
				System.out.println("Loading Sounds....");
				parent.assetMan.queueAddSounds();
				break;
			case MUSIC:
				System.out.println("Loading fonts....");
				parent.assetMan.queueAddMusic();
				break;
			case 5:	
				System.out.println("Finished");
				break;
			}
			if (currentLoadingStage >5){
				countDown -= delta;
				currentLoadingStage = 5;
				if(countDown < 0){
					parent.changeScreen(Hoard.MENU);
				}
			}
		}

		stage.act();
		stage.draw();
	}
	private void loadAssets() {
		// load loading images and wait until finished
		parent.assetMan.queueAddLoadingImages();
		parent.assetMan.manager.finishLoading();

		// get images used to display loading progress
		atlas = parent.assetMan.manager.get(parent.assetMan.loadingImages);
		title = atlas.findRegion("staying-alight-logo");
		dash = atlas.findRegion("loading-dash");
		flameAnimation = new Animation<TextureRegion>(0.07f, atlas.findRegions("flames/flames"), PlayMode.LOOP);
		background = atlas.findRegion("flamebackground");
		copyright = atlas.findRegion("copyright");
	}
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}
}