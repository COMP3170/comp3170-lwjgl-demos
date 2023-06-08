package comp3170.demos.common.demos;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.cameras.Camera;
import comp3170.demos.common.lights.Light;

public class Scene extends SceneObject {

	public static Scene theScene;
	private Camera camera;
	private Light light;  
	
	public Scene() {
		if (theScene != null) {
			throw new IllegalStateException("Two instances of the Scene singleton have been created");
		}
		theScene = this;		
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
