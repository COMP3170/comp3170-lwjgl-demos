package comp3170.demos.week10.demos;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glEnable;

import java.io.File;

import org.joml.Matrix4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.ShaderLibrary;
import comp3170.TextureLibrary;
import comp3170.Window;
import comp3170.demos.week10.cameras.Camera;
import comp3170.demos.week10.cameras.ExplosionCamera;
import comp3170.demos.week10.sceneobjects.Explosion;

public class ParticleDemo implements IWindowListener {

	private static final File COMMON_DIR = new File("src/comp3170/demos/common/shaders"); 
	private static final File SHADER_DIR = new File("src/comp3170/demos/week10/shaders"); 
	private static final File TEXTURE_DIR = new File("src/comp3170/demos/week10/textures"); 

	public static ParticleDemo instance;
	
	private Window window;
	private int screenWidth = 1000;
	private int screenHeight = 1000;
	
	private InputManager input;
	private long oldTime;
	private ExplosionCamera camera;
	private Explosion explosion;

	public ParticleDemo() throws OpenGLException {
		// Simple singleton
		instance = this;
		window = new Window("Particle demo", screenWidth, screenHeight, this);
		window.run();		
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	@Override
	public void init() {
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		glClearDepth(1f);

		new ShaderLibrary(COMMON_DIR).addPath(SHADER_DIR);
		new TextureLibrary(TEXTURE_DIR);
		
		camera = new ExplosionCamera();
		explosion = new Explosion();

		input = new InputManager(window);
	    oldTime = System.currentTimeMillis();		
	}

	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;
		
		camera.update(input, deltaTime);
		input.clear();
	}
	
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();

	@Override
	public void draw() {
		update();
		
		glClear(GL_COLOR_BUFFER_BIT);		
		glClear(GL_DEPTH_BUFFER_BIT);		
		
		camera.getViewMatrix(viewMatrix);
		camera.getProjectionMatrix(projectionMatrix);		
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);
		
		explosion.draw(mvpMatrix);
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
		new ParticleDemo();
	}
}
