package comp3170.demos.week11.demos.normalMap;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.lights.ILight;
import comp3170.demos.week11.cameras.Camera;
import comp3170.demos.week11.lights.OrbittingDirectionalLight;
import comp3170.demos.week11.sceneobjects.NormalMapQuad;

public class NormalMapScene extends SceneObject {

	public static NormalMapScene theScene = null;
	private Camera camera;
	private OrbittingDirectionalLight light;
	private NormalMapQuad quad;

	public NormalMapScene() {
		theScene = this;

		camera = new Camera();
		light = new OrbittingDirectionalLight();
		light.setParent(this);

		quad = new NormalMapQuad();
		quad.setParent(this);
	}

	public Camera getCamera() {
		return camera;
	}

	public ILight getLight() {
		return light;
	}

	public void update(float deltaTime, InputManager input) {
		camera.update(deltaTime, input);
		light.update(deltaTime, input);
		quad.update(deltaTime, input);
	}

}
