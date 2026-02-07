package comp3170.demos.common.cameras;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_END;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_UP;

import org.joml.Matrix4f;

import comp3170.InputManager;
import comp3170.SceneObject;

/**
 * An implementation of the ICamera interface for a perspective camera.
 * 
 * Note: this camera is implemented as a SceneObject so it can be attached to the scenegraph
 * and moved using getMatrix(). The view matrix is automatically calculated using the modelToWorld matrix
 * of the camera in the scene graph.
 * 
 * Note: The getDirection() method is left as an exercise for the student.
 */

public class PerspectiveCamera extends SceneObject implements ICamera {

	private float fovy;
	private float aspect;
	private float near;
	private float far;

	private float defaultFovy = TAU / 6;
	private final float FOVY_CHANGE = TAU / 6;

	public PerspectiveCamera(float fovy, float aspect, float near, float far) {
		this.fovy = fovy;
		this.aspect = aspect;
		this.near = near;
		this.far = far;
		
		defaultFovy = fovy;
	}

	@Override
	public Matrix4f getProjectionMatrix(Matrix4f dest) {
		return dest.setPerspective(fovy, aspect, near, far);
	}

	@Override
	public Matrix4f getViewMatrix(Matrix4f dest) {

		// invert the model matrix and remove scale
		getModelToWorldMatrix(dest);
		dest.invert();
		dest.normalize3x3();
		
		return dest;
	}

	/**
	 * Get the model to world matrix for the camera
	 */
	
	@Override
	public Matrix4f getModelMatrix(Matrix4f dest) {
		return getModelToWorldMatrix(dest);
	}

	/**
	 * Press page up / page down to change the FOV. Press end to restore the default FOV 
	 */
	
	public void update(float deltaTime, InputManager input) {
		if (input.isKeyDown(GLFW_KEY_PAGE_DOWN)) {
			fovy += FOVY_CHANGE * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_END)) {
			fovy = defaultFovy;
		}
		if (input.isKeyDown(GLFW_KEY_PAGE_UP)) {
			fovy -= FOVY_CHANGE * deltaTime;
		}

	}



}
