package comp3170.demos.week7.demos;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import org.joml.Matrix4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.Window;
import comp3170.demos.week7.cameras.OrthographicCamera;
import comp3170.demos.week7.sceneobjects.FogScene;

public class FogDemo implements IWindowListener {

	private Window window;
	private int screenWidth = 800;
	private int screenHeight = 800;
	private InputManager input;
	private long oldTime;
	private FogScene scene;

	public FogDemo() throws OpenGLException {
		window = new Window("Fog demo", screenWidth, screenHeight, this);
		window.run();		
	}
	
	@Override
	public void init() {
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);

	    // initialise oldTime
		input = new InputManager(window);
	    oldTime = System.currentTimeMillis();
	    
	    scene = new FogScene();
	}

	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;

		scene.update(input, deltaTime);	
		input.clear();
	}

	
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();

	@Override
	public void draw() {
		update();
		
		glClear(GL_COLOR_BUFFER_BIT);		
		glViewport(0, 0, screenWidth, screenHeight);

		glClearDepth(1f);
		glClear(GL_DEPTH_BUFFER_BIT);		
		
		OrthographicCamera camera = scene.getCamera(); 
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
		new FogDemo();
	}
}
