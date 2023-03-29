package comp3170.demos.week7.cameras;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import org.joml.Matrix4f;

import comp3170.InputManager;

public class PerspectiveCamera implements Camera {

	private static final float FOVY = TAU / 10;
	private static final float ASPECT = 1;
	private static final float NEAR = 0.1f;
	private static final float FAR = 100f;
	private static final float DISTANCE = 4f;
	
	private Matrix4f cameraRotation = new Matrix4f();
	private Matrix4f cameraMatrix = new Matrix4f();
	
	public PerspectiveCamera() {
		
	}
		
	@Override
	public Matrix4f getViewMatrix(Matrix4f dest) {
		return cameraMatrix.invert(dest);
	}

	@Override
	public Matrix4f getProjectionMatrix(Matrix4f dest) {
		return dest.setPerspective(FOVY, ASPECT, NEAR, FAR);
	}

	private static final float ROTATION_SPEED = TAU/6;

	public void update(InputManager input, float deltaTime) {
		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			cameraRotation.rotateY(ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			cameraRotation.rotateY(-ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_UP)) {
			cameraRotation.rotateLocalX(ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			cameraRotation.rotateLocalX(-ROTATION_SPEED * deltaTime);
		}
		
		cameraRotation.translate(0,0,DISTANCE, cameraMatrix);
	}
	
}
