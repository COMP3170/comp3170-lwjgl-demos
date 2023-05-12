package comp3170.demos.week12.cameras;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.InputManager;

public class OverheadCamera implements Camera {

	private static final float WIDTH = 10;
	private static final float HEIGHT = 10;
	private static final float NEAR = 0.1f;
	private static final float FAR = 10f;
	private static final float ELEVATION = 5f;
	
	private Matrix4f cameraMatrix = new Matrix4f();
	
	public OverheadCamera() {
		cameraMatrix.identity();
		cameraMatrix.translate(0, ELEVATION, 0);
		cameraMatrix.rotateX(-TAU / 4);
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
		// the view vector is the k-axis of the cameraMatrix
		return cameraMatrix.getColumn(2, dest);
	}
}
