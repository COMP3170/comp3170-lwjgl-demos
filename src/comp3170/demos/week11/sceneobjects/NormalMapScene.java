package comp3170.demos.week11.sceneobjects;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.lights.Light;
import comp3170.demos.week11.cameras.Camera;
import comp3170.demos.week11.cameras.OrbitingCamera;
import comp3170.demos.week11.lights.DirectionalLight;

public class NormalMapScene extends SceneObject {

	public static NormalMapScene theScene = null;
	private OrbitingCamera camera;
	private DirectionalLight light;
	private NormalMapQuad quad;

	public NormalMapScene() {
		theScene = this;

		camera = new OrbitingCamera();
		light = new DirectionalLight();
		light.setParent(this);

		quad = new NormalMapQuad();
		quad.setParent(this);
	}

	public Camera getCamera() {
		return camera;
	}

	public Light getLight() {
		return light;
	}

	public void update(float deltaTime, InputManager input) {
		camera.update(input, deltaTime);
		light.update(input, deltaTime);
		quad.update(input, deltaTime);
	}

}
