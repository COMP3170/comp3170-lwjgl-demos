package comp3170.demos.week8.demos;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL41.*;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.awt.Color;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.SceneObject;
import comp3170.Window;
import comp3170.demos.week8.cameras.PerspectiveCamera;
import comp3170.demos.week8.sceneobjects.Quad;

public class ZFightingDemo implements IWindowListener {

	private Window window;
	private int screenWidth = 800;
	private int screenHeight = 800;
	private InputManager input;
	private long oldTime;
	private SceneObject scene;
	private PerspectiveCamera camera;
	private Quad blueQuad;

	public ZFightingDemo() throws OpenGLException {
		window = new Window("Z-Fighting demo", screenWidth, screenHeight, this);
		window.setSamples(0);
		window.run();		
	}
	
	@Override
	public void init() {
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		
		// set up scene
		scene = new SceneObject();
	
		Quad redQuad = new Quad(Color.red);
		redQuad.setParent(scene);

		blueQuad = new Quad(Color.blue);
		blueQuad.setParent(scene);
		blueQuad.getMatrix().rotateY(0.0001f);
		
		camera = new PerspectiveCamera();
		
	    // initialise oldTime
		input = new InputManager(window);
	    oldTime = System.currentTimeMillis();
	    
	    // find out how many bits in the depth buffer (system dependent)
		try (MemoryStack stack = stackPush()) {

			IntBuffer bits = stack.mallocInt(1);
			glGetFramebufferAttachmentParameteriv(
				GL_DRAW_FRAMEBUFFER,
				GL_DEPTH,
				GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE,
				bits );
			System.out.println("depth bits = " + bits.get(0));
		}

	}
	
	private static final float ROTATION_SPEED = TAU/20;

	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;
		
		camera.update(input, deltaTime);
		
		if (input.isKeyDown(GLFW_KEY_A)) {
			blueQuad.getMatrix().rotateY(ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			blueQuad.getMatrix().rotateY(-ROTATION_SPEED * deltaTime);
		}
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
		new ZFightingDemo();
	}
}
