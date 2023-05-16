package comp3170.demos.week12.cameras;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.InputManager;

public class OffsetCamera implements Camera {

	private static final float ASPECT = 1;
	private static final float FOVY = TAU / 4;
	private static final float NEAR = 1f;
	private static final float FAR = 30f;
	private static final float HEIGHT = 2f;
	
	private Matrix4f cameraMatrix = new Matrix4f();
	private float offAngleX = 0;
	private float offAngleY = 0;
	
	public OffsetCamera() {
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
		return dest.setPerspectiveOffCenter(FOVY, offAngleX , offAngleY, ASPECT, NEAR, FAR);
	}
	
	@Override
	public Vector4f getViewVector(Vector4f dest) {
		// for a perspective camera
		// the view vector is the origin point of the cameraMatrix
		return cameraMatrix.getColumn(3, dest);
	}

	private static final float ROTATION_SPEED = TAU/6;

	public void update(InputManager input, float deltaTime) {
		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			offAngleX += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			offAngleX -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_UP)) {
			offAngleY += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			offAngleY -= ROTATION_SPEED * deltaTime;
		}
	}

}
