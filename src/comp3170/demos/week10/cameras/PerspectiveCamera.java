package comp3170.demos.week10.cameras;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.InputManager;

public class PerspectiveCamera implements Camera {

	private float fovy = TAU / 10;
	private float aspect = 1;
	private float near = 0.1f;
	private float far = 100f;
	private float distance = 5f;

	private Matrix4f cameraRotation = new Matrix4f();
	private Matrix4f cameraMatrix = new Matrix4f();

	public PerspectiveCamera() {
		// default settings
	}

	@Override
	public Matrix4f getViewMatrix(Matrix4f dest) {
		return cameraMatrix.invert(dest);
	}

	@Override
	public Matrix4f getProjectionMatrix(Matrix4f dest) {
		return dest.setPerspective(fovy, aspect, near, far);
	}

	private static final float ROTATION_SPEED = TAU/6;
	private static final float MOVEMENT_SPEED = 5;

	public void update(InputManager input, float deltaTime) {
		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			cameraRotation.rotateLocalY(ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			cameraRotation.rotateLocalY(-ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			cameraRotation.rotateX(ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_UP)) {
			cameraRotation.rotateX(-ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_S))
		{
			distance = distance + MOVEMENT_SPEED * deltaTime;
		}

		if (input.isKeyDown(GLFW_KEY_W))
		{
			distance = distance - MOVEMENT_SPEED * deltaTime;
		}

		cameraRotation.translate(0,0,distance, cameraMatrix);
	}

	@Override
	public Vector4f getViewVector(Vector4f dest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix4f getCameraMatrix(Matrix4f dest) {
		// TODO Auto-generated method stub
		return null;
	}

}
