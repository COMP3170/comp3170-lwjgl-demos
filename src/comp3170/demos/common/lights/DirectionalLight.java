package comp3170.demos.common.lights;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.SceneObject;

public class DirectionalLight extends SceneObject implements ILight {

	private Vector3f intensity = new Vector3f(1,1,1);	// RGB white
	private Vector3f ambient = new Vector3f(0.1f,0.1f,0.1f); // RGB dim white
	private Matrix4f modelMatrix = new Matrix4f();

	public DirectionalLight() {

	}

	@Override
	public Vector4f getSourceVector(Vector4f dest) {
		// compute the direction vector using the modelToWorld matrix
		// the direction vector is equal to the z axis of the light in world space
		
		getModelToWorldMatrix(modelMatrix);
		return dest.set(0,0,1,0).mul(modelMatrix);
	}

	@Override
	public Vector3f getIntensity(Vector3f dest) {
		return dest.set(intensity);
	}

	@Override
	public Vector3f getAmbient(Vector3f dest) {
		return dest.set(ambient);
	}


}
