package comp3170.demos.week8.demos;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.File;

import org.joml.Matrix4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.ShaderLibrary;
import comp3170.Window;
import comp3170.demos.common.cameras.Camera;
import comp3170.demos.week8.sceneobjects.TransparencyScene;

public class TransparencyDemo implements IWindowListener {

	private static final File COMMON_DIR = new File("src/comp3170/demos/common/shaders");

	private Window window;
	private int screenWidth = 800;
	private int screenHeight = 800;
	private InputManager input;
	private long oldTime;

	private TransparencyScene scene;

	public TransparencyDemo() throws OpenGLException {
		window = new Window("Transparency Demo", screenWidth, screenHeight, this);
		window.run();
	}

	@Override
	public void init() {

		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);

		// enable alpha blending
		glEnable(GL_BLEND);

		// setting the blend function to c = a * c_new + (1-a) c_old
		glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);


		new ShaderLibrary(COMMON_DIR);
		// set up scene
		scene = new TransparencyScene();

		// initialise oldTime
		input = new InputManager(window);
		oldTime = System.currentTimeMillis();
	}

	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;

		scene.update(deltaTime, input);
	}

	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();

	@Override
	public void draw() {
		update();

		glClear(GL_COLOR_BUFFER_BIT);
		glViewport(0, 0, screenWidth, screenHeight);

		glClearDepth(1);
		glClear(GL_DEPTH_BUFFER_BIT);

		Camera camera = scene.getCamera();
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
		new TransparencyDemo();
	}

}
