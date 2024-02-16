package comp3170.demos.week8.sceneobjects;

import static comp3170.Math.TAU;

import java.awt.Color;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.cameras.Camera;
import comp3170.demos.common.sceneobjects.Axes3D;
import comp3170.demos.common.sceneobjects.Grid;
import comp3170.demos.week8.cameras.PerspectiveCamera;

public class DepthScene extends SceneObject {

	private PerspectiveCamera camera;

	public DepthScene() {
		Axes3D axes = new Axes3D();
		axes.setParent(this);
		Grid grid = new Grid(10);
		grid.setParent(this);

		Triangle redTriangle = new Triangle(Color.red);
		redTriangle.setParent(this);
		Triangle blueTriangle = new Triangle(Color.blue);
		blueTriangle.setParent(this); blueTriangle.getMatrix().rotateY(TAU/12);

		camera = new PerspectiveCamera();

	}
	
	public Camera getCamera() {
		return camera;
	}

	public void update(float deltaTime, InputManager input) {
		camera.update(deltaTime, input);
	}
}
