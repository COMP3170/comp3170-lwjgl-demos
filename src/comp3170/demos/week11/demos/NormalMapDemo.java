package comp3170.demos.week11.demos;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.File;

import org.joml.Matrix4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.ShaderLibrary;
import comp3170.TextureLibrary;
import comp3170.Window;
import comp3170.demos.week11.cameras.Camera;
import comp3170.demos.week11.sceneobjects.NormalMapScene;

public class NormalMapDemo implements IWindowListener {

	private static final File COMMON_DIR = new File("src/comp3170/demos/common/shaders"); 
	private static final File SHADER_DIR = new File("src/comp3170/demos/week11/shaders"); 
	private static final File TEXTURE_DIR = new File("src/comp3170/demos/week11/textures"); 

	private Window window;
	private int screenWidth = 1000;
	private int screenHeight = 1000;
	
	private InputManager input;
	private long oldTime;
	
	private NormalMapScene scene;

	public NormalMapDemo() throws OpenGLException {
		window = new Window("Normal amp demo", screenWidth, screenHeight, this);
		window.run();		
	}
	
	@Override
	public void init() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_DEPTH_TEST);	
		
		new ShaderLibrary(COMMON_DIR).addPath(SHADER_DIR);
		new TextureLibrary(TEXTURE_DIR);
		
		scene = new NormalMapScene();
		
		input = new InputManager(window);
		oldTime = System.currentTimeMillis();
	}

	public void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000.0f;
		oldTime = time;
		
		scene.update(deltaTime, input);
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
		new NormalMapDemo();
	}
}
