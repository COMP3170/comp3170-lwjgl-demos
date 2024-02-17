package comp3170.demos.common.demos;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.cameras.Camera;
import comp3170.demos.common.lights.Light;
import comp3170.demos.common.sceneobjects.Axes3D;

/**
 * Template code for a Scene.
 *
 * @author malcolmryan
 *
 */

public class DemoScene extends SceneObject {

	public static DemoScene theScene;
	private Camera camera;
	private Light light;

	public DemoScene() {
		if (theScene != null) {
			throw new IllegalStateException("Two instances of the Scene singleton have been created");
		}
		theScene = this;

		Axes3D axes = new Axes3D();
		axes.setParent(this);
	}

	public Camera getCamera() {
		return camera;
	}

	public Light getLight() {
		return light;
	}

	public void update(InputManager input, float deltaTime) {
		camera.update(deltaTime, input);
	}
}
