
package comp3170.demos.week4.camera;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_DOWN;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glScissor;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.Shader;
import comp3170.Window;
import comp3170.demos.week3.instancing.Squares;

public class CameraDemo implements IWindowListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto
	
	private int width = 800;
	private int height = 800;

	private Window window;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/demos/week4/camera"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private Squares squares;

	private long oldTime;
	private InputManager input;

	private Camera camera;
	private boolean showCamera = true;
	private Matrix3f viewMatrix;
	private Matrix3f projectionMatrix;

	public CameraDemo() throws OpenGLException {
		window = new Window("Week 4 Camera Demo", width, height, this);
		window.run();
	}

	private static final int NSQUARES = 100;
	
	@Override
	public void init() {
		glEnable(GL_SCISSOR_TEST);
		
		// set the background colour to black
		glClearColor(0.1f, 0.0f, 0.0f, 1.0f);
		
		// Compile the shader
		try {
			File vertexShader = new File(DIRECTORY, VERTEX_SHADER);
			File fragementShader = new File(DIRECTORY, FRAGMENT_SHADER);
			shader = new Shader(vertexShader, fragementShader);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (OpenGLException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Set up the scene
		
	    squares = new Squares(NSQUARES);
	    
	    // Set up the camera

	    camera = new Camera(shader);
	    camera.setPosition(0,0);
	    camera.setAngle(0);
	    camera.setZoom(width * 2f);	// pixels per world unit
	    camera.setSize(width, height);
	    
	    // allocation view and projection matrices
	    viewMatrix = new Matrix3f();
	    projectionMatrix = new Matrix3f();
	    
	    // add an input manager
		input = new InputManager(window);
	}

	private static final float ROTATION_SPEED = TAU / 6;
	private static final float CAMERA_ROTATION_SPEED = TAU / 6;
	private static final float CAMERA_MOVEMENT_SPEED = 1f;
	private static final float CAMERA_ZOOM_SPEED = 1.5f;
	
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;
		
		
		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			camera.rotate(-CAMERA_ROTATION_SPEED * deltaTime);
		}

		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			camera.rotate(CAMERA_ROTATION_SPEED * deltaTime);
		}

		if (input.isKeyDown(GLFW_KEY_W)) {
			camera.translate(0, CAMERA_MOVEMENT_SPEED * deltaTime);
			
		}
		if (input.isKeyDown(GLFW_KEY_S)) {
			camera.translate(0, -CAMERA_MOVEMENT_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_A)) {
			camera.translate(-CAMERA_MOVEMENT_SPEED * deltaTime, 0);
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			camera.translate(CAMERA_MOVEMENT_SPEED * deltaTime, 0);
		}
		if (input.isKeyDown(GLFW_KEY_PAGE_UP)) {
			camera.zoom((float) Math.pow(1.0f / CAMERA_ZOOM_SPEED, deltaTime));
		}
		if (input.isKeyDown(GLFW_KEY_PAGE_DOWN)) {
			camera.zoom((float) Math.pow(CAMERA_ZOOM_SPEED, deltaTime));
		}

		if (input.wasKeyPressed(GLFW_KEY_SPACE)) {
			showCamera = !showCamera;
		}
		input.clear();		
		
		squares.update(deltaTime);
	}
	
	
	public static void main(String[] args) throws OpenGLException { 
		new CameraDemo();
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
		// update the scene
		update();	
		
		glViewport(0, 0, width, height);
		
		glClear(GL_COLOR_BUFFER_BIT);		
		
		// activate the shader
		this.shader.enable();		
		
		
		if (showCamera) {
			viewMatrix.identity();
			projectionMatrix.identity();
		}
		else {
			camera.getViewMatrix(viewMatrix);
			camera.getProjectionMatrix(projectionMatrix);
		}
		
		shader.setUniform("u_viewMatrix", viewMatrix);
		shader.setUniform("u_projectionMatrix", projectionMatrix);
		
		// draw the squares
		squares.draw();

		
		if (showCamera) {
			// draw the camera rectangle
			camera.draw(shader);			
		}
		
	}

	@Override
	public void resize(int width, int height) {
		
		this.width = width;
		this.height = height;
			
		camera.setSize(width, height);
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}


}
