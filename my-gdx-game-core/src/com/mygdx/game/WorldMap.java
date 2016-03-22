package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.chunk.*;

public class WorldMap {

	static final int MAXLIGHTLEVEL = 15;
	static final int W = 35;
	static final int H = 25;

	ArrayList<Cave> caves = new ArrayList<>();

	ArrayList<Chunk> chunkList = new ArrayList<>();

	private static final int ROCKAMOUNT = 5; // maximum types of rocks games has
	private static final int CHANCEOFADDINGCHUNK = 2; // Between 0-5

	private int GRASS_MAX = 1800; // to 1400
	private final int GRASS_START = 1600; // start 1000 down
	private final int DIRT_START = 1200; // 500 down to 1000 (Caves will be
											// generated in this part)
	private final int ROCK_START = 800; //
	private final int COAL_START = 450;
	private final int LAVA_START = 0;

	private final int CAVEREGULARITY = 10; // lower values for smaller but more
											// caves, larger for less but larger
											// caves

	private final int MAXLIGHT = 15; // top brightness of light

	private final int TREECHANCE = 7;

	private static int w, h;

	private SimplexNoise sn;

	private BlankEntity[][] worldMap; // 2D array to store the whole map

	private Random r = new Random();

	/*
	 * addLight Place a light source - when this is placed, we then need to
	 * update the light levels on blocks in it's visinity
	 */
	public boolean addLight(int x, int y) {
		if (worldMap[x][y] instanceof LightEntity)
			return false;

		worldMap[x][y] = new LightEntity(x, y);

		updateBlocksAroundLight(x, y);

		return true;
	}

	public void removeLight(int x, int y) {
		removeEntity(x, y);
	}

	public void addFloatingIsland() {
		/*for (int y = h - 1; y > GRASS_MAX; y--) // 2000 > 1400
		{
			for (int x = 0; x < w - 1; x++) {
				worldMap[x][y] = new CoalEntity(x, y);
			}
		}*/
	}

	/*
	 * updateBlocksAroundLight This is where we need to change the light level
	 * of the blocks around the light placed at worldMap[x,y] Each block/entity
	 * has a lightLevel property Calculation: lightlevel = MAXLIGHT - (tile.x -
	 * x) x - x position of torch/light y - y position of torch/light
	 */
	private void updateBlocksAroundLight(int x, int y) {

		setLightSource(x, y, worldMap, MAXLIGHTLEVEL);

	}

	private void setLightSource(int x, int y, BlankEntity[][] map, int lightLevel) {
		map[x][y].lightValue = (byte) lightLevel;
	}

	/*
	 * Go through all lights in view and update blocks etc around them - this
	 * method is called every frame - well almost!
	 */
	public void updateLights(int sx, int sy, int ex, int ey, boolean torch) {
		for (int y = sy; y <= ey; y++) {
			for (int x = sx; x < ex + 1; x++) {
				if (worldMap[x][y] != null) {
					worldMap[x][y].lightValue = worldMap[x][y].originalLightValue; // we
																					// reset
																					// all
																					// tiles
																					// back
																					// to
																					// original
																					// colour
				}
			}
		}
		// now lets see if we have any lights near the blocks
		for (int y = sy; y <= ey; y++) {
			for (int x = sx; x < ex + 1; x++) {

				if (worldMap[x][y] instanceof LightEntity) { // found a light
					updateTileLight(worldMap, x, y);
				}
			}
		}

	}

	public void updateTorch(Vector3 position) {

		updateTileLight(worldMap, (int) position.x, (int) position.y);

	}

