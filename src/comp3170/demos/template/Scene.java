package comp3170.demos.template;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.cameras.Camera;
import comp3170.demos.common.sceneobjects.Axes3D;
import comp3170.demos.common.sceneobjects.Grid;
import comp3170.demos.template.sceneobjects.Cylinder;

/**
 * The Scene class is the root of the scene graph, and handles the construction of objects in the scene.
 * 
 * This extended the SceneObject parent class, which implements the scene graph tree.
 */

public class Scene extends SceneObject {

	// I typically use a really simple singleton pattern to provide access to the Scene
	// e.g. to access the camera of lights
	
	public static Scene theScene = null;
	private DemoCamera camera; // the 'common' package contains code for some standard cameras
	private Cylinder cylinder;

	public Scene() {
		theScene = this;

		// Create objects in the scene and attach them to the scene graph (this)
		// The SceneObject class has a getMatrix() method which gives you direct access 
		// to the local model matrix for the object. We use JOML methods to change this
		// matrix in-place, in order to transform the object
		
		// There are some standard objects in the 'common.sceneobjects' package
		Grid grid = new Grid(20);
		grid.setParent(this);
		grid.getMatrix().scale(5);

		cylinder = new Cylinder();
		cylinder.setParent(this);
		
		// we can also attach objects to each other using setParent
		Axes3D axes = new Axes3D();
		axes.setParent(cylinder);
		axes.getMatrix().translate(0, 1, 0);

		camera = new DemoCamera();
	}

	public void update(float deltaTime, InputManager input) {
		// update objects in the scene
		camera.update(deltaTime, input);
		cylinder.update(deltaTime, input);
	}

	public Camera getCamera() {
		return camera;
	}


}
