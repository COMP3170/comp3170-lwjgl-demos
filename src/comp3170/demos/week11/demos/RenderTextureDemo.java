package comp3170.demos.week11.demos;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

import java.io.File;

import org.joml.Matrix4f;

import comp3170.GLBuffers;
import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.ShaderLibrary;
import comp3170.TextureLibrary;
import comp3170.Window;
import comp3170.demos.week11.cameras.Camera;
import comp3170.demos.week11.sceneobjects.AbstractScene;
import comp3170.demos.week11.sceneobjects.SceneOne;
import comp3170.demos.week11.sceneobjects.SceneZero;

public class RenderTextureDemo implements IWindowListener {

	private static final File COMMON_DIR = new File("src/comp3170/demos/common/shaders");
	private static final File SHADER_DIR = new File("src/comp3170/demos/week11/shaders");
	private static final File TEXTURE_DIR = new File("src/comp3170/demos/week11/textures");

	public static final String VERTEX_SHADER = "greyscaleVertex.glsl";
	public static final String FRAGMENT_SHADER = "greyscaleFragment.glsl";

	private Window window;
	private int screenWidth = 1000;
	private int screenHeight = 1000;

	private InputManager input;
	private long oldTime;

	private AbstractScene scene[];
	private int currentScene = 0;
	private int frameBuffer;


	public RenderTextureDemo() throws OpenGLException {
		window = new Window("Render texture demo", screenWidth, screenHeight, this);
		window.run();
	}

	@Override
	public void init() {
		glEnable(GL_DEPTH_TEST);

		new ShaderLibrary(COMMON_DIR).addPath(SHADER_DIR);
		new TextureLibrary(TEXTURE_DIR);

		int renderTexture = TextureLibrary.instance.createRenderTexture(screenWidth, screenHeight, GL_RGBA);

		try {
			frameBuffer = GLBuffers.createFrameBuffer(renderTexture);
		} catch (OpenGLException e) {
			e.printStackTrace();
			System.exit(1);
		}

		scene = new AbstractScene[] {
			new SceneZero(),
			new SceneOne(renderTexture)
		};

		input = new InputManager(window);
		oldTime = System.currentTimeMillis();
	}

	public void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000.0f;
		oldTime = time;

		if (input.wasKeyPressed(GLFW_KEY_SPACE)) {
			currentScene = 1 - currentScene;
		}

		scene[currentScene].update(input, deltaTime);
		input.clear();
	}

	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();

	@Override
	public void draw() {
		update();

		// Pass 1: Render Scene 0

		if (currentScene == 0) {
			// render to screen
			glBindFramebuffer(GL_FRAMEBUFFER, 0);
		}
		else {
			glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		}

		glViewport(0, 0, screenWidth, screenHeight);

		glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		glClear(GL_COLOR_BUFFER_BIT);

		glClearDepth(1f);
		glClear(GL_DEPTH_BUFFER_BIT);

		Camera camera = scene[0].getCamera();
		camera.getViewMatrix(viewMatrix);
		camera.getProjectionMatrix(projectionMatrix);
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);

		scene[0].draw(mvpMatrix);

		if (currentScene == 1) {
			// Pass 2: Render scene 1

			glBindFramebuffer(GL_FRAMEBUFFER, 0);
			glViewport(0, 0, screenWidth, screenHeight);

			glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			glClear(GL_COLOR_BUFFER_BIT);

			glClearDepth(1f);
			glClear(GL_DEPTH_BUFFER_BIT);

			camera = scene[1].getCamera();
			camera.getViewMatrix(viewMatrix);
			camera.getProjectionMatrix(projectionMatrix);
			mvpMatrix.set(projectionMatrix).mul(viewMatrix);

			scene[1].draw(mvpMatrix);
		}
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
		new RenderTextureDemo();
	}
}
