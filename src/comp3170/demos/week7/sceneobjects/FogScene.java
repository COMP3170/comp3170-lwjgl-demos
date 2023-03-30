package comp3170.demos.week7.sceneobjects;

import java.awt.Color;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.week7.cameras.OrthographicCamera;

public class FogScene extends SceneObject {

	private OrthographicCamera camera;

	public FogScene() {
		Grid grid = new Grid(10);
		grid.setParent(this);
		grid.getMatrix().scale(10);
		
		Axes3D axes = new Axes3D();
		axes.setParent(this);
		
		Cube redCube = new Cube(Color.red);
		redCube.setParent(this);
		redCube.getMatrix().translate(2,0.1f,1).scale(0.1f);
		
		camera = new OrthographicCamera();
	}

	public OrthographicCamera getCamera() {
		return camera;
	}
	
	public void update(InputManager input, float deltaTime) {
		camera.update(input, deltaTime);		
	}
		
}
