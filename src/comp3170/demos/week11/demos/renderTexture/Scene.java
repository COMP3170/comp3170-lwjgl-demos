package comp3170.demos.week11.demos.renderTexture;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.cameras.ICamera;
import comp3170.demos.week11.cameras.Camera;

public abstract class Scene extends SceneObject {
	private Camera camera;

	public Scene() {
		camera = new Camera();
	}

	public ICamera getCamera() {
		return camera;
	}

	public void update(float deltaTime, InputManager input) {
		camera.update(deltaTime, input);
	}

}
