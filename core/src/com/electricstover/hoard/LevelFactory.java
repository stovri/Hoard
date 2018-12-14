package com.electricstover.hoard;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.electricstover.hoard.entity.components.AnimationComponent;
import com.electricstover.hoard.entity.components.B2DBodyComponent;
import com.electricstover.hoard.entity.components.BulletComponent;
import com.electricstover.hoard.entity.components.CollisionComponent;
import com.electricstover.hoard.entity.components.DirectionComponent;
import com.electricstover.hoard.entity.components.EnemyComponent;
import com.electricstover.hoard.entity.components.ParticleEffectComponent;
import com.electricstover.hoard.entity.components.PlayerComponent;
import com.electricstover.hoard.entity.components.StateComponent;
import com.electricstover.hoard.entity.components.TextureComponent;
import com.electricstover.hoard.entity.components.TransformComponent;
import com.electricstover.hoard.entity.components.TypeComponent;
import com.electricstover.hoard.entity.components.WallComponent;
import com.electricstover.hoard.entity.components.WaterFloorComponent;
import com.electricstover.hoard.entity.systems.RenderingSystem;
import com.electricstover.hoard.loader.AGPAssetManager;
import com.electricstover.hoard.simplexnoise.OpenSimplexNoise;
import com.electricstover.hoard.simplexnoise.SimplexNoise;

public class LevelFactory {
	private BodyFactory bodyFactory;
	public World world;
	private PooledEngine engine;
	public int currentLevel = 0;
	private TextureRegion floorTex;
	private TextureAtlas atlas;
	private TextureRegion enemyTex;
	private TextureRegion platformTex;
	private TextureRegion bulletTex;
	private OpenSimplexNoise openSim;
	private ParticleEffectManager pem;

	public LevelFactory(PooledEngine en, AGPAssetManager assetMan){
		engine = en;
		this.atlas = assetMan.getImages();
		floorTex = DFUtils.makeTextureRegion(40*RenderingSystem.PPM, 0.5f*RenderingSystem.PPM, "111111FF");
		enemyTex = DFUtils.makeTextureRegion(1*RenderingSystem.PPM,1*RenderingSystem.PPM, "331111FF");
		bulletTex = DFUtils.makeTextureRegion(10,10,"444444FF");
		platformTex = DFUtils.makeTextureRegion(2*RenderingSystem.PPM, 0.1f*RenderingSystem.PPM, "221122FF");
		world = new World(new Vector2(0,0), true);
		world.setContactListener(new HContactListener());
		bodyFactory = BodyFactory.getInstance(world);
		openSim = new OpenSimplexNoise(MathUtils.random(2000l));
		pem = new ParticleEffectManager();
		pem.addParticleEffect(ParticleEffectManager.FIRE, assetMan.manager.get("particles/fire.pe",ParticleEffect.class),1f/64f);
		pem.addParticleEffect(ParticleEffectManager.WATER, assetMan.manager.get("particles/water.pe",ParticleEffect.class),1f/64f);
		pem.addParticleEffect(ParticleEffectManager.SMOKE, assetMan.manager.get("particles/smoke.pe",ParticleEffect.class),1f/64f);
		pem.addParticleEffect(ParticleEffectManager.FIREBALL, assetMan.manager.get("particles/fireball.pe",ParticleEffect.class),1f/64f);
	}


	/** Creates a pair of platforms per level up to yLevel
	 * @param ylevel
	 */
	public void generateLevel(int ylevel){
		while(ylevel > currentLevel){
			int range = 15;
			for(int i = 1; i < 5; i ++){
				generateSingleColumn(genNForL(i * 1,currentLevel)
						,genNForL(i * 100,currentLevel)
						,genNForL(i * 200,currentLevel)
						,genNForL(i * 300,currentLevel)
						,range,i * 10);
			}
			currentLevel++;
		}
	}

	// generate noise for level
	private float genNForL(int level, int height){
		return (float)openSim.eval(height, level);
	}

	private void generateSingleColumn(float n1, float n2,float n3,float n4, int range, int offset){
		if(n1 > -0.8f){
			createPlatform(n2 * range + offset ,currentLevel * 2);
			if(n3 > 0.3f){
				// add bouncy platform
				createBouncyPlatform(n2 * range + offset,currentLevel * 2);
			}
			if(n4 > 0.2f){
				// add an enemy
				createEnemy(enemyTex,n2 * range + offset,currentLevel * 2 + 1);
			}
		}
	}

	public Entity createBouncyPlatform(float x, float y){
		Entity entity = engine.createEntity();
		// create body component
		B2DBodyComponent b2dbody = engine.createComponent(B2DBodyComponent.class);
		b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, .5f, 0.5f, BodyFactory.STONE, BodyType.StaticBody);
		//make it a sensor so not to impede movement
		bodyFactory.makeAllFixturesSensors(b2dbody.body);

