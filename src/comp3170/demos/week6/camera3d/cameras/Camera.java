package comp3170.demos.week6.camera3d.cameras;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.InputManager;
import comp3170.SceneObject;
import static comp3170.Math.TAU;

/**
 * A camera that revolves around the origin.
 * Note that this is just an abstract class that handles movement. 
 * It needs to be extended to implement the projection matrix
 * 
 * @author malcolmryan. Ported to GLFW controls by camedmond
 */

public abstract class Camera {

	private Matrix4f modelMatrix = new Matrix4f();

	private float distance;
	private Vector3f angle;
	
	public Camera(float distance) {
		this.distance = distance;
		angle = new Vector3f(0,0,0);
		modelMatrix.translate(0,0,distance);				
	}
	
	/**
	 * Get the view matrix 
	 */
	public Matrix4f getViewMatrix(Matrix4f dest) {
		// invert the model matrix (assuming we have never applied any scale)
		return modelMatrix.invert(dest);
	}
	
	/**
	 * Get the model matrix for the camera
	 */
	public Matrix4f getCameraMatrix(Matrix4f dest) {
		return modelMatrix.get(dest);
	}
	
	/**
	 * Get the projection matrix (implemented in subclasses)
	 */
	public abstract Matrix4f getProjectionMatrix(Matrix4f dest);
	
	final static float ROTATION_SPEED = TAU / 4;
	final static float MOVEMENT_SPEED = 1;

	public void update(float deltaTime, InputManager input) {
		
		// key controls to orbit camera around the origin
		
		if (input.isKeyDown(GLFW_KEY_UP)) {
			angle.x -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			angle.x += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			angle.y -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			angle.y += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_S)) {
			distance += MOVEMENT_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_W)) {
			distance -= MOVEMENT_SPEED * deltaTime;
		}
		
		modelMatrix.identity();
		modelMatrix.rotateY(angle.y);	// heading
		modelMatrix.rotateX(angle.x);	// pitch
		modelMatrix.translate(0,0,distance);
	}
}
