package comp3170.demos.week6.livedemo;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import org.joml.Matrix4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.SceneObject;
import comp3170.Window;
import static comp3170.Math.TAU;

public class Week6Demo implements IWindowListener {

	private Window window;
	private int screenWidth = 800;
	private int screenHeight = 800;
	private InputManager input;
	private long oldTime;
	private Icosahedron icosahedron;
	private SceneObject scene;

	public Week6Demo() throws OpenGLException {
		window = new Window("Regular Icosahedron", screenWidth, screenHeight, this);
		window.run();
	}
	
	@Override
	public void init() {
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		scene = new SceneObject();
		Axes3D axes = new Axes3D();
		axes.setParent(scene);
		icosahedron = new Icosahedron();
		icosahedron.setParent(scene);
		
		// initialise input & animation
		input = new InputManager(window);
	    oldTime = System.currentTimeMillis();
	}

	private Matrix4f cameraRotation = new Matrix4f();	
	private static final float ROTATION_SPEED = TAU/6;
	
	
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;
		
		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			cameraRotation.rotateY(ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			cameraRotation.rotateY(-ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_UP)) {
			cameraRotation.rotateX(ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			cameraRotation.rotateX(-ROTATION_SPEED * deltaTime);
		}
	}
	
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();

	private static final float CAMERA_NEAR = 0.5f;
	private static final float CAMERA_FAR = 3f;
	private static final float CAMERA_WIDTH = 2f;
	private static final float CAMERA_HEIGHT = 2f;
	
	@Override
	public void draw() {
		glClear(GL_COLOR_BUFFER_BIT);		

		update();

		viewMatrix.identity().mul(cameraRotation).translate(0,0,2);
		viewMatrix.invert();
		
		projectionMatrix.setOrthoSymmetric(CAMERA_WIDTH, CAMERA_HEIGHT, CAMERA_NEAR, CAMERA_FAR);
		projectionMatrix.mul(viewMatrix, mvpMatrix);
		
		scene.draw(mvpMatrix);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void close() {

	}

	public static void main(String[] args) throws OpenGLException {
		new Week6Demo();
	}
	
}
