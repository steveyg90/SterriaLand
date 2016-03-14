package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

enum GrassType {
	TOP, LEFT, RIGHT, FLAT
};

// Entity Factory - creates entities on requested type
class EntityFactory {
	static BlankEntity getEntity(String type, int x, int y) {
		if (type.equals("."))
			return new BlankEntity(x, y);
		if (type.equalsIgnoreCase("L"))
			return new LandscapeEntity(x, y);

		return null;
	}
}

class BlankEntity {

	protected static int x, y;
	
	protected byte lightValue = -1;
	protected byte originalLightValue = -1;
	
	protected byte visited = 0;
	
	protected byte hits = 1;

	public BlankEntity(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[1][0], x << 4, y << 4); // 0,0
	}

/*	@Override
	public String toString() {
		return ".";
	}
*/
}

class LandscapeEntity extends BlankEntity {

	public LandscapeEntity(int x, int y) {

		super(x, y);
		this.lightValue = 2;
		originalLightValue = 2;

	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[0][0], x << 4, y << 4);
	}
/*
	@Override
	public String toString() {
		return "L";
	}*/
}

class LandscapeEntityRight extends BlankEntity {

	public LandscapeEntityRight(int x, int y) {
		super(x, y);
		this.lightValue = 2;
		originalLightValue = 2;

	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[2][1], x << 4, y << 4);
	}
/*
	@Override
	public String toString() {
		return "LR";
	}*/
}

class LandscapeEntityLeft extends BlankEntity {

	public LandscapeEntityLeft(int x, int y) {
		super(x, y);
		this.lightValue = 2;
		originalLightValue = 2;

	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[2][0], x << 4, y << 4);
	}
/*
	@Override
	public String toString() {
		return "LL";
	}*/
}

class CaveEntity extends BlankEntity {

	public CaveEntity(int x, int y) {
		super(x, y);
		this.lightValue = 2;
		this.hits = 1;
		originalLightValue = 2;

	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[1][1], x << 4, y << 4);
	}

/*	@Override
	public String toString() {
		return "CAVE";
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof CaveEntity)) {
			return false;
		}
		return true;
	}
*/
}

class CaveRightEntity extends CaveEntity {

	public CaveRightEntity(int x, int y) {
		super(x, y);
	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[1][3], x << 4, y << 4);
	}
/*
	@Override
	public String toString() {
		return "CAVE";
	}*/
}

class CaveLeftEntity extends CaveEntity {

	public CaveLeftEntity(int x, int y) {
		super(x, y);
	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[1][2], x << 4, y << 4);
	}
/*
	@Override
	public String toString() {
		return "CAVE";
	}*/
}

class CaveTopEntity extends CaveEntity {

	public CaveTopEntity(int x, int y) {
		super(x, y);
	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[1][4], x << 4, y << 4);
	}
/*
	@Override
	public String toString() {
		return "CAVETOP";
	}*/
}

class CaveBottomEntity extends CaveEntity {

	public CaveBottomEntity(int x, int y) {
		super(x, y);
	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[1][5], x << 4, y << 4);
	}
/*
	@Override
	public String toString() {
		return "CAVE";
	}*/
}

class LavaEntity extends BlankEntity {

	public LavaEntity(int x, int y) {
		super(x, y);
		this.lightValue = 2;
		this.hits = 1;
		originalLightValue = 2;

	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[1][1], x << 4, y << 4); // 6,5
	}
/*
	@Override
	public String toString() {
		return "LAVA";
	}*/
}

class RealLavaEntity extends LandscapeEntity {

	public RealLavaEntity(int x, int y) {
		super(x, y);
		this.lightValue = 1;
		this.hits = 1;
		originalLightValue = 1;

	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[2][5], x << 4, y << 4); // 6,5
	}
/*
	@Override
	public String toString() {
		return "LAVA";
	}*/
}



class GrassEntity extends BlankEntity {

	private GrassType type;

	public GrassType getGrassType() {
		return type;
	}

	public GrassEntity(int x, int y, GrassType type) {
		super(x, y);
		this.type = type;
		this.lightValue = 15;
		this.hits = 1;
		originalLightValue = 15;

	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[0][3], x << 4, y << 4);
	}
/*
	@Override
	public String toString() {
		return "G";
	}*/

}

class WeedsEntity extends BlankEntity {

	public WeedsEntity(int x, int y) {
		super(x, y);
	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[13][0], x << 4, y << 4);
	}
/*
	@Override
	public String toString() {
		return ":";
	}
*/
}

class GrassLeftEntity extends GrassEntity {

	public GrassLeftEntity(int x, int y) {
		super(x, y, GrassType.LEFT);
	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[0][2], x << 4, y << 4);
	}
/*
	@Override
	public String toString() {
		return "G";
	}
*/
}

class GrassRightEntity extends GrassEntity {

	public GrassRightEntity(int x, int y) {
		super(x, y, GrassType.RIGHT);
	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[0][4], x << 4, y << 4);
	}
/*
	@Override
	public String toString() {
		return "G";
	}
*/
}

class GrassTopEntity extends GrassEntity {

	public GrassTopEntity(int x, int y) {
		super(x, y, GrassType.TOP);
	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[0][5], x << 4, y << 4);
	}
/*
	@Override
	public String toString() {
		return "G";
	}
*/
}

class CoalEntity extends LandscapeEntity {

	public CoalEntity(int x, int y) {
		super(x, y);
		this.lightValue = 1;
		this.hits = 5;
		originalLightValue = 1;

	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[2][7], x << 4, y << 4); // 0,19
	}
/*
	@Override
	public String toString() {
		return "C";
	}
*/
}

class RockEntity extends LandscapeEntity {

	public RockEntity(int x, int y) {
		super(x, y);
		this.hits = 3;
	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[2][6], x << 4, y << 4); // 0,1
	}
/*
	@Override
	public String toString() {
		return "R";
	}
*/
}

class WaterEntity extends BlankEntity {

	public WaterEntity(int x, int y) {
		super(x, y);
		this.lightValue = 1;
		originalLightValue = 1;

	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[2][0], x << 4, y << 4);
	}
/*
	public String toString() {
		return "WATER";
	}*/
}

class WaterTopEntity extends WaterEntity {

	public WaterTopEntity(int x, int y) {
		super(x, y);

	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[2][0], x << 4, y << 4);
		// batch.draw(t[2][1], x << 4, y << 4);
	}
/*
	public String toString() {
		return "WATER";
	}*/
}

class WaterBottomEntity extends WaterEntity {

	public WaterBottomEntity(int x, int y) {
		super(x, y);
	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[2][0], x << 4, y << 4);
		// batch.draw(t[2][2], x << 4, y << 4);
	}
/*
	public String toString() {
		return "WATER";
	}*/
}

class LightEntity extends BlankEntity {

	public LightEntity(int x, int y) {
		super(x, y);
		this.lightValue = 15;
		this.hits = 1;
		originalLightValue = 15;

	}

	public void draw(Batch batch, TextureRegion[][] t, int x, int y) {
		batch.draw(t[2][3], x << 4, y << 4);
	}
/*
	@Override
	public String toString() {
		return "Light";
	}*/
}
