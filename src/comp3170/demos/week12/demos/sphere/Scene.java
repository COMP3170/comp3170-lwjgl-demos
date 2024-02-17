package comp3170.demos.week12.demos.sphere;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.sceneobjects.Axes3D;
import comp3170.demos.common.sceneobjects.Grid;
import comp3170.demos.week12.cameras.Camera;
import comp3170.demos.week12.cameras.OrbitingCamera;
import comp3170.demos.week12.sceneobjects.Skybox;
import comp3170.demos.week12.sceneobjects.Sphere;

public class Scene extends SceneObject {

	public static Scene theScene = null;
	private Skybox skybox;
	private OrbitingCamera camera;

	public Scene() {
		theScene = this;
		skybox = new Skybox();
		
		Axes3D axes = new Axes3D();
		axes.setParent(this);
		axes.getMatrix().scale(2);
		
		Sphere sphere = new Sphere();
		sphere.setParent(this);
		
		Grid grid = new Grid(20);
		grid.setParent(this);
		grid.getMatrix().translate(-0.5f, -1, 0.5f).scale(5);
		
		camera = new OrbitingCamera();
	}

	public void update(float deltaTime, InputManager input) {
		camera.update(deltaTime, input);	
		skybox.update(input, deltaTime);
	}

	public Camera getCamera() {
		return camera;
	}

	public Skybox getSkybox() {
		return skybox;
	}

	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();
	private Vector4f origin = new Vector4f(0,0,0,1);

	@Override
	public void draw(Matrix4f parentMatrix) {
		// draw the rest of the scene
		super.draw(parentMatrix);
		
		// draw the cubemap without view translation, 
		// so it is always centred on the camera
		camera.getViewMatrix(viewMatrix);
		viewMatrix.setColumn(3, origin);
		camera.getProjectionMatrix(projectionMatrix);
		projectionMatrix.mul(viewMatrix, mvpMatrix);
		skybox.draw(mvpMatrix);		
	}
	
}
