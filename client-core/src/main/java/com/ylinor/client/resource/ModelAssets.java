package com.ylinor.client.resource;

import com.badlogic.gdx.graphics.g3d.Model;

public class ModelAssets {

	private Assets assets;
	
	public Model testPlayer;

	public ModelAssets(Assets assets) {
		this.assets = assets;
	}

	public void load() {
		assets.loadModel("perso_course2.g3dj");
	}
	
	public Model getModelTest() {
		return assets.get("perso_course2.g3dj");
	}
}
