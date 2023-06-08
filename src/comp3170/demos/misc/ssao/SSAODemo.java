package comp3170.demos.misc.ssao;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.ShaderLibrary;
import comp3170.Window;

public class SSAODemo implements IWindowListener {

	private static final String COMMON_SHADERS_DIR = "src/comp3170/demos/common/shaders";
	private Window window;
	private int screenWidth = 1000;
	private int screenHeight = 1000;
	
	private InputManager input;
	private long oldTime;

	public SSAODemo() throws OpenGLException {
		window = new Window("SSAO demo", screenWidth, screenHeight, this);
		window.run();
	}
	
	@Override
	public void init() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClearDepth(1f);
		glEnable(GL_DEPTH_TEST);	

		ShaderLibrary shaderLibrary = new ShaderLibrary(COMMON_SHADERS_DIR);
		
		input = new InputManager(window);
		oldTime = System.currentTimeMillis();
	}

	public void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000.0f;
		oldTime = time;
		
		input.clear();
	}

	@Override
	public void draw() {
		update();
		
		glViewport(0, 0, screenWidth, screenHeight);
		glClear(GL_COLOR_BUFFER_BIT);		
		glClear(GL_DEPTH_BUFFER_BIT);		
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
