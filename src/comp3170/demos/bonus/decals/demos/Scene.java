package comp3170.demos.bonus.decals.demos;

import org.joml.Matrix4f;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.cameras.ICamera;

public class Scene extends SceneObject {

	public static Scene theScene = null;

	private ICamera camera;
	
	public Scene() {
		theScene = this;
	}

	public ICamera getCamera() {
		return camera;
	}

	public void update(float deltaTime, InputManager input) {
		camera.update(deltaTime, input);
	}

	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();

	public void draw() {
		camera.getViewMatrix(viewMatrix);
		camera.getProjectionMatrix(projectionMatrix);
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);
		
		draw(mvpMatrix);
	}

}
