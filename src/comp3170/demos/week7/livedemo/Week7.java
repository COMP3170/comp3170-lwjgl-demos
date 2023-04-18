package comp3170.demos.week7.livedemo;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.opengl.GL11.glClearColor;

import org.joml.Matrix4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.SceneObject;
import comp3170.Window;
import comp3170.demos.week6.livedemo.Axes3D;
import comp3170.demos.week6.livedemo.Icosahedron;

public class Week7 implements IWindowListener {

	private Window window;
	private int screenWidth = 800;
	private int screenHeight = 800;
	private InputManager input;
	private long oldTime;

	public Week7() throws OpenGLException {
		window = new Window("Regular Icosahedron", screenWidth, screenHeight, this);
		window.run();	
	}
	
	@Override
	public void init() {
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		
		// initialise input & animation
		input = new InputManager(window);
	    oldTime = System.currentTimeMillis();
	}

	
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;
	}

	@Override
	public void draw() {
		update();

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
		new Week7();
	}
}
