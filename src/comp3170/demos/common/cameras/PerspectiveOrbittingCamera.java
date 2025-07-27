package comp3170.demos.common.cameras;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.InputManager;
import comp3170.SceneObject;

/**
 * A perspective camera that orbits the origin.
 * 
 * I split this into separate classes to handle camera & orbiting features, 
 * but kept this class for backward compatibility with old code. 
 *
 * @author malcolmryan
 *
 */

public class PerspectiveOrbittingCamera extends SceneObject implements ICamera {

	private PerspectiveCamera camera;
	private OrbitingArmature armature;

	public PerspectiveOrbittingCamera(float distance, float fovy, float aspect, float near, float far) {
		armature = new OrbitingArmature(distance);
		armature.setParent(this);
		
		camera = new PerspectiveCamera(fovy, aspect, near, far);
		camera.setParent(armature);
	}

	@Override
	public void update(float deltaTime, InputManager input) {
		camera.update(deltaTime, input);
		armature.update(deltaTime, input);
	}

	@Override
	public Matrix4f getProjectionMatrix(Matrix4f dest) {
		return camera.getProjectionMatrix(dest);
	}

	@Override
	public Matrix4f getViewMatrix(Matrix4f dest) {
		return camera.getViewMatrix(dest);
	}

	@Override
	public Vector4f getDirection(Vector4f dest) {
		return camera.getDirection(dest);
	}


}