	private void updateTileLight(BlankEntity[][] map, int lightX, int lightY) {
		byte lightLevel = 0;
		try {
			for (int x = 0; x < 1 + W / 2; x++) {
				for (int y = 0; y < (H / 2); y++) {
					lightLevel = (byte) ((byte) MAXLIGHTLEVEL - x - y);
					if (lightLevel > 15)
						lightLevel = MAXLIGHTLEVEL;
					if (lightLevel < 0)
						lightLevel = 0;

					// Array index out of bounds fixed - 8/3/2016
					if ((x + (lightX) < WorldMap.w) && ((lightY) - y > 0) && ((lightX) - x > 0)
							&& ((lightY) + 1 + y < WorldMap.h)) {
						if (map[x + lightX][lightY - y] != null) {
							if (map[x + lightX][lightY - y].lightValue < lightLevel) {
								map[x + lightX][lightY - y].lightValue = lightLevel;
							}
						}

						if (map[x + lightX][lightY + 1 + y] != null) {
							if (map[x + lightX][lightY + 1 + y].lightValue < lightLevel) {
								map[x + lightX][lightY + 1 + y].lightValue = lightLevel;
							}
						}
						if (map[lightX - x][lightY - y] != null) {

							if (map[lightX - x][lightY - y].lightValue < lightLevel) {
								map[lightX - x][lightY - y].lightValue = lightLevel;
							}
						}
						if (map[lightX - x][lightY + 1 + y] != null) {

							if (map[lightX - x][lightY + 1 + y].lightValue < lightLevel) {
								map[lightX - x][lightY + 1 + y].lightValue = lightLevel;
							}
						}
					}

				}
			}
		} catch (Exception ex) {
			System.out.println("Light out of range");
		}

	}

	/*
	 * getYPosition
	 */
	private int getYPosition(int x) {
		int yOffset = 8;
		float val = Math.abs(sn.noise(x, 1));
		if (val < 0.05)
			yOffset = 1;
		if (val > 0.05 && val < 0.1)
			yOffset = 3;
		if (val > 0.1 && val < 0.2)
			yOffset = 5;
		if (val > 0.2 && val < 0.3)
			yOffset = 8;
		if (val > 0.3 && val < 0.4)
			yOffset = 10;
		if (val > 0.4 && val < 0.6)
			yOffset = 15;
		if (val > 0.6 && val < 0.7)
			yOffset = 40;
		if (val > 0.8 && val < 0.9)
			yOffset = 48;

		return yOffset;
	}

	/*
	 * WorldMap Constructor
	 */
	public WorldMap(int w, int h) {

		// GRASS_MAX = h; // Set to height of map

		WorldMap.w = w;
		WorldMap.h = h;

		worldMap = new BlankEntity[w][h];

		sn = new SimplexNoise();

		generateMap();
	}

	/*
	 * getBlock get tile from map
	 */
	public BlankEntity getBlock(int x, int y) {
		if (x <= WorldMap.w && x >= 0 && y <= WorldMap.h && y >= 0)
			return worldMap[x][y];
		else
			return null;
	}

