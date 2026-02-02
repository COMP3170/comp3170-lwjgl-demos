package comp3170.demos.common.cameras;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.InputManager;

/**
 * A standard interface for all cameras
 */

public interface ICamera {
	/**
	 * Get the view matrix for this camera in 'dest'
	 * 
	 * @param dest A pre-allocated matrix. 
	 * @return 
	 */
	public Matrix4f getViewMatrix(Matrix4f dest);
	
	/**
	 * Get the projection matrix for this camera in 'dest'
	 * 
	 * @param dest A pre-allocated matrix. 
	 * @return
	 */
	public Matrix4f getProjectionMatrix(Matrix4f dest);

	/**
	 * Get the 'direction' vector for this camera in world coordinates to compute lighting.
	 * Note: for a perspective camera, this should be a point, not a vector.
	 * 
	 * @param dest A pre-allocated matrix. 
	 * @return
	 */

	public Vector4f getDirection(Vector4f dest);

	/**
	 * Handle any per-frame updates & input
	 * 
	 * @param dest A pre-allocated matrix. 
	 * @return
	 */

	public void update(float deltaTime, InputManager input);
}
