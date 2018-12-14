/*
 * 
 */
package com.electricstover.hoard.views;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.electricstover.hoard.HContactListener;
import com.electricstover.hoard.HModel;
import com.electricstover.hoard.Hoard;
import com.electricstover.hoard.BodyFactory;
import com.electricstover.hoard.DFUtils;
import com.electricstover.hoard.LevelFactory;
import com.electricstover.hoard.controller.KeyboardController;
import com.electricstover.hoard.entity.components.B2DBodyComponent;
import com.electricstover.hoard.entity.components.CollisionComponent;
import com.electricstover.hoard.entity.components.PlayerComponent;
import com.electricstover.hoard.entity.components.StateComponent;
import com.electricstover.hoard.entity.components.TextureComponent;
import com.electricstover.hoard.entity.components.TransformComponent;
import com.electricstover.hoard.entity.components.TypeComponent;
import com.electricstover.hoard.entity.systems.AnimationSystem;
import com.electricstover.hoard.entity.systems.BulletSystem;
import com.electricstover.hoard.entity.systems.CollisionSystem;
import com.electricstover.hoard.entity.systems.EnemySystem;
import com.electricstover.hoard.entity.systems.LevelGenerationSystem;
import com.electricstover.hoard.entity.systems.ParticleEffectSystem;
import com.electricstover.hoard.entity.systems.PhysicsDebugSystem;
import com.electricstover.hoard.entity.systems.PhysicsSystem;
import com.electricstover.hoard.entity.systems.PlayerControlSystem;
import com.electricstover.hoard.entity.systems.RenderingSystem;
import com.electricstover.hoard.entity.systems.WallSystem;
import com.electricstover.hoard.entity.systems.WaterFloorSystem;

// TODO: Auto-generated Javadoc
/**
 * The Class MainScreen.
 */
public class MainScreen implements Screen {
	private Hoard parent;
	private OrthographicCamera cam;
	private KeyboardController controller;
	private SpriteBatch sb;
	private PooledEngine engine;
	private LevelFactory lvlFactory;

	private Sound ping;
	private Sound boing;
	private TextureAtlas atlas;
	private Entity player;	


	public MainScreen(Hoard agp) {
		parent = agp;
		parent.assetMan.queueAddSounds();
		parent.assetMan.manager.finishLoading();
		atlas = parent.assetMan.manager.get("images/game.pack", TextureAtlas.class);
		ping = parent.assetMan.manager.get("sounds/hit.wav",Sound.class);
		boing = parent.assetMan.manager.get("sounds/jumping.wav",Sound.class);
		controller = new KeyboardController();
		engine = new PooledEngine();
		lvlFactory = new LevelFactory(engine,parent.assetMan);

		sb = new SpriteBatch();
		RenderingSystem renderingSystem = new RenderingSystem(sb);
		cam = renderingSystem.getCamera();
		ParticleEffectSystem particleSystem = new ParticleEffectSystem(sb,cam);
		sb.setProjectionMatrix(cam.combined);

		engine.addSystem(new AnimationSystem());
		engine.addSystem(new PhysicsSystem(lvlFactory.world, engine));
		engine.addSystem(renderingSystem);
		engine.addSystem(particleSystem); // particles get drawn on top so should be placed after normal rendering
		engine.addSystem(new PhysicsDebugSystem(lvlFactory.world, renderingSystem.getCamera()));
		engine.addSystem(new CollisionSystem());
		engine.addSystem(new PlayerControlSystem(controller, lvlFactory));
		engine.addSystem(new EnemySystem());
		player = lvlFactory.createPlayer(atlas.findRegion("dragon"),cam);
		engine.addSystem(new BulletSystem(player));
		engine.addSystem(new WallSystem(player));
		//engine.addSystem(new WaterFloorSystem(player));
		engine.addSystem(new LevelGenerationSystem(lvlFactory));

		int floorWidth = (int) (40*RenderingSystem.PPM);
		int floorHeight = (int) (1*RenderingSystem.PPM);
		TextureRegion floorRegion = DFUtils.makeTextureRegion(floorWidth, floorHeight, "11331180");
		lvlFactory.createFloor(floorRegion);

		int wFloorWidth = (int) (40*RenderingSystem.PPM);
		int wFloorHeight = (int) (10*RenderingSystem.PPM);
		TextureRegion wFloorRegion = DFUtils.makeTextureRegion(wFloorWidth, wFloorHeight, "11113380");
		lvlFactory.createWaterFloor(wFloorRegion);


		int wallWidth = (int) (1*RenderingSystem.PPM);
		int wallHeight = (int) (60*RenderingSystem.PPM);
		TextureRegion wallRegion = DFUtils.makeTextureRegion(wallWidth, wallHeight, "222222FF");
		lvlFactory.createWalls(wallRegion);   
	}


	@Override
	public void show() {
		Gdx.input.setInputProcessor(controller);	
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		engine.update(delta);

		//check if player is dead. if so show end screen
		if((player.getComponent(PlayerComponent.class)).isDead){
			DFUtils.log("YOU DIED : back to menu you go!");
			parent.changeScreen(Hoard.ENDGAME );	
		}

	}
	@Override
	public void resize(int width, int height) {		
	}

	@Override
	public void pause() {		
	}

	@Override
	public void resume() {		
	}

	@Override
	public void hide() {		
	}

	@Override
	public void dispose() {
	}
}
