package com.electricstover.hoard;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.electricstover.hoard.loader.AGPAssetManager;
import com.electricstover.hoard.views.EndScreen;
import com.electricstover.hoard.views.LoadingScreen;
import com.electricstover.hoard.views.MainScreen;
import com.electricstover.hoard.views.MenuScreen;
import com.electricstover.hoard.views.PreferencesScreen;

// TODO: Auto-generated Javadoc
public class Hoard extends Game{
	
	/** The loading screen. */
	private LoadingScreen loadingScreen;
	
	/** The preferences screen. */
	private PreferencesScreen preferencesScreen;
	
	/** The menu screen. */
	private MenuScreen menuScreen;
	
	/** The main screen. */
	private MainScreen mainScreen;
	
	/** The end screen. */
	private EndScreen endScreen;
	private AppPreferences prefs;
	public AGPAssetManager assetMan;

	private Music introSong;

	public int lastScore =0;

	/** The Constant MENU. Used to select the menu screen. */
	public final static int MENU = 0;
	
	/** The Constant PREFERENCES. Used to select the preferences screen. */
	public final static int PREFERENCES = 1;
	
	/** The Constant APPLICATION. Used to select the main screen. */
	public final static int APPLICATION = 2;
	
	/** The Constant ENDGAME. Used to select the end screen. */
	public final static int ENDGAME = 3;
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create() {

		prefs = new AppPreferences();
		assetMan = new AGPAssetManager();
		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
		// tells our asset manger that we want to load the images set in loadImages method
		assetMan.queueAddSkin();
		assetMan.queueAddSounds();
		assetMan.queueAddImages();
		assetMan.queueAddMusic();
		// tells the asset manager to load the images and wait until finished loading.
		assetMan.manager.finishLoading();
		// loads the 2 sounds we use
		introSong = assetMan.manager.get(assetMan.introSong);
		
		introSong.play();
		introSong.setLooping(true);
	}
	
	/**
	 * Change screen.
	 *
	 * @param screen - The screen to switch to as defined in the constants.
	 */
	public void changeScreen(int screen){
		switch(screen){
			case MENU:
				if(menuScreen == null) menuScreen = new MenuScreen(this); // added (this)
				this.setScreen(menuScreen);
				break;
			case PREFERENCES:
				if(preferencesScreen == null) preferencesScreen = new PreferencesScreen(this); // added (this)
				this.setScreen(preferencesScreen);
				break;
			case APPLICATION:
				if(mainScreen == null) mainScreen = new MainScreen(this); //added (this)
				this.setScreen(mainScreen);
				break;
			case ENDGAME:
				if(endScreen == null) endScreen = new EndScreen(this);  // added (this)
				this.setScreen(endScreen);
				break;
		}
	}

	public AppPreferences getPreferences() {
		// TODO Auto-generated method stub
		return this.prefs;
	}
	@Override
	public void dispose(){
		introSong.dispose();
		assetMan.manager.dispose();
	}
}