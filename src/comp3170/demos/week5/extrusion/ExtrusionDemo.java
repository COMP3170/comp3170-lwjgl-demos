package comp3170.demos.week5.extrusion;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.SceneObject;
import comp3170.Window;
import comp3170.demos.week5.mesh.sceneobjects.NormalisedCube;
import comp3170.demos.week5.mesh.sceneobjects.SimpleCube;
import comp3170.demos.week5.mesh.sceneobjects.UVSphere;

public class ExtrusionDemo implements IWindowListener {
	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto

	private Window window;
	private int screenWidth = 800;
	private int screenHeight = 800;
	private InputManager input;
	private long oldTime;

	private Extrusion mesh;

	public ExtrusionDemo() throws OpenGLException {
		window = new Window("Week 5 mesh demo", screenWidth, screenHeight, this);
		window.run();
	}
	
	@Override
	public void init() {
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		mesh = new Extrusion();
				
	    // initialise oldTime
		input = new InputManager(window);
	    oldTime = System.currentTimeMillis();

	}

	private final float ROTATION_SPEED = TAU / 8;

	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;

		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			mesh.getMatrix().rotateY(ROTATION_SPEED * deltaTime);			
		}
		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			mesh.getMatrix().rotateY(-ROTATION_SPEED * deltaTime);			
		}
		if (input.isKeyDown(GLFW_KEY_UP)) {
			mesh.getMatrix().rotateX(ROTATION_SPEED * deltaTime);			
		}
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			mesh.getMatrix().rotateX(-ROTATION_SPEED * deltaTime);			
		}
		
		input.clear();
	}

	@Override
	public void draw() {
		update();

		glClear(GL_COLOR_BUFFER_BIT);		
		mesh.draw();
	}

	@Override
	public void resize(int width, int height) {
		screenWidth = width;
		screenHeight = height;
		glViewport(0, 0, width, height);
	}

	@Override
	public void close() {
	}

	public static void main(String[] args) throws OpenGLException {
		new ExtrusionDemo();
	}
}
