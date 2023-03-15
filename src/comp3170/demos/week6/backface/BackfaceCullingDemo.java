package comp3170.demos.week6.backface;

import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_FRONT;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import org.joml.Matrix4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.Window;
import comp3170.demos.week6.camera3d.cameras.Camera;
import comp3170.demos.week6.camera3d.cameras.PerspectiveCamera;
import comp3170.demos.week6.camera3d.sceneobjects.Axes;
import comp3170.demos.week6.camera3d.sceneobjects.Grid;
import comp3170.demos.week6.shaders.ShaderLibrary;

public class BackfaceCullingDemo implements IWindowListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto
	
	private int width = 800;
	private int height = 800;

	private Window window;
	private Shader shader;
	 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";
	
	private int frameRate = 100;
	
	private long oldTime;
	private InputManager input;

	private Grid grid;
	private Triangle triangle;
	private Axes axes;

	private Camera camera;
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();

	private boolean isCulling = false;
	private int cullFace = GL_BACK;

	private SceneObject root;


	public BackfaceCullingDemo() throws OpenGLException {
		window = new Window("Week 6 Backface Culling Demo", width, height, this);
		window.setResizable(true);
		window.run();
		
		oldTime = System.currentTimeMillis();		
	}

	@Override
	public void init() {	
		
		if (isCulling) {
			glEnable(GL_CULL_FACE);
		}
		else {
			glDisable(GL_CULL_FACE);			
		}
		
		glCullFace(cullFace);
		
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);	
		

		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// Set up the scene
		root = new SceneObject();
		grid = new Grid(shader, 11);
		grid.setParent(root);
		
		axes = new Axes();
		axes.setParent(root);
		
		triangle = new Triangle(shader, Color.YELLOW);
		triangle.setParent(root);
		
		camera = new PerspectiveCamera(2, TAU/6, 1, 0.1f, 10f);		
		
		input = new InputManager(window);
	}
	
	private void update() {

		long time = System.currentTimeMillis();
		float deltaTime = (time-oldTime) / 1000f;
		oldTime = time;
				
		// enable/disable face culling
		if (input.wasKeyPressed(GLFW_KEY_SPACE)) {
			isCulling = !isCulling;
			
			if (isCulling) {
				glEnable(GL_CULL_FACE);
			}
			else {
				glDisable(GL_CULL_FACE);
			}
		}

		// set which face to cull
		if (input.wasKeyPressed(GLFW_KEY_F)) {
			glCullFace(GL_FRONT);
		}

		if (input.wasKeyPressed(GLFW_KEY_B)) {
			glCullFace(GL_BACK);
		}

		camera.update(input, deltaTime);
		
		input.clear();
	}
	
	@Override
	public void draw() {
		
		update();
		
        // clear the colour buffer	
		glClear(GL_COLOR_BUFFER_BIT);		
		
		// pre-multiply projetion and view matrices
		camera.getViewMatrix(viewMatrix);
		camera.getProjectionMatrix(projectionMatrix);		
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);
		
		// draw the scene
		root.draw(mvpMatrix);
		
		// restrict the framerate by sleeping between frames
		try {
			TimeUnit.MILLISECONDS.sleep(1000 / frameRate);
		} catch (InterruptedException e) {
		}
		
	}

	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		glViewport(0, 0,width, height);
		
	}

	@Override
	public void close() {

		
	}
	public static void main(String[] args) throws OpenGLException { 
		new BackfaceCullingDemo();
	}





}
