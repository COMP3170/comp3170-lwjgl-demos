package comp3170.demos.common.lights;

import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * A standard interface for a light source
 */

public interface ILight {
	
	/**
	 * Get the source 'vector' for use in lighting calculations.
	 * This might be a vector or a point depending on the kind of light source.
	 * @param dest
	 * @return
	 */
	Vector4f getSourceVector(Vector4f dest);
	
	/**
	 * Get the intensity of the light as an (R,G,B) vector with values between 0 and 1. 
	 * @param dest
	 * @return
	 */
	Vector3f getIntensity(Vector3f dest);

	/**
	 * Get the ambient intensity of the light as an (R,G,B) vector with values between 0 and 1. 
	 * @param dest
	 * @return
	 */
	Vector3f getAmbient(Vector3f dest);
}
