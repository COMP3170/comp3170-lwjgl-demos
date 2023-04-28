package comp3170.demos.week7.cameras;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import comp3170.InputManager;

public class OrthographicCamera implements Camera {

	private static final float WIDTH = 3;
	private static final float HEIGHT = 3;
	private static final float NEAR = 0.1f;
	private static final float FAR = 10f;
	private static final float DISTANCE = 5f;
	private static final float ELEVATION = 0f;
	
	private Matrix4f cameraMatrix = new Matrix4f();
	
	public OrthographicCamera() {
		
	}
		
	@Override
	public Matrix4f getViewMatrix(Matrix4f dest) {
		return cameraMatrix.invert(dest);
	}

	@Override
	public Matrix4f getProjectionMatrix(Matrix4f dest) {
		return dest.setOrthoSymmetric(WIDTH, HEIGHT, NEAR, FAR);
	}

	private static final float ROTATION_SPEED = TAU/6;
	private Vector3f angle = new Vector3f(0,0,0);

	public void update(InputManager input, float deltaTime) {
		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			angle.y += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			angle.y -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_UP)) {
			angle.x += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			angle.x -= ROTATION_SPEED * deltaTime;
		}

		cameraMatrix.identity();
		cameraMatrix.translate(0, ELEVATION, 0);
		cameraMatrix.rotateY(angle.y);
		cameraMatrix.rotateX(angle.x);
		cameraMatrix.rotateZ(angle.z);
		cameraMatrix.translate(0, 0, DISTANCE);
	}
	
}
