package comp3170.demos.week12.demos.skybox;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.sceneobjects.Grid;
import comp3170.demos.week12.cameras.Camera;
import comp3170.demos.week12.cameras.PerspectiveCamera;
import comp3170.demos.week12.sceneobjects.Cube;
import comp3170.demos.week12.sceneobjects.Skybox;

public class Scene extends SceneObject {

	public static Scene theScene = null;
	private Skybox cubemap;
	private PerspectiveCamera camera;

	public Scene() {
		theScene = this;
		cubemap = new Skybox();
		
		Grid grid = new Grid(11);
		grid.setParent(this);
		grid.getMatrix().translate(-0.5f, 0, 0.5f).scale(10);
		Cube cube = new Cube(Color.gray);
		cube.setParent(this);
		cube.getMatrix().translate(0,1,0);
		
		camera = new PerspectiveCamera();
	}

	public void update(float deltaTime, InputManager input) {
		camera.update(input, deltaTime);	
		cubemap.update(input, deltaTime);
	}

	public Camera getCamera() {
		return camera;
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
		cubemap.draw(mvpMatrix);		
	}
	
}
