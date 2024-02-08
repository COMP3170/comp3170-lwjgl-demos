package comp3170.demos.common.lights;

import org.joml.Vector3f;
import org.joml.Vector4f;

public interface Light {
	Vector4f getSourceVector(Vector4f dest);
	Vector3f getIntensity(Vector3f dest);
	Vector3f getAmbient(Vector3f dest);
}
