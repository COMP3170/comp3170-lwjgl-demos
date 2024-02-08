package comp3170.demos.week10.sceneobjects;

import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.SceneObject;
import comp3170.demos.week10.cameras.PerspectiveCamera;
import comp3170.demos.week10.cameras.Camera;
import comp3170.demos.week10.sceneobjects.Quad;
import comp3170.demos.week7.sceneobjects.Grid;

public class Scene extends SceneObject {

	public static Scene theScene = null;
	private PerspectiveCamera camera;
	private Quad quad;
	
	public Scene() {

		theScene = this;
		
		quad = new Quad();
		quad.setParent(theScene);
		
		camera = new PerspectiveCamera();

	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public void update(InputManager input, float deltaTime) {
		camera.update(input, deltaTime);
		//quad.update(input, deltaTime);
	}
	
}
