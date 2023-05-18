package comp3170.demos.week11.livedemo;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.sceneobjects.Axes3D;
import comp3170.demos.week11.cameras.Camera;

public class Scene extends SceneObject {

	public static Scene theScene = null;
	
	private OrthographicCamera camera;
	private TexturedCube cube;

	private DirectionalLight light;

	public Scene() {
		if (theScene != null) {
			throw new IllegalStateException("theScene is not null");
		}
		
		theScene = this;
		
		Axes3D axes = new Axes3D();
		axes.setParent(this);
		axes.getMatrix().scale(2,2,2);
		
		cube = new TexturedCube();
		cube.setParent(this);
		
		camera = new OrthographicCamera();		
		light = new DirectionalLight();
	}
	
	public Camera getCamera() {
		return camera;
	}

	public DirectionalLight getLight() {
		return light;
	}
	
	public void update(InputManager input, float deltaTime) {
		cube.update(input, deltaTime);		
		camera.update(input, deltaTime);
		light.update(input, deltaTime);
	}

	
}
