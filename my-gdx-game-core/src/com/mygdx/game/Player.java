package com.mygdx.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {

	private static final float JUMPHEIGHT = 3.0f;
	
	private float endY, jumpY;
	
	public boolean jumping = false;
	
	private float stateTime = 0;

	private Texture playerTextures;
	private SpriteBatch spriteBatch;
	private TextureRegion currentFrame;
	private TextureRegion[] walkFrames;

	private static final int FRAMEROWS = 24;//768 / 32;
	private static final int FRAMECOLS = 50;//800 / 16;
	private static float TIMER = 1.0f;
	private static float AMOUNTFRAMES = 1.8f;

	private boolean bRight = true;
	
	private float animTime = 0;
	private Animation currentAnim;
	
	private Map<String, Animation> animations = new HashMap<String, Animation>();

	// Need to load animations and put into hashmap here
	// anim1 would be of type Animation
	public static void initialize() {
	}

	
	public void jump() {
		if(!jumping)
		{
			jumping = true;
			jumpY = y;
			endY = y - JUMPHEIGHT;
		}
		if(jumping)
		{
			if(jumpY <= endY)     // reached jump height?
			{
				jumping = false;  // yes, set jumping to false, player should now fall down
			} else
			{
				jumpY -= 0.1f;    // keep jumping
			}
		}
	}
	
	// Get Animation object with specified key
	private Animation getAnimation(String animKey) {
		if (!animations.containsKey(animKey)) {
			return null;
		}
		return animations.get(animKey);
	}

	
	public TextureRegion getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(TextureRegion currentFrame) {
		this.currentFrame = currentFrame;
	}

	public TextureRegion[] getWalkFrames() {
		return walkFrames;
	}

	public void setWalkFrames(TextureRegion[] walkFrames) {
		this.walkFrames = walkFrames;
	}

	public Animation getWalkAnimation() {
		return walkAnimation;
	}

	public void setWalkAnimation(Animation walkAnimation) {
		this.walkAnimation = walkAnimation;
	}

	// private TextureAtlas atlas;
	private Animation walkAnimation;

	private int x, y;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Player(String textureFile, int x, int y) {

		currentAnim = getAnimation("player-idle");

		playerTextures = new Texture(textureFile);

		TextureRegion[][] tmp = TextureRegion.split(playerTextures,
				playerTextures.getWidth() / FRAMECOLS,
				playerTextures.getHeight() / FRAMEROWS);
		walkFrames = new TextureRegion[FRAMECOLS * FRAMEROWS];

		int index = 0;
		for (int i = 0; i < FRAMEROWS; i++) {
			for (int j = 0; j < FRAMECOLS; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}
		walkAnimation = new Animation(TIMER, walkFrames);
		spriteBatch = new SpriteBatch();

		this.x = x;
		this.y = y;

		currentFrame = walkAnimation.getKeyFrame(0, true);

	}

	public void render(float delta) {
		stateTime += Gdx.graphics.getDeltaTime();

		animTime += Gdx.graphics.getDeltaTime();   // use for new animation code
		
		spriteBatch.begin();

		// Use this when new animation is implemented
		//spriteBatch.draw(currentAnim.getKeyFrame(animTime, true),x,50);

		spriteBatch.draw(currentFrame, !bRight ? x + 16 : x, y, !bRight ? -16 : 16,32);
		spriteBatch.end();
	}

	public void moveRight(int amount) {
		bRight = true;
		currentFrame = walkAnimation.getKeyFrame(TIMER, true);
		if (TIMER > AMOUNTFRAMES)
			TIMER = 0.0f;
		else
			TIMER += 0.1f;

	}

	public void moveLeft(int amount) {
		bRight = false;
		currentFrame = walkAnimation.getKeyFrame(TIMER, true);
		if (TIMER > AMOUNTFRAMES)
			TIMER = 0.0f;
		else
			TIMER += 0.1f;
	}

	public Texture getPlayerTextures() {
		return playerTextures;
	}

	public void setPlayerTextures(Texture playerTextures) {
		this.playerTextures = playerTextures;
	}

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	public void setSpriteBatch(SpriteBatch spriteBatch) {
		this.spriteBatch = spriteBatch;
	}

}
