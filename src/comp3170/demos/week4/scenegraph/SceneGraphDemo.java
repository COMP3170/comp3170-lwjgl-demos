
package comp3170.demos.week4.scenegraph;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.Window;

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

	private Matrix4f modelMatrix;
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;
	
	private SceneObject root;
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
		
		root = new SceneObject();
		
		arms = new Arm[3];
		
	    arms[0] = new Arm(shader, 0.2f, 1);
	    arms[0].setParent(root);
	    arms[0].getMatrix().translate(0f, -1.0f, 0f);
//	    arms[0].getMatrix().scale(2,1,1);		// this causes shear in child objects
	    arms[0].setColour(Color.RED);
	    
	    arms[1] = new Arm(shader, 0.15f, 0.5f);
	    arms[1].setParent(arms[0]);
	    arms[1].getMatrix().translate(0f, 0.9f, 0f);
	    arms[1].setColour(Color.GREEN);

	    arms[2] = new Arm(shader, 0.1f, 0.25f);
	    arms[2].setParent(arms[1]);
	    arms[2].getMatrix().translate(0f, 0.4f, 0f);
	    arms[2].setColour(Color.BLUE);
		
		
	    // allocation view and projection matrices
	    modelMatrix = new Matrix4f();
	    viewMatrix = new Matrix4f();
	    projectionMatrix = new Matrix4f();
	}

	private final float ROTATION_SPEED = TAU / 8;
	private final float MOVEMENT_SPEED = 0.1f;
	private Vector3f armPosition = new Vector3f();
	
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;
		
		if (input.isKeyDown(GLFW_KEY_A)) {
			arms[0].getMatrix().translate(armPosition.x - MOVEMENT_SPEED * deltaTime, armPosition.y, 0f);
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			arms[0].getMatrix().translate(armPosition.x + MOVEMENT_SPEED * deltaTime, armPosition.y, 0f);
		}
		float rot = ROTATION_SPEED * deltaTime;
		
		if (input.isKeyDown(GLFW_KEY_W)) {
			arms[0].getMatrix().rotateZ(rot);
		}
		if (input.isKeyDown(GLFW_KEY_S)) {
			arms[0].getMatrix().rotateZ(-rot);
		}
		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			arms[1].getMatrix().rotateZ(rot);
		}
		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			arms[1].getMatrix().rotateZ(-rot);
		}
		if (input.isKeyDown(GLFW_KEY_UP)) {
			arms[2].getMatrix().rotateZ(rot);
		}
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			arms[2].getMatrix().rotateZ(-rot);
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
		root.draw();
		
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
