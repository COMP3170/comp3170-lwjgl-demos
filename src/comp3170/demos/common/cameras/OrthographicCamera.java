package comp3170.demos.common.cameras;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_UP;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.InputManager;
import comp3170.SceneObject;

/**
 * An implementation of the ICamera interface for an orthographic camera.
 * 
 * Note: this camera is implemented as a SceneObject so it can be attached to the scenegraph
 * and moved using getMatrix(). The view matrix is automatically calculated using the modelToWorld matrix
 * of the camera in the scene graph.
 */

public class OrthographicCamera extends SceneObject implements ICamera {

	private float width;
	private float height;
	private float near;
	private float far;

	private final float ZOOM_CHANGE = 2f;

	public OrthographicCamera(float width, float height, float near, float far) {
		this.width = width;
		this.height = height;
		this.near = near;
		this.far = far;
	}

	/**
	 * Calculate the orthographic projection matrix based on the camera parameters
	 */
	@Override
	public Matrix4f getProjectionMatrix(Matrix4f dest) {
		return dest.setOrtho(-width / 2, width / 2, -height / 2, height / 2, near, far);
	}

	private Matrix4f modelMatrix = new Matrix4f();

	/**
	 * Calculate the view matrix based on the scene graph
	 */
	
	@Override
	public Matrix4f getViewMatrix(Matrix4f dest) {

		// invert the model matrix and remove scale
		getModelToWorldMatrix(modelMatrix);
		modelMatrix.invert(dest);
		dest.normalize3x3();
		
		return dest;
	}

	/**
	 * Get the orthorgraphic direction vector in world coordinates.
	 */
	
	@Override
	public Vector4f getDirection(Vector4f dest) {
		// for an orthographic camera, the direction is the same everywhere
		// and is equal to the local z-axis of the camera 
		getModelToWorldMatrix(modelMatrix);
		return dest.set(0,0,1,0).mul(modelMatrix);
	}

	/**
	 * Use page up / page down to control the field of view of the camera
	 */
	
	public void update(float deltaTime, InputManager input) {

		if (input.isKeyDown(GLFW_KEY_PAGE_UP)) {
			width += ZOOM_CHANGE * deltaTime;
			height += ZOOM_CHANGE * deltaTime;
		}

		if (input.isKeyDown(GLFW_KEY_PAGE_DOWN)) {
			width -= ZOOM_CHANGE * deltaTime;
			height -= ZOOM_CHANGE * deltaTime;
		}
	}
}
