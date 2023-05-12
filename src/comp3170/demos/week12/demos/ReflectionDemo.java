package comp3170.demos.week12.demos;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glScissor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.Window;
import comp3170.demos.week12.cameras.Camera;
import comp3170.demos.week12.sceneobjects.Mirror;
import comp3170.demos.week12.sceneobjects.ReflectionScene;

public class ReflectionDemo implements IWindowListener {

	private Window window;
	private int screenWidth = 2400;
	private int screenHeight = 800;
	
	private InputManager input;
	private long oldTime;
	
	private ReflectionScene scene;

	public ReflectionDemo() throws OpenGLException {
		window = new Window("Reflection demo", screenWidth, screenHeight, this);
		window.run();
	}
	
	@Override
	public void init() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_DEPTH_TEST);	
		glEnable(GL_SCISSOR_TEST);

		
		scene = new ReflectionScene();
		
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

		Mirror mirror = scene.getMirror();

		// render to texture
		int frameBuffer = mirror.getFrameBuffer();		
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);		
		scene.setDrawVolumes(false);
		mirror.setDrawn(false);
		draw(2, scene.getMirrorCamera());		

		// render to window 0
		scene.setDrawVolumes(true);
		mirror.setDrawn(true);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);		
		draw(0, scene.getOverheadCamera());

		// render to window 1
		scene.setDrawVolumes(false);
		mirror.setDrawn(true);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);		
		draw(1, scene.getMainCamera());
		
		// render to window 2 
		glBindFramebuffer(GL_FRAMEBUFFER, 0);		
		scene.setDrawVolumes(false);
		mirror.setDrawn(false);
		draw(2, scene.getMirrorCamera());		


	}

	private Vector4f[] clearColours = new Vector4f[] {
		new Vector4f(0.1f, 0, 0, 1),
		new Vector4f(0, 0.1f, 0, 1),
		new Vector4f(0, 0, 0.1f, 1),
	};
	
	private void draw(int window, Camera camera) {
		glViewport(window * screenWidth / 3, 0, screenWidth / 3, screenHeight);
		glScissor(window * screenWidth / 3, 0, screenWidth / 3, screenHeight);
		
		Vector4f c = clearColours[window];
		glClearColor(c.x, c.y, c.z, c.w);
		glClear(GL_COLOR_BUFFER_BIT);		

		glClearDepth(1f);
		glClear(GL_DEPTH_BUFFER_BIT);		
		
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
		new ReflectionDemo();
	}
}
