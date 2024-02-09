package comp3170.demos.week6.camera3d;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.File;

import org.joml.Matrix4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.ShaderLibrary;
import comp3170.Window;
import comp3170.demos.week6.camera3d.cameras.Camera;
import comp3170.demos.week6.camera3d.sceneobjects.Scene;

public class CameraDemo implements IWindowListener {

	private static final File COMMON_DIR = new File("src/comp3170/demos/common/shaders"); 

	private int screenWidth = 800;
	private int screenHeight = 800;

	private Window window;
	private long oldTime;
	private InputManager input;

	private Scene scene;


	public CameraDemo() throws OpenGLException {
		window = new Window("Week 6 Camera Demo", screenWidth, screenHeight, this);
		window.setResizable(true);
		window.run();
	}

	
	@Override
	public void init() {
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		new ShaderLibrary(COMMON_DIR);
		scene = new Scene();
		
		input = new InputManager(window);
		oldTime = System.currentTimeMillis();		
	}
	
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time-oldTime) / 1000f;
		oldTime = time;
		
		scene.update(deltaTime, input);		
		input.clear();
	}
	
	private Matrix4f viewMatrix  = new Matrix4f();
	private Matrix4f projectionMatrix  = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();

	@Override
	public void draw() {		
		update();
		
        // clear the colour buffer
		
		glClear(GL_COLOR_BUFFER_BIT);		
		
		// pre-multiply projection and view matrices

		Camera camera = scene.getCamera();
		camera.getViewMatrix(viewMatrix);
		camera.getProjectionMatrix(projectionMatrix);		
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);

		// draw the scene with the given MVP matrix
		
		scene.draw(mvpMatrix);		
	}

	@Override
	public void resize(int width, int height) {
		screenWidth = width;
		screenHeight = height;
		glViewport(0,0,screenWidth,screenHeight);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws OpenGLException { 
		new CameraDemo();
	}

}