		// give it a texture..todo get another texture and anim for springy action
		TextureComponent texture = engine.createComponent(TextureComponent.class);
		texture.region = floorTex;

		TypeComponent type = engine.createComponent(TypeComponent.class);
		type.type = TypeComponent.SPRING;

		b2dbody.body.setUserData(entity);
		entity.add(b2dbody);
		entity.add(texture);
		entity.add(type);
		engine.addEntity(entity);

		return entity;
	}
	public void createPlatform(float x, float y){
		Entity entity = engine.createEntity();
		B2DBodyComponent b2dbody = engine.createComponent(B2DBodyComponent.class);
		b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, 1.5f, 0.2f, BodyFactory.STONE, BodyType.StaticBody);
		TextureComponent texture = engine.createComponent(TextureComponent.class);
		texture.region = floorTex;
		TypeComponent type = engine.createComponent(TypeComponent.class);
		type.type = TypeComponent.SCENERY;
		b2dbody.body.setUserData(entity);
		entity.add(b2dbody);
		entity.add(texture);
		entity.add(type);
		engine.addEntity(entity);

	}

	public void createFloor(TextureRegion tex){
		Entity entity = engine.createEntity();
		B2DBodyComponent b2dbody = engine.createComponent(B2DBodyComponent.class);
		b2dbody.body = bodyFactory.makeBoxPolyBody(0, 0, 100, 10, BodyFactory.STONE, BodyType.StaticBody);
		TextureComponent texture = engine.createComponent(TextureComponent.class);
		texture.region = tex;
		TypeComponent type = engine.createComponent(TypeComponent.class);
		type.type = TypeComponent.SCENERY;

		b2dbody.body.setUserData(entity);

		entity.add(b2dbody);
		entity.add(texture);
		entity.add(type);

		engine.addEntity(entity);
	}

	public Entity createPlayer(TextureRegion tex, OrthographicCamera cam){

		Entity entity = engine.createEntity();
		B2DBodyComponent b2dbody = engine.createComponent(B2DBodyComponent.class);
		TransformComponent position = engine.createComponent(TransformComponent.class);
		TextureComponent texture = engine.createComponent(TextureComponent.class);
		PlayerComponent player = engine.createComponent(PlayerComponent.class);
		CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
		TypeComponent type = engine.createComponent(TypeComponent.class);
		StateComponent stateCom = engine.createComponent(StateComponent.class);
		AnimationComponent animCom = engine.createComponent(AnimationComponent.class);


		player.cam = cam;
		b2dbody.body = bodyFactory.makeCirclePolyBody(10,1,1, BodyFactory.STONE, BodyType.DynamicBody,false);
		// set object position (x,y,z) z used to define draw order 0 first drawn
		position.position.set(10,1,0);
		texture.region = tex;
		type.type = TypeComponent.PLAYER;
		stateCom.set(StateComponent.IDLE);
		b2dbody.body.setUserData(entity);
		String[] idleAnimationNames= {"DragonNE","DragonN","DragonNW","DragonW","DragonSW","DragonS","DragonSE","DragonE"};
		String[] attackingAnimationNames= {"DragonFireNE","DragonFireN","DragonFireNW","DragonFireW","DragonFireSW","DragonFireS","DragonFireSE","DragonFireE"};
		String deadAnimationName="DragonDie";
		DirectionComponent[] dirCom= {new DirectionComponent(DirectionComponent.NORTH_EAST),
				new DirectionComponent(DirectionComponent.NORTH),
				new DirectionComponent(DirectionComponent.NORTH_WEST),
				new DirectionComponent(DirectionComponent.WEST),
				new DirectionComponent(DirectionComponent.SOUTH_WEST),
				new DirectionComponent(DirectionComponent.SOUTH),
				new DirectionComponent(DirectionComponent.SOUTH_EAST),
				new DirectionComponent(DirectionComponent.EAST)};
		HashMap <Integer, Animation> da=new HashMap <Integer, Animation>();
		for(int i=0;i<idleAnimationNames.length;i++) {
			Animation anim = new Animation(0.2f,atlas.findRegions(idleAnimationNames[5]));
			anim.setPlayMode(Animation.PlayMode.LOOP);
			da.put(dirCom[i].getId(), anim);
		}
		animCom.animations.put(StateComponent.IDLE,da);
		HashMap <Integer, Animation> db=new HashMap <Integer, Animation>();
		for(int i=0;i<attackingAnimationNames.length;i++) {
			Animation anim = new Animation(0.2f,atlas.findRegions(attackingAnimationNames[5]));
			anim.setPlayMode(Animation.PlayMode.LOOP);
			db.put(dirCom[i].getId(), anim);
		}
		animCom.animations.put(StateComponent.ATTACKING,db);
		HashMap <Integer, Animation> dc=new HashMap <Integer, Animation>();
		for(int i=0;i<dirCom.length;i++) {
			Animation anim = new Animation(0.2f,atlas.findRegions(deadAnimationName));
			anim.setPlayMode(Animation.PlayMode.LOOP);
			dc.put(dirCom[i].getId(), anim);
		}
		animCom.animations.put(StateComponent.DIEING,dc);
		b2dbody.body.setAngularDamping(10);
		b2dbody.body.setLinearDamping(10);

		entity.add(b2dbody);
		entity.add(position);
		entity.add(texture);
		entity.add(player);
		entity.add(colComp);
		entity.add(type);
		entity.add(animCom);
		entity.add(stateCom);
		entity.add(dirCom[0]);

		engine.addEntity(entity);
		return entity;
	}
	/**
	 * Creates the water entity that steadily moves upwards towards player
	 * @return
	 */
	public Entity createWaterFloor(TextureRegion tex){
		Entity entity = engine.createEntity();
		B2DBodyComponent b2dbody = engine.createComponent(B2DBodyComponent.class);
		TransformComponent position = engine.createComponent(TransformComponent.class);
		TextureComponent texture = engine.createComponent(TextureComponent.class);
		TypeComponent type = engine.createComponent(TypeComponent.class);
		WaterFloorComponent waterFloor = engine.createComponent(WaterFloorComponent.class);

		type.type = TypeComponent.OTHER;
		texture.region = tex;
		b2dbody.body = bodyFactory.makeBoxPolyBody(0,0,40,10, BodyFactory.GRASS, BodyType.StaticBody,true); 
		position.position.set(20,0,0);
		bodyFactory.makeAllFixturesSensors(b2dbody.body);
		entity.add(b2dbody);
		entity.add(position);
		entity.add(texture);
		entity.add(type);
		entity.add(waterFloor);

		b2dbody.body.setUserData(entity);

		engine.addEntity(entity);

		return entity;
	}
	public void createWalls(TextureRegion tex){

		for(int i = 0; i < 2; i++){
			System.out.println("Making wall "+i);
			Entity entity = engine.createEntity();
			B2DBodyComponent b2dbody = engine.createComponent(B2DBodyComponent.class);
			TransformComponent position = engine.createComponent(TransformComponent.class);
			TextureComponent texture = engine.createComponent(TextureComponent.class);
			TypeComponent type = engine.createComponent(TypeComponent.class);
			WallComponent wallComp = engine.createComponent(WallComponent.class);

			//make wall
			b2dbody.body = b2dbody.body = bodyFactory.makeBoxPolyBody(0+(i*40),30,1,60, BodyFactory.STONE, BodyType.KinematicBody,true);
			position.position.set(0+(i*40), 30, 0);
			texture.region = tex;
			type.type = TypeComponent.SCENERY;

			entity.add(b2dbody);
			entity.add(position);
			entity.add(texture);
			entity.add(type);
			entity.add(wallComp);
			b2dbody.body.setUserData(entity);

			engine.addEntity(entity);
		}
	}
	public Entity createEnemy(TextureRegion tex, float x, float y){
		Entity entity = engine.createEntity();
		B2DBodyComponent b2dbody = engine.createComponent(B2DBodyComponent.class);
		TransformComponent position = engine.createComponent(TransformComponent.class);
		TextureComponent texture = engine.createComponent(TextureComponent.class);
		EnemyComponent enemy = engine.createComponent(EnemyComponent.class);
		TypeComponent type = engine.createComponent(TypeComponent.class);
		CollisionComponent colComp = engine.createComponent(CollisionComponent.class);

		b2dbody.body = bodyFactory.makeCirclePolyBody(x,y,1, BodyFactory.STONE, BodyType.KinematicBody,true);
		position.position.set(x,y,0);
		texture.region = tex;
		enemy.xPosCenter = x;
		type.type = TypeComponent.ENEMY;
		b2dbody.body.setUserData(entity);

		entity.add(colComp);
		entity.add(b2dbody);
		entity.add(position);
		entity.add(texture);
		entity.add(enemy);
		entity.add(type);	

		engine.addEntity(entity);

		return entity;
	}
	public Entity createBullet(float x, float y, float xVel, float yVel){
		Entity entity = engine.createEntity();
		B2DBodyComponent b2dbody = engine.createComponent(B2DBodyComponent.class);
		TransformComponent position = engine.createComponent(TransformComponent.class);
		//TextureComponent texture = engine.createComponent(TextureComponent.class);
		//AnimationComponent animCom = engine.createComponent(AnimationComponent.class);
		StateComponent stateCom = engine.createComponent(StateComponent.class);
		TypeComponent type = engine.createComponent(TypeComponent.class);
		CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
		BulletComponent bul = engine.createComponent(BulletComponent.class);
		//DirectionComponent dirCom=engine.createComponent(DirectionComponent.class);

		b2dbody.body = bodyFactory.makeCirclePolyBody(x,y,0.5f, BodyFactory.STONE, BodyType.DynamicBody,true);
		b2dbody.body.setBullet(true); // increase physics computation to limit body travelling through other objects
		bodyFactory.makeAllFixturesSensors(b2dbody.body); // make bullets sensors so they don't move player
		position.position.set(x,y,0);
		b2dbody.body.setTransform(x,y,MathUtils.atan2(yVel, xVel));
		//texture.region = bulletTex;
		/*DirectionComponent[] dirComs= {new DirectionComponent(DirectionComponent.NORTH_EAST),
				new DirectionComponent(DirectionComponent.NORTH),
				new DirectionComponent(DirectionComponent.NORTH_WEST),
				new DirectionComponent(DirectionComponent.WEST),
				new DirectionComponent(DirectionComponent.SOUTH_WEST),
				new DirectionComponent(DirectionComponent.SOUTH),
				new DirectionComponent(DirectionComponent.SOUTH_EAST),
				new DirectionComponent(DirectionComponent.EAST)};*/
		String bulletName="Fireball";
		/*HashMap <Integer, Animation> db=new HashMap <Integer, Animation>();
		for(int i=0;i<dirComs.length;i++) {
			Animation anim = new Animation(0.2f,atlas.findRegions(bulletName));
			anim.setPlayMode(Animation.PlayMode.LOOP);
			db.put(dirComs[i].getId(), anim);
		}
		animCom.animations.put(StateComponent.MOVING, db);*/
		stateCom.set(StateComponent.MOVING);
		type.type = TypeComponent.BULLET;
		b2dbody.body.applyLinearImpulse(xVel,yVel, b2dbody.body.getWorldCenter().x,b2dbody.body.getWorldCenter().y, true);
		b2dbody.body.setUserData(entity);
		bul.xVel = xVel;
		bul.yVel = yVel;
		//attach party to bullet
		bul.particleEffect = makeParticleEffect(ParticleEffectManager.FIREBALL,b2dbody);
		entity.add(bul);
		entity.add(colComp);
		entity.add(b2dbody);
		entity.add(position);
		//entity.add(texture);
		//entity.add(animCom);
		entity.add(stateCom);
		//entity.add(dirComs[0]);
		entity.add(type);	
		engine.addEntity(entity);
		return entity;
	}
	/**
	 * Make particle effect at xy
	 * @param x 
	 * @param y
	 * @return the Particle Effect Entity
	 */
	public Entity makeParticleEffect(int type, float x, float y){
		Entity entPE = engine.createEntity();
		ParticleEffectComponent pec = engine.createComponent(ParticleEffectComponent.class);
		pec.particleEffect = pem.getPooledParticleEffect(type);
		pec.particleEffect.setPosition(x, y);
		entPE.add(pec);
		engine.addEntity(entPE);
		return entPE;
	}

	/** Attache particle effect to body from body component
	 * @param type the type of particle effect to show
	 * @param b2dbody the bodycomponent with the body to attach to
	 * @return the Particle Effect Entity
	 */
	public Entity makeParticleEffect(int type, B2DBodyComponent b2dbody){
		return makeParticleEffect(type,b2dbody,0,0);
	}

	/**
	 * Attache particle effect to body from body component with offsets
	 * @param type the type of particle effect to show
	 * @param b2dbody the bodycomponent with the body to attach to
	 * @param xo x offset
	 * @param yo y offset
	 * @return the Particle Effect Entity
	 */
	public Entity makeParticleEffect(int type, B2DBodyComponent b2dbody, float xo, float yo){
		Entity entPE = engine.createEntity();
		ParticleEffectComponent pec = engine.createComponent(ParticleEffectComponent.class);
		System.out.println("Particle type:"+type);
		pec.particleEffect = pem.getPooledParticleEffect(type);
		pec.particleEffect.setPosition(b2dbody.body.getPosition().x, b2dbody.body.getPosition().y);
		ParticleEmitter emitter = pec.particleEffect.getEmitters().first();
		pec.particleEffect.getEmitters().first().setAttached(true); //manually attach for testing
		emitter.getAngle().setHigh(b2dbody.body.getAngle()*MathUtils.radDeg+180); 
		emitter.getAngle().setLow(b2dbody.body.getAngle()*MathUtils.radDeg+180);
		//System.out.println("Particle Angle:"+emitter.getAngle().);
		pec.xOffset = xo; 
		pec.yOffset = yo;
		pec.isattached = true;
		pec.attachedBody = b2dbody.body;
		entPE.add(pec);
		engine.addEntity(entPE);
		return entPE;
	}
}