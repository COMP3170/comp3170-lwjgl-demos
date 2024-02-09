package comp3170.demos.week6.backface;

import static comp3170.Math.TAU;

import java.awt.Color;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.cameras.Camera;
import comp3170.demos.common.cameras.PerspectiveOrbittingCamera;
import comp3170.demos.common.sceneobjects.Axes3D;
import comp3170.demos.common.sceneobjects.Grid;

public class Scene extends SceneObject {
	private Camera camera;

	public Scene() {		
		
		// Set up the scene
		Grid grid = new Grid(11);
		grid.setParent(this);
		
		Axes3D axes = new Axes3D();
		axes.setParent(this);
		
		Triangle triangle = new Triangle(Color.YELLOW);
		triangle.setParent(this);
		
		camera = new PerspectiveOrbittingCamera(2, TAU/6, 1, 0.1f, 10f);	
	}

	public Camera getCamera() {
		return camera;
	}

	public void update(float deltaTime, InputManager input) {
		camera.update(deltaTime, input);		
	}

}
