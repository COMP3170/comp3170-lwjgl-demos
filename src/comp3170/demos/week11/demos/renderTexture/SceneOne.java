package comp3170.demos.week11.demos.renderTexture;

import comp3170.demos.week11.sceneobjects.TexturedCube;

public class SceneOne extends Scene {

	public SceneOne(int renderTexture) {
		super();

		TexturedCube cube = new TexturedCube(renderTexture);
		cube.setParent(this);
	}

}
