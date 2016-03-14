package com.mygdx.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class FloodFill {

	BlankEntity[][] map;

	private CaveCell cell;

	public ArrayList<CaveCell> caveCells = new ArrayList<CaveCell>();

	public FloodFill() {
	}

	public FloodFill(BlankEntity[][] map) {
		this.map = map;
	}

	/*
	 * Recursive flood fill - CANNOT handle large caves
	 */
	public void fill(int x, int y) {

		try {
			if (x < 0)
				return;
			if (y < 0)
				return;
			if (x >= map.length)
				return;
			if (y >= map[x].length)
				return;

			BlankEntity currentTile = map[x][y]; // get current tile
			if (currentTile == null)
				return;

			if (currentTile.visited==1)
				return;

			if (!(currentTile instanceof CaveEntity))
				return;

			currentTile.visited = 1;//true;

			cell = new CaveCell(x, y);
			caveCells.add(cell);

			fill(x - 1, y);
			fill(x + 1, y);
			fill(x, y - 1);
			fill(x, y + 1);
		} catch (Exception ex) {
			System.out.println(":-( Cave too large");
		}
	}

	
    /*
     * Iterative Flood fill - stops stack overflow :-) We use this one
     */
	public void iterativeFill(int initialX, int initialY) {
		Queue<Point> points = new LinkedList<Point>();
		points.add(new Point(initialX, initialY));
		caveCells = new ArrayList<CaveCell>();
		
		while (!points.isEmpty()) {
			Point currentPoint = points.remove();
			int x = currentPoint.x;
			int y = currentPoint.y;

			BlankEntity currentTile = map[x][y]; // get current tile
	
			if(currentTile instanceof CaveEntity && currentTile.visited==0)
			{
				cell = new CaveCell(x, y);
				caveCells.add(cell);
				currentTile.visited = 1;//true;
				
				if (x < map.length - 1 )
					points.add(new Point(x + 1, y));
				if (x > 0)
					points.add(new Point(x - 1, y));
				if (y < map[x].length -1)
					points.add(new Point(x, y + 1));
				if (y > 0)
					points.add(new Point(x, y - 1));
			}
		}
	}
}


class CaveCell {
	public int x, y;

	public CaveCell(int x, int y) {
		this.x = x;
		this.y = y;
	}

/*	@Override
	public String toString() {
		return "X:" + x + " Y:" + y;
	}*/
}

class Cave {
	
	static final int CHANCEOFWATER = 4;      // 1-fantastic 5-none
	public Cave() {
		cells = new ArrayList<CaveCell>();
		Random r = new Random();
		isWaterCave = r.nextInt(5) >=CHANCEOFWATER ? true : false;   
	}

	public boolean isWaterCave;
	
	public ArrayList<CaveCell> cells;
}
