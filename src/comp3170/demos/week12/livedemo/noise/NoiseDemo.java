package comp3170.demos.week12.livedemo.noise;

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
import comp3170.Shader;
import comp3170.Window;
import comp3170.demos.week12.sceneobjects.RenderQuad;
import comp3170.demos.week12.shaders.ShaderLibrary;

public class NoiseDemo implements IWindowListener {

	private static final String VERTEX_SHADER = "noiseVertex.glsl";
	private static final String FRAGMENT_SHADER = "noiseFragment.glsl";
	private Window window;
	private int screenWidth = 1000;
	private int screenHeight = 1000;
	
	private InputManager input;
	private long startTime;
	private long oldTime;
	private RenderQuad quad;
	private Shader shader;

	public NoiseDemo() throws OpenGLException {
		window = new Window("Noise demo", screenWidth, screenHeight, this);
		window.run();
	}

	@Override
	public void init() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_DEPTH_TEST);	

		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		shader.setStrict(false);

		quad = new RenderQuad();		
		input = new InputManager(window);
		startTime = System.currentTimeMillis();
		oldTime = System.currentTimeMillis();
	}

	public void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000.0f;
		oldTime = time;
		
		shader.setUniform("u_time", (time - startTime) / 1000f);
		
		input.clear();
	}

	@Override
	public void draw() {
		update();
		
		glViewport(0, 0, screenWidth, screenHeight);
		glClear(GL_COLOR_BUFFER_BIT);		
		glClearDepth(1f);
		glClear(GL_DEPTH_BUFFER_BIT);		
		
		quad.setShader(shader);
		quad.draw();		
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
		new NoiseDemo();
	}
	
}
