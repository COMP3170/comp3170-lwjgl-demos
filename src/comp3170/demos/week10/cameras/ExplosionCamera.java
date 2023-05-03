package comp3170.demos.week10.cameras;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.InputManager;

public class ExplosionCamera implements Camera {

	private static final float ASPECT = 1;
	private static final float FOVY = TAU / 4;
	private static final float NEAR = 0.1f;
	private static final float FAR = 30f;
	private static final float DISTANCE = 15f;
	private static final float ELEVATION = 0f;
	
	private Matrix4f cameraMatrix = new Matrix4f();
	
	public ExplosionCamera() {
		
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
		// the view vector is the k-axis of the cameraMatrix
		return cameraMatrix.getColumn(2, dest);
	}

	private static final float ROTATION_SPEED = TAU/6;
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

		cameraMatrix.identity();
		cameraMatrix.translate(0, ELEVATION, 0);
		cameraMatrix.rotateY(angle.y);
		cameraMatrix.rotateX(angle.x);
		cameraMatrix.rotateZ(angle.z);
		cameraMatrix.translate(0, 0, DISTANCE);
	}

	
}
