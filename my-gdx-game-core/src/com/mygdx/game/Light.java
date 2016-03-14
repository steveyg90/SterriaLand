package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;

import box2dLight.PointLight;
import box2dLight.RayHandler;


public class Light extends PointLight {

	public Light(RayHandler ray, int rays, Color col, float distance, float x, float y)
	{
		super(ray, rays, col, distance, x, y);
	}
	
	public int mapX, mapY;
	public float screenX, screenY;
	
	public int duration;
	
}

