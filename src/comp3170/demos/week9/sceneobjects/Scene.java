package comp3170.demos.week9.sceneobjects;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.lights.Light;
import comp3170.demos.common.sceneobjects.Axes3D;
import comp3170.demos.common.sceneobjects.Grid;
import comp3170.demos.week9.cameras.Camera;
import comp3170.demos.week9.cameras.OrthographicCamera;
import comp3170.demos.week9.lights.DirectionalLight;

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
		
		Axes3D axes = new Axes3D();
		axes.setParent(grid);

		cylinder = new Cylinder();
		cylinder.setParent(grid);
		cylinder.getMatrix().scale(0.75f, 1f, 0.75f);
		
		camera = new OrthographicCamera();
		light = new DirectionalLight();
		light.setParent(this);
	}
	
	public Camera getCamera() {
		return camera;
	}

	public Light getLight() {
		return light;
	}
	
	public void update(InputManager input, float deltaTime) {
		camera.update(deltaTime, input);
		light.update(input, deltaTime);
		cylinder.update(input, deltaTime);
	}
	
}
