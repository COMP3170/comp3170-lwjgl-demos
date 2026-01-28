package comp3170.demos.template;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
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
import comp3170.TextureLibrary;
import comp3170.Window;
import comp3170.demos.common.cameras.Camera;

/**
 * Template for Demo scenes.
 * 
 * This class implements the IWindowListener interface which includes the event handling methods:
 * 
 * - init() - run once when the window is created to initialise everything
 * - draw() - run every frame, to update & render the scene
 * - resize() - run when the window is resized (e.g. to resize the camera aspect)
 * - close() - run when the window is closed (we don't tend to use this)
 * 
 */

public class Demo implements IWindowListener{

	// Paths for shaders and textures
	// The 'common' package contains some standard shaders
	// I typically put custom shaders in the 'shaders' dir in the package
	// And textures in the 'textures' dir

	private static final File COMMON_DIR = new File("src/comp3170/demos/common/shaders");
	private static final File SHADER_DIR = new File("src/comp3170/demos/week12/shaders");
	private static final File TEXTURE_DIR = new File("src/comp3170/demos/week12/textures");

	private Window window;
	private int screenWidth = 1000;
	private int screenHeight = 1000;

	private InputManager input;	// interface for mouse & keyboard input
	private long oldTime;		// used in update to calcualte deltaTime

	private Scene scene;		// The scene graph

	/**
	 * The constructor typically just creates a 
	 * 
	 * @throws OpenGLException
	 */
	
	public Demo() throws OpenGLException {
		window = new Window("CubemDemo", screenWidth, screenHeight, this);
		window.setResizable(true);	// make the window resizable
		window.setSamples(4); // use anti-aliasing
		window.run();
	}

	/**
	 * Initialise OpenGL and create the scene
	 */
	@Override
	public void init() {
		
		// Enable required GL features & configure
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);	
		glEnable(GL_CULL_FACE);

		// Create ShaderLibrary and TextureLibrary singletons and 
		// configure the paths they will use to find files
		
		new ShaderLibrary(COMMON_DIR).addPath(SHADER_DIR);
		new TextureLibrary(TEXTURE_DIR);

		// create the scene graph
		// all objects should be created in the Scene constructor
		scene = new Scene();

		// initialise the input manager
		input = new InputManager(window);
		
		// record the starting time (used in update)
		oldTime = System.currentTimeMillis();

	}

	/**
	 * Update the world (e.g. moving objects in the scene)
	 * This is called from draw() below at the beginning of every frame 
	 */
	
	private void update() {
		// calculate the time that has passed since the last update
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000.0f;
		oldTime = time;

		// update the scene
		scene.update(deltaTime, input);
		
		// input needs to be cleared at the end of every frame
		input.clear();
	}

	// To avoid garbage collection, we typically allocate matrices once
	// and re-use them, rather than allocating them on every frame.
	
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();
	
	/**
	 * Redraw the scene.
	 * 
	 * This is called repeatedly at a high frame rate.
	 */

	@Override
	public void draw() {
		// update the scene before drawing
		update();
		
		// clear buffers before drawing
		glViewport(0, 0, screenWidth, screenHeight);
		glClear(GL_COLOR_BUFFER_BIT);

		glClearDepth(1f);
		glClear(GL_DEPTH_BUFFER_BIT);

		// We typically use a Camera object to implement the view and projection matrices
		// We multiply the view and projection matrices together to get the MVP matrix
		
		Camera camera = scene.getCamera();
		camera.getViewMatrix(viewMatrix);
		camera.getProjectionMatrix(projectionMatrix);
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);

		// Traverse the scene-graph drawing each of the objects
		// The MVP matrix is calculated recursively as we navigate the scene graph
		
		scene.draw(mvpMatrix);
	}

	/**
	 * The window has been resized.
	 * This is always called between init() and the first call to draw()
	 */
	
	@Override
	public void resize(int width, int height) {
		screenWidth = width;
		screenHeight = height;
		// adjust the camera here if necessary
		Camera camera = scene.getCamera();
		// ...
	}

	@Override
	public void close() {
		// We generally don't do anything here
	}

	public static void main(String[] args) throws OpenGLException {
		// just call the constructor, it will take care of the rest
		new Demo();
	}
}
