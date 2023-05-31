package comp3170.demos.week12.livedemo.pixelart;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.sceneobjects.Axes3D;
import comp3170.demos.week12.cameras.Camera;
import comp3170.demos.week12.cameras.OrbitingCamera;
import comp3170.demos.week12.lights.DirectionalLight;
import comp3170.demos.week12.lights.Light;
import comp3170.demos.week12.sceneobjects.Cylinder;
import comp3170.demos.week12.sceneobjects.Plane;

public class Scene extends SceneObject {

	public static Scene theScene;
	private OrbitingCamera camera;
	private DirectionalLight light;
	
	public Scene() {
		theScene = this;
		camera = new OrbitingCamera();
		light = new DirectionalLight();

		Axes3D axes = new Axes3D();
		axes.setParent(this);
		
		Cylinder cylinder = new Cylinder();
		cylinder.setParent(this);
		cylinder.getMatrix().translate(0,-1,0).scale(2,2,2);
		
		Plane plane = new Plane();
		plane.setParent(this);
		plane.getMatrix().translate(0,-1,0).scale(4,4,4);
	}
	
	public Camera getCamera() {
		return camera;
	}

	public Light getLight() {
		return light;
	}

	public void update(InputManager input, float deltaTime) {
		camera.update(input, deltaTime);
		light.update(input, deltaTime);
	}



}
