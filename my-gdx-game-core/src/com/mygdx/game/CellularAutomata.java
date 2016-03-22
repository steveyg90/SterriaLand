package com.mygdx.game;

import java.util.Random;

public class CellularAutomata {

	private BlankEntity[][] map = null;
	private BlankEntity[][] newMap = null;
	private Random r = new Random();
	private int chanceToStartAlive, w, h, death, life;

	public CellularAutomata(int w, int h, int chanceToStartAlive, int death,
			int life) {
		this.w = w;
		this.h = h;
		this.chanceToStartAlive = chanceToStartAlive;
		this.death = death;
		this.life = life;
	}

	public void initialiseMap() {
		map = new BlankEntity[w][h];
		for (int x = 0; x < map.length; x++)
			for (int y = 0; y < map[0].length; y++)
				map[x][y] = r.nextInt(100) > chanceToStartAlive ? new LandscapeEntity(
						x, y) : null;
	}

	// Debug
	public void displayMap() {
		if (map != null) {
			for (int x = 0; x < map.length; x++, System.out.println())
				for (int y = 0; y < map[0].length; y++) {
					System.out.print(map[x][y]);
				}
		}
	}

	public BlankEntity[][] getMap() {
		return map;
	}

	public BlankEntity[][] doSimulationStep(int layer) {
		newMap = new BlankEntity[w][h];
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				int nbs = countAliveNeighbours(map, x, y);
				if (map[x][y] instanceof LandscapeEntity)
					switch(layer)
					{
					case 0: // rock
						newMap[x][y] = nbs <= life ? null : new RockEntity(x,
							y); break;
					case 1: // coal
						newMap[x][y] = nbs <= life ? null : new CoalEntity(x,
							y); break;
					case 2: // lava
						newMap[x][y] = nbs <= life ? null : new RealLavaEntity(x,
							y); break;
							
					default: // dirt
						newMap[x][y] = nbs <= life ? null : ( r.nextBoolean() == true ? new LandscapeEntity(x,
								y) : new LandscapeEntity2(x,y)); break;			
					}
				else
				{
					switch(layer)
					{
					case 0: // rock
						newMap[x][y] = nbs > death ? new RockEntity(x, y) : null; break;
					
					case 1: // coal
						newMap[x][y] = nbs > death ? new CoalEntity(x, y) : null; break;
					case 2: // lava
						newMap[x][y] = nbs > death ? new RealLavaEntity(x, y) : null; break;
					default: // dirt
						newMap[x][y] = nbs > death ? ( r.nextBoolean() == true ? new LandscapeEntity(x, y) : new LandscapeEntity2(x,y)) : null; break;
					}		
				
				}
			}
		}
		map = newMap;
		return newMap;
	}

	private int countAliveNeighbours(BlankEntity[][] map, int x, int y) {
		int count = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				int neighbour_x = x + i;
				int neighbour_y = y + j;
				if (i == 0 && j == 0) { // do nothing as this is us!
				} else if (neighbour_x < 0 || neighbour_y < 0
						|| neighbour_x >= map.length
						|| neighbour_y >= map[0].length) {
					count++;
				} else if (map[neighbour_x][neighbour_y] instanceof LandscapeEntity)
					count++;
			}
		}
		return count;
	}
}
