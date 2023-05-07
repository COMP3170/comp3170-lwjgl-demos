package comp3170.demos.week12.demos;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glEnable;

import org.joml.Matrix4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.Window;
import comp3170.demos.week12.cameras.TestCamera;
import comp3170.demos.week12.sceneobjects.Frustum;

public class ProjectionMatrixTest implements IWindowListener {
	private Window window;
	private int screenWidth = 1000;
	private int screenHeight = 1000;
	
	private InputManager input;
	private long oldTime;
	
	private TestCamera camera;
	private Frustum frustum;

	public ProjectionMatrixTest() throws OpenGLException {
		window = new Window("Reflection demo", screenWidth, screenHeight, this);
		window.run();
	}
	
	@Override
	public void init() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_DEPTH_TEST);	

		camera = new TestCamera();
		frustum = new Frustum();
		
		input = new InputManager(window);
		oldTime = System.currentTimeMillis();
		
	}

	public void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000.0f;
		oldTime = time;
		
		camera.update(input, deltaTime);
		frustum.update(input, deltaTime);
		input.clear();
	}
	
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();
	
	@Override
	public void draw() {
		update();
		glClear(GL_COLOR_BUFFER_BIT);		

		glClearDepth(1f);
		glClear(GL_DEPTH_BUFFER_BIT);		
		
		camera.getViewMatrix(viewMatrix);
		camera.getProjectionMatrix(projectionMatrix);		
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);
		
		frustum.draw(mvpMatrix);
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
		new ProjectionMatrixTest();
	}

}
