package comp3170.demos.week6.camera3d;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.joml.Matrix4f;

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
import comp3170.demos.SceneObject;
import comp3170.demos.week6.camera3d.cameras.Camera;
import comp3170.demos.week6.camera3d.cameras.OrthographicCamera;
import comp3170.demos.week6.camera3d.cameras.PerspectiveCamera;
import comp3170.demos.week6.camera3d.sceneobjects.Axes;
import comp3170.demos.week6.camera3d.sceneobjects.Cube;
import comp3170.demos.week6.camera3d.sceneobjects.Grid;

public class CameraDemo extends JFrame implements GLEventListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto
	
	private int width = 800;
	private int height = 800;

	private GLCanvas canvas;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/demos/week6"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private Animator animator;
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

	public CameraDemo() {
		super("Week 6 3D camera demo");

		// set up a GL canvas
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		add(canvas);
		
		// set up Animator		

		animator = new Animator(canvas);
		animator.start();
		oldTime = System.currentTimeMillis();		

		// input
		
		input = new InputManager(canvas);
		
		// set up the JFrame
		
		setSize(width,height);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	private static final float CAMERA_DISTANCE = 2f;
	private static final float CAMERA_NEAR = 0.1f;
	private static final float CAMERA_FAR = 10f;
	private static final float CAMERA_WIDTH = 4;
	private static final float CAMERA_HEIGHT = 4;
	private static final float CAMERA_FOVY = TAU/6;
	private static final float CAMERA_ASPECT = 1;
	
	@Override
	public void init(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
				
		shader = compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		root = new SceneObject();
		
		// Set up the scene
		grid = new Grid(shader, 11);
		grid.setParent(root);
		
		axes = new Axes(shader);
		axes.setParent(root);
		
		cubes = new Cube[] {
			new Cube(shader, Color.RED),
			new Cube(shader, new Color(0.4f, 0.4f, 1.0f)),
			new Cube(shader, Color.GREEN),
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
	
	}

	private Shader compileShader(String vertex, String fragment) {
		// Compile the shader
		try {
			File vertexShader = new File(DIRECTORY, vertex);
			File fragementShader = new File(DIRECTORY, fragment);
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
		
		if (input.wasKeyPressed(KeyEvent.VK_SPACE)) {
			currentCamera = (currentCamera + 1) % cameras.length; 
		}

		for (int i = 0; i < cameras.length; i++) {
			cameras[i].update(input, deltaTime);
		}
		
		input.clear();
	}
	
	@Override
	public void display(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		update();
		
        // clear the colour buffer
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClear(GL_COLOR_BUFFER_BIT);		
		
		cameras[currentCamera].getViewMatrix(viewMatrix);
		cameras[currentCamera].getProjectionMatrix(projectionMatrix);
		
		// pre-multiply projetion and view matrices
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);
		
		root.draw(mvpMatrix);		
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
		new CameraDemo();
	}


}
