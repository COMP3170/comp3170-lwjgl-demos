package comp3170.demos.week7.sceneobjects;

import java.awt.Color;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.sceneobjects.Axes3D;
import comp3170.demos.common.sceneobjects.Grid;
import comp3170.demos.week7.cameras.DepthSceneCamera;

public class FogScene extends SceneObject {


	private DepthSceneCamera camera;

	public FogScene() {
		Grid grid = new Grid(10);
		grid.setParent(this);
		grid.getMatrix().scale(10);

		Axes3D axes = new Axes3D();
		axes.setParent(this);

		Cube redCube = new Cube(Color.red);
		redCube.setParent(this);
		redCube.getMatrix().translate(2,0.1f,1).scale(0.1f);

		camera = new DepthSceneCamera();
	}

	public DepthSceneCamera getCamera() {
		return camera;
	}

	public void update(float deltaTime, InputManager input) {
		camera.update(deltaTime, input);
	}

}
