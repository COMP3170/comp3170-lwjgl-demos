package comp3170.demos.week6.backface;

import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glCullFace;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;

import static org.lwjgl.opengl.GL20.GL_FLOAT_VEC2;
import static org.lwjgl.opengl.GL20.GL_FLOAT_VEC3;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_O;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_L;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.joml.Matrix4f;



import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector3f;

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

public class BackfaceCullingDemo implements IWindowListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto
	
	private int width = 800;
	private int height = 800;

	private Window window;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/demos/week6"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

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
		Window window = new Window("Week 6 Backface Culling Demo", width, height, this);
		window.setResizable(true);
		window.run();
		
		
		
		oldTime = System.currentTimeMillis();		
	}

	@Override
	public void init() {
		input = new InputManager(window);
		
		if (isCulling) {
			glEnable(GL_CULL_FACE);
		}
		else {
			glDisable(GL_CULL_FACE);			
		}
		
		glCullFace(cullFace);
		
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);	
		

		shader = compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// Set up the scene
		root = new SceneObject();
		grid = new Grid(shader, 11);
		grid.setParent(root);
		
		axes = new Axes(shader);
		axes.setParent(root);
		
		triangle = new Triangle(shader, Color.YELLOW);
		triangle.setParent(root);
		
		camera = new PerspectiveCamera(2, TAU/6, 1, 0.1f, 10f);		
	}

	private Shader compileShader(String vertex, String fragement) {
		// Compile the shader
		try {
			File vertexShader = new File(DIRECTORY, vertex);
			File fragementShader = new File(DIRECTORY, fragement);
			return new Shader(vertexShader, fragementShader);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (GLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;

	}

	
	private void update() {

		long time = System.currentTimeMillis();
		float deltaTime = (time-oldTime) / 1000f;
		oldTime = time;
		
		// enable/disable face culling
		if (input.wasKeyPressed(KeyEvent.VK_SPACE)) {
			isCulling = !isCulling;
			
			if (isCulling) {
				glEnable(GL_CULL_FACE);
			}
			else {
				glDisable(GL_CULL_FACE);
			}
		}

		// set which face to cull
		if (input.wasKeyPressed(KeyEvent.VK_F)) {
			glCullFace(GL_FRONT);
		}

		if (input.wasKeyPressed(KeyEvent.VK_B)) {
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

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}





}
