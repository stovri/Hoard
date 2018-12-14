/*
 * 
 */
package com.electricstover.hoard.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.electricstover.hoard.Hoard;

// TODO: Auto-generated Javadoc
/**
 * The Class MenuScreen. Provides 
 */
public class MenuScreen implements Screen {

	/** The parent - a field to store our ochestrator. */
	private Hoard parent; // a field to store our orchestrator
	private Stage stage;
	private Skin skin;
	private TextureAtlas atlas;
	private AtlasRegion background;


	/**
	 * Instantiates a new menu screen.
	 *
	 * @param agp the agp
	 */
	// our constructor with an Hoard argument
	public MenuScreen(Hoard agp){
		parent = agp;     // setting the argument to our field.
		stage = new Stage(new ScreenViewport());
		parent.assetMan.queueAddSkin();  //new
		parent.assetMan.manager.finishLoading(); // new
		skin = parent.assetMan.manager.get(parent.assetMan.skin); // new	

	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		// Create a table that fills the screen. Everything else will go inside this table.
		Table table = new Table();
		table.setFillParent(true);
		table.setDebug(false);
		stage.addActor(table);
		atlas = parent.assetMan.manager.get("images/loading.pack");
		background = atlas.findRegion("flamebackground");
		table.setBackground(new TiledDrawable(background));
		TextButton newGame = new TextButton("New Game", skin);
		TextButton preferences = new TextButton("Preferences", skin);
		TextButton exit = new TextButton("Exit", skin);

		table.add(newGame).fillX().uniformX();
		table.row().pad(10, 0, 10, 0);
		table.add(preferences).fillX().uniformX();
		table.row();
		table.add(exit).fillX().uniformX();

		exit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();				
			}
		});
		newGame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.changeScreen(Hoard.APPLICATION);
			}
		});
		preferences.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.changeScreen(Hoard.PREFERENCES);
			}
		});
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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
		stage.dispose();
	}

}
