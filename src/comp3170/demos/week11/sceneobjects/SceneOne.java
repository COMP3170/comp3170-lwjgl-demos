package comp3170.demos.week11.sceneobjects;

public class SceneOne extends AbstractScene {

	public SceneOne(int renderTexture) {
		super();

		TexturedCube cube = new TexturedCube(renderTexture);
		cube.setParent(this);
	}

}
