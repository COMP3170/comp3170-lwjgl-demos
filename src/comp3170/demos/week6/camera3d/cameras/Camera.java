package comp3170.demos.week6.camera3d.cameras;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import comp3170.InputManager;
import comp3170.SceneObject;

/**
 * A camera that revolves around the origin
 * @author malcolmryan. Ported to GLFW controls by camedmond
 *
 */

public abstract class Camera extends SceneObject {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto

	private Matrix4f modelMatrix;

	private float distance;
	private Vector3f angle;
	
	public Camera(float distance) {
		this.distance = distance;
		this.modelMatrix = new Matrix4f();
		this.angle = new Vector3f(0,0,0);

		modelMatrix.translate(0,0,distance);		
		
	}
	
	public Matrix4f getViewMatrix(Matrix4f dest) {
		// invert the model matrix (we have never applied any scale)
		return modelMatrix.invert(dest);
	}
	
	abstract public Matrix4f getProjectionMatrix(Matrix4f dest);
	
	final static float ROTATION_SPEED = TAU / 4;
	final static float MOVEMENT_SPEED = 1;

	public void update(InputManager input, float deltaTime) {
		
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
