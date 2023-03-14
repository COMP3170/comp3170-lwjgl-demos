package comp3170.demos.week6.camera3d.cameras;

import java.awt.event.KeyEvent;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import comp3170.InputManager;

/**
 * A camera that revolves around the origin
 * @author malcolmryan
 *
 */

public abstract class Camera {

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
		
		if (input.isKeyDown(KeyEvent.VK_UP)) {
			angle.x -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(KeyEvent.VK_DOWN)) {
			angle.x += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(KeyEvent.VK_LEFT)) {
			angle.y -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(KeyEvent.VK_RIGHT)) {
			angle.y += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(KeyEvent.VK_PAGE_DOWN)) {
			distance += MOVEMENT_SPEED * deltaTime;
		}
		if (input.isKeyDown(KeyEvent.VK_PAGE_UP)) {
			distance -= MOVEMENT_SPEED * deltaTime;
		}
		
		modelMatrix.identity();
		modelMatrix.rotateY(angle.y);	// heading
		modelMatrix.rotateX(angle.x);	// pitch
		modelMatrix.translate(0,0,distance);
	}
}
