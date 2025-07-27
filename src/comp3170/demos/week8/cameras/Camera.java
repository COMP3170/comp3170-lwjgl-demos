package comp3170.demos.week8.cameras;

import static comp3170.Math.TAU;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.cameras.ICamera;
import comp3170.demos.common.cameras.OrbitingArmature;
import comp3170.demos.common.cameras.PerspectiveCamera;

public class Camera extends SceneObject implements ICamera {

	private float fovy = TAU / 10;
	private float aspect = 1;
	private float near = 0.1f;
	private float far = 100f;
	private float distance = 4f;

	private PerspectiveCamera camera;
	private OrbitingArmature armature;

	public Camera() {
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
