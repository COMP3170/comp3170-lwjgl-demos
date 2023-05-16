package comp3170.demos.week6.livedemo;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import org.joml.Matrix4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.SceneObject;
import comp3170.Window;
import comp3170.demos.common.sceneobjects.Axes3D;

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
		glEnable(GL_CULL_FACE);

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
		
		if (input.isKeyDown(GLFW_KEY_Z)) {
			cameraFOVY += CAMERA_FOVY_RATE * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_X)) {
			cameraFOVY -= CAMERA_FOVY_RATE * deltaTime;
		}
	}
	
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();

	private static final float CAMERA_NEAR = 0.5f;
	private static final float CAMERA_FAR = 3f;
	private static final float CAMERA_WIDTH = 2f;
	private static final float CAMERA_HEIGHT = 2f;
	private float cameraFOVY = TAU / 4;	
	private static final float CAMERA_FOVY_RATE = TAU / 6;	
	
	@Override
	public void draw() {
		glClear(GL_COLOR_BUFFER_BIT);		

		update();

		viewMatrix.identity().mul(cameraRotation).translate(0,0,2);
		viewMatrix.invert();
		viewMatrix.normalize3x3();

		float aspect = (float)screenWidth / screenHeight;
		projectionMatrix.setOrthoSymmetric(CAMERA_WIDTH, CAMERA_HEIGHT, CAMERA_NEAR, CAMERA_FAR);
//		projectionMatrix.setPerspective(cameraFOVY, aspect, CAMERA_NEAR, CAMERA_FAR);
		
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
