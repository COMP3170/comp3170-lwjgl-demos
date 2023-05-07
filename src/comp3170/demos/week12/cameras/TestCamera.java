package comp3170.demos.week12.cameras;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.InputManager;

public class TestCamera implements Camera {

	private static final float WIDTH = 20;
	private static final float HEIGHT = 20;
	private static final float NEAR = 0.1f;
	private static final float FAR = 100f;
	private float distance = 20f;
	
	private Matrix4f cameraMatrix = new Matrix4f();
	
	public TestCamera() {
		
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
		return dest.setOrthoSymmetric(WIDTH, HEIGHT, NEAR, FAR);
	}
	
	@Override
	public Vector4f getViewVector(Vector4f dest) {
		return cameraMatrix.getColumn(2, dest);
	}

	private static final float ROTATION_SPEED = TAU/6;
	private static final float MOVEMENT_SPEED = 4f;
	private Vector3f angle = new Vector3f(0,0,0);

	public void update(InputManager input, float deltaTime) {
		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			angle.y = (angle.y + ROTATION_SPEED * deltaTime) % TAU;
		}
		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			angle.y = (angle.y - ROTATION_SPEED * deltaTime) % TAU;
		}
		if (input.isKeyDown(GLFW_KEY_UP)) {
			angle.x = (angle.x + ROTATION_SPEED * deltaTime) % TAU;
		}
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			angle.x = (angle.x - ROTATION_SPEED * deltaTime) % TAU;
		}
		if (input.isKeyDown(GLFW_KEY_PAGE_DOWN)) {
			distance += MOVEMENT_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_PAGE_UP)) {
			distance -= MOVEMENT_SPEED * deltaTime;
		}

		cameraMatrix.identity();
		cameraMatrix.rotateY(angle.y);
		cameraMatrix.rotateX(angle.x);
		cameraMatrix.rotateZ(angle.z);
		cameraMatrix.translate(0, 0, distance);
	}

	
}
