package comp3170.demos.week8.sceneobjects;

import static comp3170.Math.TAU;

import java.awt.Color;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.cameras.ICamera;
import comp3170.demos.common.sceneobjects.Axes3D;
import comp3170.demos.common.sceneobjects.Grid;
import comp3170.demos.week8.cameras.Camera;

public class TransparencyScene extends SceneObject {

	private Camera camera;

	public TransparencyScene() {
		Axes3D axes = new Axes3D();
		axes.setParent(this);
		Grid grid = new Grid(10);
		grid.setParent(this);

		Color red = new Color(1f,0,0,0.5f);
		Color blue = new Color(0,0,1f,0.5f);
		Color green = new Color(0,1f,0,0.5f);

		Triangle redTriangle = new Triangle(red);
		redTriangle.setParent(this);
		Triangle blueTriangle = new Triangle(blue);
		blueTriangle.setParent(this); blueTriangle.getMatrix().rotateY(TAU/12);
		Triangle greenTriangle = new Triangle(green);
		greenTriangle.setParent(this); greenTriangle.getMatrix().rotateY(TAU/6);

		camera = new Camera();

	}

	public ICamera getCamera() {
		return camera;
	}

	public void update(float deltaTime, InputManager input) {
		camera.update(deltaTime, input);
	}
}
