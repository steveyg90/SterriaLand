package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

public class SoundFx implements Disposable {

	static final Sound digDirt = Gdx.audio.newSound(Gdx.files
	         .internal("../my-gdx-game-core/data/coal.mp3"));
	
	
	static final Sound MainTrack = Gdx.audio.newSound(Gdx.files
	         .internal("../my-gdx-game-core/data/Retribution.mp3"));

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		digDirt.dispose();
		MainTrack.dispose();
		
		System.out.println("All music disposed");
	}

}
