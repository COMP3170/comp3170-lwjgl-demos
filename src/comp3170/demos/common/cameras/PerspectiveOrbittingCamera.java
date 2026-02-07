package comp3170.demos.common.cameras;

import org.joml.Matrix4f;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.sceneobjects.OrbitingArmature;

/**
 * This class combines the OrthographicCamera and the OrbittingArmature to 
 * implement an orthographic camera that orbits its parent in the scene graph
 * 
 * It creates a local scene graph as:
 * 
 * this -> armature -> camera
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
	public Matrix4f getModelMatrix(Matrix4f dest) {
		return camera.getModelMatrix(dest);
	}


}
