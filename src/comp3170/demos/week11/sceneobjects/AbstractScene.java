package comp3170.demos.week11.sceneobjects;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.week11.cameras.Camera;
import comp3170.demos.week11.cameras.OrbitingCamera;

public abstract class AbstractScene extends SceneObject {
	private OrbitingCamera camera;

	public AbstractScene() {
		camera = new OrbitingCamera();
	}

	public Camera getCamera() {
		return camera;
	}

	public void update(InputManager input, float deltaTime) {
		camera.update(input, deltaTime);
	}

}
