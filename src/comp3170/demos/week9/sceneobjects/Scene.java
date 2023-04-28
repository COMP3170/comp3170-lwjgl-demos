package comp3170.demos.week9.sceneobjects;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.week9.cameras.Camera;
import comp3170.demos.week9.cameras.OrthographicCamera;
import comp3170.demos.week9.lights.DirectionalLight;
import comp3170.demos.week9.lights.Light;

public class Scene extends SceneObject {

	public static Scene theScene = null;
	private OrthographicCamera camera;
	private DirectionalLight light;
	private Cylinder cylinder;
	
	public Scene() {
		// simple implementation of the Singleton pattern
		// to allow scene objects to access camera and light
		theScene = this;
		
		Grid grid = new Grid(10);
		grid.setParent(this);

		cylinder = new Cylinder();
		cylinder.setParent(grid);
		
		camera = new OrthographicCamera();
		light = new DirectionalLight();
	}
	
	public Camera getCamera() {
		return camera;
	}

	public Light getLight() {
		return light;
	}
	
	public void update(InputManager input, float deltaTime) {
		camera.update(input, deltaTime);
		light.update(input, deltaTime);
		cylinder.update(input, deltaTime);
	}
	
}
