package comp3170.demos.misc.ssao;

import java.awt.Color;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.cameras.Camera;
import comp3170.demos.common.lights.Light;
import comp3170.demos.misc.ssao.cameras.OrbitingCamera;
import comp3170.demos.misc.ssao.sceneobjects.Cube;
import comp3170.demos.misc.ssao.sceneobjects.Plane;

public class Scene extends SceneObject {

	public static Scene theScene;
	private Camera camera;
	private Light light;  
	
	public Scene() {
		if (theScene != null) {
			throw new IllegalStateException("Two instances of the Scene singleton have been created");
		}
		theScene = this;
		
		Plane plane = new Plane(Color.gray);
		plane.setParent(this);
		
		Cube cube = new Cube(Color.red);
		cube.setParent(plane);
		cube.getMatrix().translate(0,1,0);
		
		camera = new OrbitingCamera();
	}
	
	public Camera getCamera() {
		return camera;
	}

	public Light getLight() {
		return light;
	}

	public void update(InputManager input, float deltaTime) {
		camera.update(input, deltaTime);	
	}	
}
