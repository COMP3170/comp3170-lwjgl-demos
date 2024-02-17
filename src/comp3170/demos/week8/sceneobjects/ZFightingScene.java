package comp3170.demos.week8.sceneobjects;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;

import java.awt.Color;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.cameras.Camera;
import comp3170.demos.week8.cameras.PerspectiveCamera;

public class ZFightingScene extends SceneObject {

	private Quad blueQuad;
	private Camera camera;

	public ZFightingScene() {
		Quad redQuad = new Quad(Color.red);
		redQuad.setParent(this);

		blueQuad = new Quad(Color.blue);
		blueQuad.setParent(this);
		blueQuad.getMatrix().rotateY(0.0001f);

		camera = new PerspectiveCamera();
	}

	public Camera getCamera() {
		return camera;
	}

	private static final float ROTATION_SPEED = TAU/80;

	public void update(float deltaTime, InputManager input) {

		camera.update(deltaTime, input);

		if (input.isKeyDown(GLFW_KEY_A)) {
			blueQuad.getMatrix().rotateY(ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			blueQuad.getMatrix().rotateY(-ROTATION_SPEED * deltaTime);
		}
	}
}
