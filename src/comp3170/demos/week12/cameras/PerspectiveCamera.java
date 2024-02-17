package comp3170.demos.week12.cameras;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.InputManager;

public class PerspectiveCamera implements Camera {

	private static final float ASPECT = 1;
	private static final float FOVY = TAU / 4;
	private static final float NEAR = 0.1f;
	private static final float FAR = 30f;
	private static final float HEIGHT = 2f;

	private Matrix4f cameraMatrix = new Matrix4f();

	public PerspectiveCamera() {
		cameraMatrix.translate(0, HEIGHT, 0);
	}

	@Override
	public Matrix4f getCameraMatrix(Matrix4f dest) {
		return dest.set(cameraMatrix);
	}

	@Override
	public Matrix4f getViewMatrix(Matrix4f dest) {
		return cameraMatrix.invert(dest);
	}

	@Override
	public Matrix4f getProjectionMatrix(Matrix4f dest) {
		return dest.setPerspective(FOVY, ASPECT, NEAR, FAR);
	}

	@Override
	public Vector4f getViewVector(Vector4f dest) {
		// for a perspective camera
		// the view vector is the origin point of the cameraMatrix
		return cameraMatrix.getColumn(3, dest);
	}

	private static final float ROTATION_SPEED = TAU/6;
	private static final float MOVEMENT_SPEED = 4f;

	public void update(InputManager input, float deltaTime) {
		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			cameraMatrix.rotateY(ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			cameraMatrix.rotateY(-ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_UP)) {
			cameraMatrix.translate(0,0,-MOVEMENT_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			cameraMatrix.translate(0,0,MOVEMENT_SPEED * deltaTime);
		}
	}


}
