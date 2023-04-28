package comp3170.demos.week8.demos;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_GEQUAL;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.Color;

import org.joml.Matrix4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.SceneObject;
import comp3170.Window;
import comp3170.demos.week8.sceneobjects.Axes3D;
import comp3170.demos.week8.sceneobjects.Grid;
import comp3170.demos.week8.sceneobjects.Triangle;
import comp3170.demos.week8.cameras.PerspectiveCamera;

import static comp3170.Math.TAU;

public class DepthDemo implements IWindowListener {

	private Window window;
	private int screenWidth = 800;
	private int screenHeight = 800;
	private InputManager input;
	private long oldTime;

	private PerspectiveCamera camera;
	private SceneObject scene;

	public DepthDemo() throws OpenGLException {
		window = new Window("Depth Demo", screenWidth, screenHeight, this);
		window.run();
	}

	@Override
	public void init() {
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);

		glDepthFunc(GL_LEQUAL);

		// set up scene
		scene = new SceneObject();
		Axes3D axes = new Axes3D();
		axes.setParent(scene);
		Grid grid = new Grid(10);
		grid.setParent(scene);

		Triangle redTriangle = new Triangle(Color.red);
		redTriangle.setParent(scene);
		Triangle blueTriangle = new Triangle(Color.blue);
		blueTriangle.setParent(scene); blueTriangle.getMatrix().rotateY(TAU/12);

		camera = new PerspectiveCamera();

		// initialise oldTime
		input = new InputManager(window);
		oldTime = System.currentTimeMillis();
	}

	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;

		camera.update(input, deltaTime);
	}

	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();

	@Override
	public void draw() {
		update();

		glClear(GL_COLOR_BUFFER_BIT);
		glViewport(0, 0, screenWidth, screenHeight);

		glClearDepth(1);
		glClear(GL_DEPTH_BUFFER_BIT);

		camera.getViewMatrix(viewMatrix);
		camera.getProjectionMatrix(projectionMatrix);
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);

		scene.draw(mvpMatrix);

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws OpenGLException {
		new DepthDemo();
	}

}
