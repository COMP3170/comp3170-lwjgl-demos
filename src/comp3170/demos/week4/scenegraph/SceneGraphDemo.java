
package comp3170.demos.week4.scenegraph;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.joml.Matrix3f;
import org.joml.Vector2f;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import comp3170.GLException;
import comp3170.InputManager;
import comp3170.Shader;

public class SceneGraphDemo extends JFrame implements GLEventListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto
	
	private int width = 800;
	private int height = 800;

	private GLCanvas canvas;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/demos/week4/scenegraph"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private Animator animator;
	private long oldTime;
	private InputManager input;

	private Matrix3f modelMatrix;
	private Matrix3f viewMatrix;
	private Matrix3f projectionMatrix;
	
	private SceneObject sceneGraph;
	private Arm[] arms;
	
	public SceneGraphDemo() {
		super("Week 4 scene graph demo");

		// set up a GL canvas
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		this.canvas = new GLCanvas(capabilities);
		this.canvas.addGLEventListener(this);
		this.add(canvas);
		
		// set up the JFrame
		
		this.setSize(width,height);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		// set up Animator		
		this.animator = new Animator(canvas);
		this.animator.start();
		this.oldTime = System.currentTimeMillis();		
		
		// set up Input manager
		this.input = new InputManager(canvas);

	}

	@Override
	public void init(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// set the background colour to black
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		// Compile the shader
		try {
			File vertexShader = new File(DIRECTORY, VERTEX_SHADER);
			File fragementShader = new File(DIRECTORY, FRAGMENT_SHADER);
			this.shader = new Shader(vertexShader, fragementShader);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (GLException e) {
			e.printStackTrace();
			System.exit(1);
		}

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
		
		if (input.isKeyDown(KeyEvent.VK_LEFT)) {
			arms[0].setPosition(armPosition.x - MOVEMENT_SPEED * deltaTime, armPosition.y);
		}
		if (input.isKeyDown(KeyEvent.VK_RIGHT)) {
			arms[0].setPosition(armPosition.x + MOVEMENT_SPEED * deltaTime, armPosition.y);
		}

		float angle0 = arms[0].getAngle();
		float angle1 = arms[1].getAngle();
		float angle2 = arms[2].getAngle();
		float rot = ROTATION_SPEED * deltaTime;
		
		if (input.isKeyDown(KeyEvent.VK_NUMPAD1)) {
			arms[0].setAngle(angle0 + rot);
		}
		if (input.isKeyDown(KeyEvent.VK_NUMPAD2)) {
			arms[0].setAngle(angle0 - rot);
		}
		if (input.isKeyDown(KeyEvent.VK_NUMPAD4)) {
			arms[1].setAngle(angle1 + rot);
		}
		if (input.isKeyDown(KeyEvent.VK_NUMPAD5)) {
			arms[1].setAngle(angle1 - rot);
		}
		if (input.isKeyDown(KeyEvent.VK_NUMPAD7)) {
			arms[2].setAngle(angle2 + rot);
		}
		if (input.isKeyDown(KeyEvent.VK_NUMPAD8)) {
			arms[2].setAngle(angle2 - rot);
		}
		
		input.clear();		
	}
	
	@Override
	public void display(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// update the scene
		update();	

        // clear the colour buffer
		gl.glClear(GL_COLOR_BUFFER_BIT);		

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
	public void reshape(GLAutoDrawable d, int x, int y, int width, int height) {
		this.width = width;
		this.height = height;		
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) { 
		new SceneGraphDemo();
	}


}
