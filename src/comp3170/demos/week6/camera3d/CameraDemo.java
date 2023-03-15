package comp3170.demos.week6.camera3d;

import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import static org.lwjgl.opengl.GL14.GL_COLOR_BUFFER_BIT;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.Shader;
import comp3170.Window;
import comp3170.SceneObject;
import comp3170.demos.week6.camera3d.cameras.Camera;
import comp3170.demos.week6.camera3d.cameras.OrthographicCamera;
import comp3170.demos.week6.camera3d.cameras.PerspectiveCamera;
import comp3170.demos.week6.camera3d.sceneobjects.Axes;
import comp3170.demos.week6.camera3d.sceneobjects.Cube;
import comp3170.demos.week6.camera3d.sceneobjects.Grid;
import comp3170.demos.week6.shaders.ShaderLibrary;

public class CameraDemo implements IWindowListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto
	
	private int width = 800;
	private int height = 800;

	private Window window;
	private Shader shader;

	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private long oldTime;
	private InputManager input;

	private Grid grid;

	private Camera[] cameras;
	private int currentCamera;
	private Matrix4f viewMatrix  = new Matrix4f();
	private Matrix4f projectionMatrix  = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();

	private Cube[] cubes;

	private Axes axes;

	private SceneObject root;

	public CameraDemo() throws OpenGLException {
		window = new Window("Week 6 Backface Culling Demo", width, height, this);
		window.setResizable(true);
		window.run();
		
		oldTime = System.currentTimeMillis();		


	}

	private static final float CAMERA_DISTANCE = 2f;
	private static final float CAMERA_NEAR = 0.1f;
	private static final float CAMERA_FAR = 10f;
	private static final float CAMERA_WIDTH = 4;
	private static final float CAMERA_HEIGHT = 4;
	private static final float CAMERA_FOVY = TAU/6;
	private static final float CAMERA_ASPECT = 1;
	
	@Override
	public void init() {
		
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
				
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		root = new SceneObject();
		
		// Set up the scene
		grid = new Grid(shader, 11);
		grid.setParent(root);
		
		axes = new Axes();
		axes.setParent(root);
		
		cubes = new Cube[] {
			new Cube(shader, new Vector4f(0.9f,0.9f,0.5f, 1.0f)),   // YELLOW
			new Cube(shader, new Vector4f(0.5f, 0.7f, 0.4f, 1.0f)), // GREEN
			new Cube(shader, new Vector4f(0.8f, 0.4f, 0.7f, 1.0f)), // PINK
		};
		
		for (int i = 0; i < cubes.length; i++) {
			cubes[i].setParent(grid);
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
		
		input = new InputManager(window);
	
	}
	
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time-oldTime) / 1000f;
		oldTime = time;
		
		if (input.wasKeyPressed(GLFW_KEY_SPACE)) {
			currentCamera = (currentCamera + 1) % cameras.length; 
		}

		for (int i = 0; i < cameras.length; i++) {
			cameras[i].update(input, deltaTime);
		}
		
		input.clear();
	}
	
	@Override
	public void draw() {
		
		update();
		
        // clear the colour buffer
		
		glClear(GL_COLOR_BUFFER_BIT);		
		
		cameras[currentCamera].getViewMatrix(viewMatrix);
		cameras[currentCamera].getProjectionMatrix(projectionMatrix);
		
		// pre-multiply projetion and view matrices
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);
		
		root.draw(mvpMatrix);		
	}

	
	public static void main(String[] args) throws OpenGLException { 
		new CameraDemo();
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		glViewport(0,0,width,height);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}


}
