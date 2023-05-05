package comp3170.demos.week12.sceneobjects;

import java.awt.Color;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.week12.cameras.Camera;
import comp3170.demos.week12.cameras.OrbitingCamera;
import comp3170.demos.week9.sceneobjects.Axes3D;

public class ReflectionScene extends SceneObject {

	public static ReflectionScene theScene = null;
	private OrbitingCamera camera;
	private Mirror mirror;
	
	public ReflectionScene() {
		theScene = this;
		
		camera = new OrbitingCamera();
		Axes3D axes = new Axes3D();
		axes.setParent(this);
		
		mirror = new Mirror();
		mirror.setParent(this);
		
		Cube redCube = new Cube(Color.RED);
		redCube.setParent(this);
		redCube.getMatrix().translation(2, 1, 1).scale(0.5f);		
	}
	
	public Camera getCamera() {
		return camera;
	}

	public void update(InputManager input, float deltaTime) {
		camera.update(input, deltaTime);		
	}


}
