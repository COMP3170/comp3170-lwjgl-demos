package comp3170.demos.week12.livedemo;

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

import org.joml.Matrix4f;

import comp3170.GLBuffers;
import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.Shader;
import comp3170.Window;
import comp3170.demos.week12.cameras.Camera;
import comp3170.demos.week12.sceneobjects.RenderQuad;
import comp3170.demos.week12.textures.TextureLibrary;
import comp3170.demos.week12.shaders.ShaderLibrary;

public class Week12 implements IWindowListener{

	public static final String VERTEX_SHADER = "postProcessingVertex.glsl";
	public static final String FRAGMENT_SHADER = "postProcessingFragment.glsl";
	public static final String OUTLINE_VERTEX_SHADER = "outlineVertex.glsl";
	public static final String OUTLINE_FRAGMENT_SHADER = "outlineFragment.glsl";
	
	private Window window;
	private int screenWidth = 1000;
	private int screenHeight = 1000;
	private int renderWidth = 100;
	private int renderHeight = 100;
	
	private InputManager input;
	private long oldTime;
	
	private Scene scene;
	private int renderTexture;
	private int frameBuffer;
	private Shader shader;
	private RenderQuad quad;

	public Week12() throws OpenGLException {
		window = new Window("Week 12 livedemo", screenWidth, screenHeight, this);
		window.run();
	}
	
	@Override
	public void init() {
		glClearColor(0.9f, 0.9f, 1.0f, 0.0f);
		glEnable(GL_DEPTH_TEST);	

		scene = new Scene();
		
		quad = new RenderQuad();
		renderTexture = TextureLibrary.createRenderTexture(renderWidth, renderHeight, GL_RGBA);
		try {
			frameBuffer = GLBuffers.createFrameBuffer(renderTexture);
		} catch (OpenGLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		shader = ShaderLibrary.compileShader(OUTLINE_VERTEX_SHADER, OUTLINE_FRAGMENT_SHADER);
		shader.setStrict(false);
		
		input = new InputManager(window);
		oldTime = System.currentTimeMillis();
	}

	public void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000.0f;
		oldTime = time;
		
		scene.update(input, deltaTime);
		input.clear();
	}
	
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();
	
	@Override
	public void draw() {
		update();
		
		// Pass 1: Render scene to texture
//		glBindFramebuffer(GL_FRAMEBUFFER, 0);		
//		glViewport(0, 0, screenWidth, screenHeight);
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);				
		glViewport(0, 0, renderWidth, renderHeight);
		glClear(GL_COLOR_BUFFER_BIT);		

		glClearDepth(1f);
		glClear(GL_DEPTH_BUFFER_BIT);		
		
		Camera camera = scene.getCamera();
		camera.getViewMatrix(viewMatrix);
		camera.getProjectionMatrix(projectionMatrix);		
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);
		
		scene.draw(mvpMatrix, 0);
		
		// Pass 2: Render texture to screen
		glBindFramebuffer(GL_FRAMEBUFFER, 0);

		glViewport(0, 0, screenWidth, screenHeight);
		glClear(GL_COLOR_BUFFER_BIT);		
		glClearDepth(1f);
		glClear(GL_DEPTH_BUFFER_BIT);		
		
		quad.setShader(shader);
		quad.setTexture(renderTexture);
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
		new Week12();
	}
}