	/*
	 * generateMap Generate the world map
	 */
	private void generateMap() {
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				worldMap[x][y] = null;// new BlankEntity(x,y);
			}
		}

		int yOffset = getYPosition(1);

		addEntity(new GrassEntity(0, GRASS_START - 2 + yOffset, GrassType.FLAT));
		for (int x = 1; x < w; x++) {
			int pos = r.nextInt(5);
			if (pos == 1 && yOffset < GRASS_MAX - GRASS_START) {
				yOffset += 2;
				addEntity(new GrassLeftEntity(x, GRASS_START + yOffset));
			} else if (pos >= 4 && yOffset >= 0) {
				yOffset -= 2;
				addEntity(new GrassEntity(x, GRASS_START + yOffset, GrassType.FLAT));
			} else {
				addEntity(new GrassRightEntity(x, GRASS_START + yOffset));// ,
			}
		}

		CellularAutomata caves = new CellularAutomata(GRASS_START - DIRT_START, w, 30, 5, 4);

		System.out.println("Creating caves...");
		caves.initialiseMap();
		for (int i = 0; i < CAVEREGULARITY; i++) {
			caves.doSimulationStep(3);
		}

		System.out.println("Creating dirt blocks...");
		// Add dirt/earth - need to pass map array to cave generation here
		for (int x = 0; x < w; x++) {
			int y = DIRT_START + getYPosition(r.nextInt(4));
			do {
				if (x > 0) {
					BlankEntity entity = this.getEntity(x - 1, y);
					if (entity == null) {
						addEntity(new LandscapeLeftEntity(x, y));
					} else {
						addEntity(r.nextBoolean() == true ? new LandscapeEntity(x, y) : new LandscapeEntity2(x, y));
					}
				} else
					addEntity(r.nextBoolean() == true ? new LandscapeEntity(x, y) : new LandscapeEntity2(x, y));

				y++;
			} while (!(worldMap[x][y] instanceof BlankEntity));
		}

		//
		for (int yy = DIRT_START; yy < GRASS_MAX; yy++) {
			for (int x = w - 2; x > 1; x--) {
				BlankEntity entity = this.getEntity(x - 1, yy);

				if (entity == null && getEntity(x + 1, yy) == null && getEntity(x, yy) instanceof LandscapeEntity) {
					// Single block
					addEntity(new LandscapeSingle(x, yy));
				} else {
					if (entity instanceof LandscapeEntity) // one to left a
															// brick
					{

						if (getEntity(x, yy) == null)
							addEntity(new LandscapeRightEntity(x - 1, yy));
					}
				}

			}
		}
		//

		// Add rock
		System.out.println("Creating rock blocks...");
		for (int x = 0; x < w; x++) {
			int y = ROCK_START + r.nextInt(1);
			do {
				addEntity(new RockEntity(x, y));
				y++;
			} while (!(worldMap[x][y] instanceof LandscapeEntity));
		}

		// Add coal
		System.out.println("Creating Coal blocks...");
		for (int x = 0; x < w; x++) {
			int y = COAL_START + r.nextInt(3);
			do {
				addEntity(new CoalEntity(x, y));
				y++;
			} while (!(worldMap[x][y] instanceof RockEntity));
		}

		System.out.println("Creating Lava blocks...");
		// Add lava
		for (int x = 0; x < w; x++) {
			int y = LAVA_START;
			do {
				addEntity(new RealLavaEntity(x, y));
				y++;
			} while (!(worldMap[x][y] instanceof CoalEntity));
		}

		System.out.println("Adding caves pass one...");
		// Add caves to earth/dirt layer
		BlankEntity[][] m = caves.getMap();
		for (int x = 0; x < GRASS_START - (DIRT_START + 2); x++) {
			for (int y = 0; y < w; y++) {
				if (m[x][y] != null) {
					m[x][y].x = y;
					m[x][y].y = x + DIRT_START;
				}
				worldMap[y][x + DIRT_START] = m[x][y];
			}
		}

		// 2nd pass checks - modify grass graphics
		System.out.println("Creating grass...");
		for (int y = GRASS_START; y < GRASS_MAX; y++) {
			for (int x = 1; x < w - 1; x++) {
				if (worldMap[x][y] instanceof GrassEntity) {
					if (worldMap[x - 1][y] == null) {
						if (worldMap[x + 1][y] == null) {
							addEntity(new GrassTopEntity(x, y));
						}
					}
				} else {
					if (worldMap[x][y] == null) { // check what is around it
						if (worldMap[x - 1][y] instanceof GrassEntity) // check
																		// right
						{
							GrassEntity g = (GrassEntity) worldMap[x - 1][y];
							if (g.getGrassType() != GrassType.TOP) {
								addEntity(new GrassRightEntity(x - 1, y)); // add
																			// right
							}
						}
					} else {
						if ((worldMap[x - 1][y] instanceof GrassRightEntity)) {
							// worldMap[x][y+2] = new WeedsEntity(x,y+2);
						}
					}
				}
			}
		}

		System.out.println("Adding cave walls...");

		// Do cave gfx - add cave edges to left and right
		for (int x = 0; x < GRASS_START - (DIRT_START + 2); x++) {
			for (int y = 1; y < w - 1; y++) {
				if (worldMap[y][x + DIRT_START] instanceof LandscapeEntity) {
					if (worldMap[y - 1][x + DIRT_START] == null) {
						addEntity(new CaveRightEntity(y - 1, x + DIRT_START));
					} else {
						if (worldMap[y + 1][x + DIRT_START] == null) {
							addEntity(new CaveLeftEntity(y + 1, x + DIRT_START));
						}
					}
				}
			}
		}

		System.out.println("Adding grass...");

		for (int x = 0; x < GRASS_START - (DIRT_START + 2); x++) {
			for (int y = 1; y < w - 1; y++) {
				if (worldMap[y][x + DIRT_START + 1] instanceof LandscapeEntity) // dirt
																				// above
				{
					if (worldMap[y][x + DIRT_START] == null) // empty space -
																// cave
						addEntity(new CaveTopEntity(y, x + DIRT_START));
				} else {
					if (worldMap[y][x + DIRT_START - 1] instanceof LandscapeEntity) // dirt
																					// above
					{
						if (worldMap[y][x + DIRT_START] == null) // empty space
																	// - cave
							addEntity(new CaveBottomEntity(y, x + DIRT_START));
					}
				}
			}
		}

		System.out.println("Adding caves pass two...");

		// Cave third pass - fill in nulls -
		for (int x = 0; x < GRASS_START - (DIRT_START + 2); x++) {
			for (int y = 1; y < w - 1; y++) {
				if (worldMap[y][x + DIRT_START] == null) // empty space - cave
				{
					addEntity(new CaveEntity(y, x + DIRT_START));
				}
			}
		}

		// Caves in Rock layer
		caves = new CellularAutomata(DIRT_START - ROCK_START, w, 30, 5, 4);

		System.out.println("Creating caves in rocks...");
		caves.initialiseMap();
		for (int i = 0; i < CAVEREGULARITY + 3; i++) {
			caves.doSimulationStep(0);
		}
		m = caves.getMap();
		for (int x = 0; x < DIRT_START - (ROCK_START); x++) {
			for (int y = 0; y < w; y++) {
				if (m[x][y] != null) {
					m[x][y].x = y;
					m[x][y].y = x + ROCK_START;
				}
				worldMap[y][x + ROCK_START] = m[x][y];
			}
		}
		// TO DO - NEED GRAPHICS FOR ROCK LEFT/RIGHT and entities creating for
		// them
		/*
		 * System.out.println(
		 * "Creating rock left and right entities for rock caves..."); for (int
		 * x = 0; x < DIRT_START - (ROCK_START ); x++) { for (int y = 1; y < w -
		 * 1; y++) { if (worldMap[y][x + ROCK_START] instanceof LandscapeEntity)
		 * { if (worldMap[y - 1][x + ROCK_START] == null) { addEntity(new
		 * CaveLeftEntity(y - 1, x + ROCK_START)); // right rockEntity needs
		 * creating } else { if (worldMap[y + 1][x + ROCK_START] == null) {
		 * addEntity(new CaveRightEntity(y + 1, x + ROCK_START)); // left
		 * rockEntity needs creating } } } } }
		 */

		System.out.println("Filling in null blocks in caves in rock layer...");
		for (int x = 0; x < DIRT_START - (ROCK_START); x++) {
			for (int y = 1; y < w - 1; y++) {
				if (worldMap[y][x + ROCK_START] == null) // empty space - cave
				{
					addEntity(new CaveEntity(y, x + ROCK_START));
				}
			}
		}

		// Caves in COAL layer
		caves = new CellularAutomata(ROCK_START - COAL_START, w, 30, 5, 4);

		System.out.println("Creating caves in coal layer...");
		caves.initialiseMap();
		for (int i = 0; i < CAVEREGULARITY + 7; i++) {
			caves.doSimulationStep(1);
		}
		m = caves.getMap();
		for (int x = 0; x < ROCK_START - (COAL_START); x++) {
			for (int y = 0; y < w; y++) {
				if (m[x][y] != null) {
					m[x][y].x = y;
					m[x][y].y = x + COAL_START;
				}
				worldMap[y][x + COAL_START] = m[x][y];
			}
		}
		// TO DO - NEED GRAPHICS FOR COAL LEFT/RIGHT and entities creating for
		// them
		/*
		 * System.out.println(
		 * "Creating coal left and right entities for coal caves..."); for (int
		 * x = 0; x < ROCK_START - (COAL_START ); x++) { for (int y = 1; y < w -
		 * 1; y++) { if (worldMap[y][x + COAL_START] instanceof LandscapeEntity)
		 * { if (worldMap[y - 1][x + COAL_START] == null) { addEntity(new
		 * CaveLeftEntity(y - 1, x + COAL_START)); // right rockEntity needs
		 * creating } else { if (worldMap[y + 1][x + COAL_START] == null) {
		 * addEntity(new CaveRightEntity(y + 1, x + COAL_START)); // left
		 * rockEntity needs creating } } } } }
		 */
		System.out.println("Filling in null blocks in caves in coal layer...");
		for (int x = 0; x < ROCK_START - (COAL_START); x++) {
			for (int y = 1; y < w - 1; y++) {
				if (worldMap[y][x + COAL_START] == null) // empty space - cave
				{
					addEntity(new CaveEntity(y, x + COAL_START));
				}
			}
		}

		System.out.println("Adding water...");

		// Create cave entities so we can add water and wot not to them
		FloodFill fFill = new FloodFill(worldMap);

		for (int x = 0; x < worldMap.length; x++) {
			for (int y = 0; y < worldMap[x].length; y++) {
				fFill.iterativeFill(x, y);
				ArrayList<CaveCell> cavec = fFill.caveCells;
				if (cavec.size() > 0) {
					Cave c = new Cave();
					for (CaveCell cell : cavec) {
						c.cells.add(cell);
					}
					this.caves.add(c);
				}
			}
		}
		System.out.println("[Caves generated:[" + this.caves.size() + "]");

		/*
		 * for(Cave cave : this.caves) { for(CaveCell cell : cave.cells) {
		 * System.out.printf("X:%d,Y:%d",cell.x,cell.y);
		 * 
		 * } System.out.println("\nNext cave:"); }
		 */

		addWaterToCave(0);

	
		// Caves in Lava layer
		caves = new CellularAutomata(COAL_START - LAVA_START, w, 30, 5, 4);

		System.out.println("Creating caves in Lava layer...");
		caves.initialiseMap();
		for (int i = 0; i < CAVEREGULARITY + 12; i++) {
			caves.doSimulationStep(2);
		}
		m = caves.getMap();
		for (int x = 0; x < COAL_START - (LAVA_START); x++) {
			for (int y = 0; y < w; y++) {
				if (m[x][y] != null) {
					m[x][y].x = y;
					m[x][y].y = x + LAVA_START;
				}
				worldMap[y][x + LAVA_START] = m[x][y];
			}
		}
		// TO DO - NEED GRAPHICS FOR COAL LEFT/RIGHT and entities creating for
		// them
		/*
		 * System.out.println(
		 * "Creating coal left and right entities for coal caves..."); for (int
		 * x = 0; x < COAL_START - (LAVA_START ); x++) { for (int y = 1; y < w -
		 * 1; y++) { if (worldMap[y][x + LAVA_START] instanceof LandscapeEntity)
		 * { if (worldMap[y - 1][x + LAVA_START] == null) { addEntity(new
		 * CaveLeftEntity(y - 1, x + LAVA_START)); // right rockEntity needs
		 * creating } else { if (worldMap[y + 1][x + LAVA_START] == null) {
		 * addEntity(new CaveRightEntity(y + 1, x + LAVA_START)); // left
		 * rockEntity needs creating } } } } }
		 */
		System.out.println("Filling in null blocks in caves in lava layer...");
		for (int x = 0; x < COAL_START - (LAVA_START); x++) {
			for (int y = 1; y < w - 1; y++) {
				if (worldMap[y][x + LAVA_START] == null) // empty space - cave
				{
					addEntity(new CaveEntity(y, x + LAVA_START)); // need to add
																	// red lava
																	// type tile
				}
			}
		}

		System.out.println("Creating chunks...");
		createChunks();
		System.out.println("Amount of chunks we can add to:" + chunkList.size() + "\n\n");
		System.out.println("Inserting random rocks...");
		insertRandomRocks();

		System.out.println("Adding random planets...");
		//addPlanets();

		Random r = new Random();
		int randValue = r.nextInt(60) + 100;
		int xrandValue = r.nextInt(6) + 150;
		for (int y = h - 60; y > GRASS_START + 100; y-=randValue) // 2000 > 1400
		{
			for (int x = 50; x < w - 51; x+=xrandValue) {
	
				addPlanet(x,y);
			}
		}
		
		System.out.println("All done!");
		// addTrees(); // This needs updating
	}

	/*
	 * addWaterToCave Add water blocks to cave - still needs work as adds to
	 * half way in the cave - best to do random height and also do random value
	 * if the cave is going to have water or not (DONE if has water 6/3/2016)
	 */
	//
	private void addWaterToCave(int whichCave) {

		Random r = new Random();

		for (int xx = 0; xx < caves.size(); xx++) {
			Cave cavern = this.caves.get(xx);

			if (!cavern.isWaterCave) // Is this a water cave?
				continue;

			int highest = 0;
			int lowest = 100000;

			for (int i = 0; i < cavern.cells.size(); i++) {
				if (cavern.cells.get(i).y > highest)
					highest = cavern.cells.get(i).y;
				if (cavern.cells.get(i).y < lowest)
					lowest = cavern.cells.get(i).y;
			}

			int pickedValue = lowest;
			if (highest - lowest > 0)
				pickedValue = lowest + ((highest - lowest) / 2);

			for (int i = 0; i < cavern.cells.size(); i++) {
				if (cavern.cells.get(i).y <= pickedValue)
					worldMap[cavern.cells.get(i).x][cavern.cells.get(i).y] = new WaterEntity(cavern.cells.get(i).x,
							cavern.cells.get(i).y);
			}
			for (int i = 0; i < cavern.cells.size(); i++) {
				if (cavern.cells.get(i).y == pickedValue) {
					worldMap[cavern.cells.get(i).x][cavern.cells.get(i).y] = new WaterTopEntity(cavern.cells.get(i).x,
							cavern.cells.get(i).y);
				}
				if (cavern.cells.get(i).y == lowest) {
					worldMap[cavern.cells.get(i).x][cavern.cells.get(i).y] = new WaterBottomEntity(
							cavern.cells.get(i).x, cavern.cells.get(i).y);
				}
			}

		}
	}

	/*
	 * addTress Add trees of random height to the top level (grass)
	 */
	private void addTrees() {
		Random r = new Random();
		for (int y = GRASS_START; y < GRASS_MAX; y++) {
			for (int x = 1; x < w - 1; x++) {

				if (worldMap[x][y] instanceof GrassEntity) {
					GrassEntity g = (GrassEntity) worldMap[x][y];
					if (g.getGrassType() == GrassType.FLAT) {
						if (worldMap[x + 1][y] instanceof GrassEntity) {
							g = (GrassEntity) worldMap[x + 1][y];
							if (g.getGrassType() == GrassType.FLAT) {
								if (r.nextInt(20) > TREECHANCE) {
									// This needs modifying so we just place a
									// tree sprite, not
									// modify the tile map!
									for (int i = 1; i < r.nextInt(50); i++)
										worldMap[x][y + i] = new CaveBottomEntity(x, y + i); // place
																								// tree
																								// sprite
								}
								x += 10; // so we don't have a tree next to each
											// other....
							}
						}
					}
				}
			}
		}
	}

	/*
	 * getEntity Get tile from map
	 */
	private BlankEntity getEntity(int x, int y) {
		return worldMap[x][y];
	}

	/*
	 * addEntity Add a tile to the map
	 */
	public void addEntity(BlankEntity entity) {
		worldMap[entity.x][entity.y] = entity;
	}

	/*
	 * removeEntity Remove a tile from the map
	 */
	public void removeEntity(int x, int y) {
		worldMap[x][y] = new CaveEntity(x, y);
	}

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	public void moveWater(int sx, int sy, int ex, int ey) {
		for (int y = sy; y <= ey; y++) {
			for (int x = sx; x < ex + 1; x++) {
				if (worldMap[x][y] instanceof WaterEntity) {
					if ((worldMap[x][y - 1] instanceof LavaEntity) || (worldMap[x][y - 1] instanceof CaveEntity)) {
						worldMap[x][y] = new CaveEntity(x, y);
						worldMap[x][y - 1] = new WaterEntity(x, y - 1);
					} else if ((worldMap[x - 1][y] instanceof LavaEntity)
							|| (worldMap[x - 1][y] instanceof CaveEntity)) { // air
																				// to
																				// the
																				// left
						worldMap[x][y] = new CaveEntity(x, y);
						worldMap[x - 1][y] = new WaterEntity(x - 1, y);
					} else if ((worldMap[x + 1][y] instanceof LavaEntity)
							|| (worldMap[x + 1][y] instanceof CaveEntity)) { // air
																				// to
																				// the
																				// right
						worldMap[x][y] = new CaveEntity(x, y);
						worldMap[x + 1][y] = new WaterEntity(x + 1, y);
					}
				}
			}
		}
	}

	/*
	 * drawMap When drawing the map, we need to draw just screen width, screen
	 * height at the position the player is in
	 */
	public void drawMap(boolean bDebug, SpriteBatch batch, TextureRegion[][] region, int sx, int sy, int ex, int ey,
			int w, int h) {

		if (bDebug) { // debug mode - brute force draw everything
			for (int row = 0; row < h; row++)
				for (int col = 0; col < w; col++)
					if (getEntity(col, row) != null)
						worldMap[col][row].draw(batch, region, col, row);
		} else { // draw what we can see
			for (int row = sy; row < ey + 1; row++)
				for (int col = sx; col < ex + 1; col++) {
					BlankEntity entity = getEntity(col, row);
					if (entity != null) {
						{
							batch.setColor((float) entity.lightValue / MAXLIGHT, (float) entity.lightValue / MAXLIGHT,
									(float) entity.lightValue / MAXLIGHT, 1);
							// We could be fancy here and not draw any blocks
							// that
							// don't have any colour...
							// We would need to work out how not to display the
							// background through though, that's the problem...
							if (entity.lightValue > 0)
								worldMap[col][row].draw(batch, region, col, row);
							// if(worldMap[col][row] instanceof BlankEntity) {
							// batch.draw(region[1][0], col << 4, row << 4);
							// }
						}
					}
				}
		}
	}

	private void insertRandomRocks() {

		Random r = new Random();
		for (Chunk c : chunkList) {
			int rock = r.nextInt(ROCKAMOUNT);
			for (int y = 0; y < Chunk.CHUNKSIZE; y++) {
				for (int x = 0; x < Chunk.CHUNKSIZE; x++) {
					drawRockLayer(c.x, c.y, rock);

				}
			}
		}
	}

	private void drawRockLayer(int xx, int yy, int rock) {
		Random r = new Random();
		int endY = r.nextInt(Chunk.CHUNKSIZE);
		for (int y = 0; y < endY; y++) {
			int xStart = r.nextInt(Chunk.CHUNKSIZE - 2);
			int xEnd = r.nextInt(Chunk.CHUNKSIZE);
			if (xEnd < xStart)
				xEnd += 1;
			for (int x = xStart; x < xEnd; x++) {
				if (yy < 1000) {
					switch (rock) // need to add more rocks here
					{

					case 0:
						worldMap[xx + x][yy + y] = r.nextBoolean() == true ? new RealLavaEntity(xx + x, yy + y)
								: worldMap[xx + x][yy + y];
						break;
					case 1:
						worldMap[xx + x][yy + y] = r.nextBoolean() == true ? new RockEntity(xx + x, yy + y)
								: worldMap[xx + x][yy + y];
						break;
					case 2:
						worldMap[xx + x][yy + y] = r.nextBoolean() == true ? new RockEntity(xx + x, yy + y)
								: worldMap[xx + x][yy + y];
						break;
					case 3:
						worldMap[xx + x][yy + y] = r.nextBoolean() == true ? new CoalEntity(xx + x, yy + y)
								: worldMap[xx + x][yy + y];
						break;
					case 4:
						worldMap[xx + x][yy + y] = r.nextBoolean() == true ? new CoalEntity(xx + x, yy + y)
								: worldMap[xx + x][yy + y];
						break;
					default:
						worldMap[xx + x][yy + y] = r.nextBoolean() == true ? new LandscapeEntity(xx + x, yy + y)
								: new LandscapeEntity2(xx + x, yy + y);
						break;
					}
				}
			}
		}
	}

	private void createChunks() {
		Random r = new Random();
		for (int sy = 0; sy < WorldMap.h; sy += Chunk.CHUNKSIZE) {
			for (int sx = 0; sx < WorldMap.w; sx += Chunk.CHUNKSIZE) {
				if (isChunk(sx, sy)) {
					if (r.nextInt(5) > CHANCEOFADDINGCHUNK) // randomly choose
															// if we create this
															// chunk (saves on
															// memory doing it
															// here)
						chunkList.add(new Chunk(sx, sy));
				}
			}
		}
	}

	private boolean isChunk(int x, int y) {
		try {
			for (int yy = 0; yy < Chunk.CHUNKSIZE; yy++) {
				for (int xx = 1; xx < Chunk.CHUNKSIZE; xx++) {
					// We don't want to add any special rocks in water or a cave
					// entity
					if (worldMap[x + xx][y + yy] == null || worldMap[x + xx][y + yy] instanceof WaterEntity
							|| worldMap[x + xx][y + yy] instanceof CaveEntity)
						return false;

					if (worldMap[x + xx][y + yy].id != worldMap[xx + x - 1][yy + y].id) {
						return false;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;// (Chunk.CHUNKSIZE-1) * Chunk.CHUNKSIZE;
	}
	
	
	private void addPlanet(int x, int y)
	{
		Random r = new Random();
		if(r.nextInt(4) >= 2)
		{
			for(float i = 0; i <  r.nextInt(30) + 8; i++){
			    for(double u = 0.0f; u < 360; u += 1){
			        double angle = u * Math.PI / 180;
			        int x2 = (int)(x + i * Math.cos(angle));
			        int y2 = (int)(y + i * Math.sin(angle));
			        worldMap[x2][y2] = r.nextBoolean() == true ? new CaveEntity(x2,y2) : new LandscapeEntity(x2,y2);
			    }
			}
		}
	}
	
	// This will create a weird landscape with some caves...
	private void generateLandIsland(int x, int y)
	{
		Random r = new Random();
		for(float i = 0; i <  r.nextInt(28) + 8; i++){
		    for(double u = 0.0f; u < r.nextInt(360)+100; u += 0.5){
		        double angle = u * Math.PI / 180;
		        int x2 = (int)(x + i * Math.cos(angle));
		        int y2 = (int)(y + i * Math.sin(angle));
		        worldMap[x2][y2] = r.nextBoolean() == true ? new CaveEntity(x2,y2) : new LavaEntity(x2,y2);
		    }
		}
	}
	
}
