package comp3170.demos.week5.mesh;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.File;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.SceneObject;
import comp3170.ShaderLibrary;
import comp3170.Window;

public class MeshDemo implements IWindowListener {

	private static final File COMMON_DIR = new File("src/comp3170/demos/common/shaders"); 

	private Window window;
	private int screenWidth = 800;
	private int screenHeight = 800;
	private InputManager input;
	private long oldTime;
	private SceneObject[] meshes;
	private int currentMesh;

	private Scene scene;

	public MeshDemo() throws OpenGLException {
		window = new Window("Week 5 mesh demo", screenWidth, screenHeight, this);
		window.run();
	}
	
	@Override
	public void init() {
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		// using the common shaders
		new ShaderLibrary(COMMON_DIR);
		
		scene = new Scene();
		
	    // initialise oldTime
		input = new InputManager(window);
	    oldTime = System.currentTimeMillis();

	}

	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;

		scene.update(deltaTime, input);
		
		input.clear();
	}

	@Override
	public void draw() {
		update();

		glClear(GL_COLOR_BUFFER_BIT);
		scene.draw();
	}

	@Override
	public void resize(int width, int height) {
		screenWidth = width;
		screenHeight = height;
		glViewport(0, 0, width, height);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws OpenGLException {
		new MeshDemo();
	}
}
