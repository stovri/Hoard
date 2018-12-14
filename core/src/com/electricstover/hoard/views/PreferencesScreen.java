/*
 * 
 */
package com.electricstover.hoard.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.electricstover.hoard.Hoard;

// TODO: Auto-generated Javadoc
/**
 * The Class PreferencesScreen.
 */
public class PreferencesScreen implements Screen{

	/** The parent - a field to store our ochestrator. */
	private Hoard parent; // a field to store our orchestrator
	private Stage stage;
	// our new fields
	private Label titleLabel; 
	private Label volumeMusicLabel;
	private Label volumeSoundLabel;
	private Label musicOnOffLabel;
	private Label soundOnOffLabel;	

	/**
	 * Instantiates a new preferences screen.
	 *
	 * @param agp the agp
	 */
	// our constructor with an Hoard argument
	public PreferencesScreen(Hoard agp){
		parent = agp;     // setting the argument to our field.
		stage = new Stage(new ScreenViewport());
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		// Create a table that fills the screen. Everything else will go inside this table.
		Table table = new Table();
		table.setFillParent(true);
		table.setDebug(false);
		stage.addActor(table);
		Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

		titleLabel = new Label( "Preferences", skin );
		volumeMusicLabel = new Label( "Music Volume", skin );
		volumeSoundLabel = new Label( "Sound Effects Volume", skin );
		musicOnOffLabel = new Label( "Music", skin );
		soundOnOffLabel = new Label( "Sound", skin );

		//music volume
		final Slider volumeMusicSlider = new Slider( 0f, 1f, 0.1f,false, skin );
		volumeMusicSlider.setValue( parent.getPreferences().getMusicVolume() );
		volumeMusicSlider.addListener( new EventListener() {
			@Override
			public boolean handle(Event event) {
				parent.getPreferences().setMusicVolume( volumeMusicSlider.getValue() );
				return false;
			}
		});

		//sound volume
		final Slider volumeSoundSlider = new Slider( 0f, 1f, 0.1f,false, skin );
		volumeSoundSlider.setValue( parent.getPreferences().getSoundVolume() );
		volumeSoundSlider.addListener( new EventListener() {
			@Override
			public boolean handle(Event event) {
				parent.getPreferences().setSoundVolume( volumeSoundSlider.getValue() );
				return false;
			}
		});
		//music
		final CheckBox musicCheckbox = new CheckBox(null, skin);
		musicCheckbox.setChecked( parent.getPreferences().isMusicEnabled() );
		musicCheckbox.addListener( new EventListener() {
			@Override
			public boolean handle(Event event) {
				boolean enabled = musicCheckbox.isChecked();
				parent.getPreferences().setMusicEnabled( enabled );
				return false;
			}
		});
		//sound
		final CheckBox soundCheckbox = new CheckBox(null, skin);
		soundCheckbox.setChecked( parent.getPreferences().isSoundEffectsEnabled() );
		soundCheckbox.addListener( new EventListener() {
			@Override
			public boolean handle(Event event) {
				boolean enabled = soundCheckbox.isChecked();
				parent.getPreferences().setSoundEffectsEnabled( enabled );
				return false;
			}
		});
		// return to main screen button
		//final TextButton backButton = new TextButton("Back", skin, "small"); // the extra argument here "small" is used to set the button to the smaller version instead of the big default version
		final TextButton backButton = new TextButton("Back", skin);
		backButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.changeScreen(Hoard.MENU);
			}
		});
		table.add(titleLabel).colspan(2);
		table.row().pad(10,0,0,10);
		table.add(volumeMusicLabel).left();
		table.add(volumeMusicSlider);
		table.row().pad(10,0,0,10);
		table.add(musicOnOffLabel).left();
		table.add(musicCheckbox);
		table.row().pad(10,0,0,10);
		table.add(volumeSoundLabel).left();
		table.add(volumeSoundSlider);
		table.row().pad(10,0,0,10);
		table.add(soundOnOffLabel).left();
		table.add(soundCheckbox);
		table.row().pad(10,0,0,10);
		table.add(backButton).colspan(2);	
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
