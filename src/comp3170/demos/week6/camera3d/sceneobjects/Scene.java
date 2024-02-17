package comp3170.demos.week6.camera3d.sceneobjects;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import org.joml.Vector4f;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.sceneobjects.Axes3D;
import comp3170.demos.common.sceneobjects.Grid;
import comp3170.demos.week6.camera3d.cameras.Camera;
import comp3170.demos.week6.camera3d.cameras.OrthographicCamera;
import comp3170.demos.week6.camera3d.cameras.PerspectiveCamera;

public class Scene extends SceneObject {
	private Grid grid;
	private Cube[] cubes;
	private Axes3D axes;
	private Camera[] cameras;
	private int currentCamera;

	private static final float CAMERA_DISTANCE = 2f;
	private static final float CAMERA_NEAR = 0.1f;
	private static final float CAMERA_FAR = 10f;
	private static final float CAMERA_WIDTH = 4;
	private static final float CAMERA_HEIGHT = 4;
	private static final float CAMERA_FOVY = TAU/6;
	private static final float CAMERA_ASPECT = 1;

	public Scene() {
		// Set up the scene
		grid = new Grid(11);
		grid.setParent(this);

		axes = new Axes3D();
		axes.setParent(this);

		// @formatter:off
		cubes = new Cube[] {
			new Cube(new Vector4f(0.9f,0.9f,0.5f, 1.0f)),   // YELLOW
			new Cube(new Vector4f(0.5f, 0.7f, 0.4f, 1.0f)), // GREEN
			new Cube(new Vector4f(0.8f, 0.4f, 0.7f, 1.0f)), // PINK
		};
		// @formatter:on

		for (Cube cube : cubes) {
			cube.setParent(grid);
		}

		cubes[0].getMatrix().translate(0.7f, 0.05f, -0.3f).scale(0.05f);
		cubes[1].getMatrix().translate(-0.5f, 0.05f, 0.3f).scale(0.05f);
		cubes[2].getMatrix().translate(0.1f, 0.05f, 0.1f).scale(0.05f);

		OrthographicCamera orthoCamera = new OrthographicCamera(CAMERA_DISTANCE, CAMERA_WIDTH, CAMERA_HEIGHT, CAMERA_NEAR, CAMERA_FAR);
		PerspectiveCamera perspectiveCamera = new PerspectiveCamera(CAMERA_DISTANCE, CAMERA_FOVY, CAMERA_ASPECT, CAMERA_NEAR, CAMERA_FAR);

		cameras = new Camera[] {
			orthoCamera,
			perspectiveCamera,
		};
		currentCamera = 0;

	}

	public Camera getCamera() {
		return cameras[currentCamera];
	}


	public void update(float deltaTime, InputManager input) {
		if (input.wasKeyPressed(GLFW_KEY_SPACE)) {
			currentCamera = (currentCamera + 1) % cameras.length;
		}

		for (Camera element : cameras) {
			element.update(deltaTime, input);
		}
	}


}
