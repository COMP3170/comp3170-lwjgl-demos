
package comp3170.demos.week4.scenegraph;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;

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
import org.joml.Vector2f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.Shader;
import comp3170.Window;
import comp3170.demos.week4.camera.Square;

public class SceneGraphDemo implements IWindowListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto
	
	private int width = 800;
	private int height = 800;

	private Window window;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/demos/week4/scenegraph"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private long oldTime;
	private InputManager input;

	private Matrix3f modelMatrix;
	private Matrix3f viewMatrix;
	private Matrix3f projectionMatrix;
	
	private SceneObject sceneGraph;
	private Arm[] arms;
	
	public SceneGraphDemo() throws OpenGLException {
		window = new Window("Week 4 Camera Demo", width, height, this);
		window.setResizable(true);
		window.run();
	}


	@Override
	public void init() {
		
		// set the background colour to black
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		// Compile the shader
		try {
			File vertexShader = new File(DIRECTORY, VERTEX_SHADER);
			File fragementShader = new File(DIRECTORY, FRAGMENT_SHADER);
			this.shader = new Shader(vertexShader, fragementShader);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (OpenGLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		input = new InputManager(window);

		// Set up the scene
		//
		// sceneGraph -> arm0 -> arm1 -> arm2
		
		sceneGraph = new SceneObject();
		
		arms = new Arm[3];
		
	    arms[0] = new Arm(shader, 0.2f, 1);
	    arms[0].setParent(sceneGraph);
	    arms[0].setPosition(0, -1.0f);
//	    arms[0].setScale(2, 1);		// this causes shear in child objects
	    arms[0].setColour(Color.RED);
	    
	    arms[1] = new Arm(shader, 0.15f, 0.5f);
	    arms[1].setParent(arms[0]);
	    arms[1].setPosition(0, 0.9f);
	    arms[1].setColour(Color.GREEN);

	    arms[2] = new Arm(shader, 0.1f, 0.25f);
	    arms[2].setParent(arms[1]);
	    arms[2].setPosition(0, 0.4f);
	    arms[2].setColour(Color.BLUE);
		
		
	    // allocation view and projection matrices
	    modelMatrix = new Matrix3f();
	    viewMatrix = new Matrix3f();
	    projectionMatrix = new Matrix3f();
	}

	private final float ROTATION_SPEED = TAU / 8;
	private final float MOVEMENT_SPEED = 0.1f;
	private Vector2f armPosition = new Vector2f();
	
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;

		arms[0].getPosition(armPosition);
		
		if (input.isKeyDown(GLFW_KEY_A)) {
			arms[0].setPosition(armPosition.x - MOVEMENT_SPEED * deltaTime, armPosition.y);
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			arms[0].setPosition(armPosition.x + MOVEMENT_SPEED * deltaTime, armPosition.y);
		}

		float angle0 = arms[0].getAngle();
		float angle1 = arms[1].getAngle();
		float angle2 = arms[2].getAngle();
		float rot = ROTATION_SPEED * deltaTime;
		
		if (input.isKeyDown(GLFW_KEY_W)) {
			arms[0].setAngle(angle0 + rot);
		}
		if (input.isKeyDown(GLFW_KEY_S)) {
			arms[0].setAngle(angle0 - rot);
		}
		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			arms[1].setAngle(angle1 + rot);
		}
		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			arms[1].setAngle(angle1 - rot);
		}
		if (input.isKeyDown(GLFW_KEY_UP)) {
			arms[2].setAngle(angle2 + rot);
		}
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			arms[2].setAngle(angle2 - rot);
		}
		
		input.clear();		
	}
	
	@Override
	public void draw() {
		
		// update the scene
		update();	

        // clear the colour buffer
		glClear(GL_COLOR_BUFFER_BIT);		

		// activate the shader
		this.shader.enable();		
				
		viewMatrix.identity();
		projectionMatrix.identity();
		
		shader.setUniform("u_viewMatrix", viewMatrix);
		shader.setUniform("u_projectionMatrix", projectionMatrix);
		
		modelMatrix.identity();
		sceneGraph.draw(modelMatrix);
		
	}


	@Override
	public void resize(int width, int height) {
		
		this.width = width;
		this.height = height;
		glViewport(0, 0, width, height);
		
		

		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws OpenGLException { 
		new SceneGraphDemo();
	}


}
