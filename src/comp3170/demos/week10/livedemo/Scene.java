package comp3170.demos.week10.livedemo;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.sceneobjects.Axes3D;
import comp3170.demos.week10.cameras.Camera;
import comp3170.demos.week10.cameras.PerspectiveCamera;

public class Scene extends SceneObject {

	public static Scene theScene = null;
	private PerspectiveCamera camera;
	private AnimatedQuad quad;
	
	public Scene() {
		theScene = this;

		Axes3D axes = new Axes3D();
		axes.setParent(this);
		
		quad = new AnimatedQuad();
		quad.setParent(this);		
		
		camera = new PerspectiveCamera();
	}
	
	public Camera getCamera() {
		return camera;
	}

	public void update(InputManager input, float deltaTime) {
		camera.update(input, deltaTime);	
		quad.update(input, deltaTime);
	}


}
