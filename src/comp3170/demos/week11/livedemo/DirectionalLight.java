package comp3170.demos.week11.livedemo;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_I;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_J;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_K;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_L;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.InputManager;

public class DirectionalLight {

	private Vector3f ambientIntensity = new Vector3f(0, 0, 0.1f);	// Tyla's blue
	private Vector3f lightIntensity = new Vector3f(1, 1, 1);	// Tyla's blue
	private Matrix4f lightMatrix = new Matrix4f();	// Tyla's blue

	
	public Vector3f getAmbient(Vector3f dest) {
		return dest.set(ambientIntensity);
	}

	public Vector3f getIntensity(Vector3f dest) {
		return dest.set(lightIntensity);
	}

	public Vector4f getDirection(Vector4f dest) {
		// return the k vector
		return lightMatrix.getColumn(2, dest);
	}

	private final static float ROTATION_SPEED = TAU / 6;

	public void update(InputManager input, float deltaTime) {
		if (input.isKeyDown(GLFW_KEY_I)) {
			lightMatrix.rotateX(ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_K)) {
			lightMatrix.rotateX(-ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_J)) {
			lightMatrix.rotateY(ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_L)) {
			lightMatrix.rotateY(-ROTATION_SPEED * deltaTime);
		}
	}
}
