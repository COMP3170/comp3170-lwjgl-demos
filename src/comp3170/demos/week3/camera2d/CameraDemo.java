package comp3170.demos.week3.camera2d;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_0;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_4;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_6;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_7;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_8;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_9;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glScissor;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.File;
import java.io.IOException;

import org.joml.Matrix4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.Shader;
import comp3170.Window;

/**
 * @author malcolmryan
 *
 */

public class CameraDemo implements IWindowListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto

	final private File DIRECTORY = new File("src/comp3170/demos/week3/camera2d");
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";
	
	private Shader shader;
	private Window window;

	private int width = 1200;
	private int height = 1200;
	
	private long oldTime;
	private InputManager input;

	private Axes worldAxes;
	private Axes modelAxes;
	private Axes cameraAxes;
	private House house;
	private Camera camera;
	
	public CameraDemo() throws OpenGLException {
		window = new Window("Model / World / View / NDC", width, height, this);
		window.setResizable(true);
		window.run();
	}

	/**
	 * Initialise the GLCanvas
	 * 
	 * <img src="images/square.png" />
	 * 
	 */
	@Override
	public void init() {
		glEnable(GL_SCISSOR_TEST);

		// Compile the shader
		shader = compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		worldAxes = new Axes();
		modelAxes = new Axes();
		cameraAxes = new Axes();
		house = new House();
		camera = new Camera();
				
	    // initialise oldTime
	    oldTime = System.currentTimeMillis();
		// add an input manager 
		input = new InputManager(window);

	}

	private Shader compileShader(String vertexShader, String fragmentShader) {
		try {
			File vs = new File(DIRECTORY, vertexShader);
			File fs = new File(DIRECTORY, fragmentShader);
			shader = new Shader(vs, fs);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (OpenGLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return shader;
	}

	private Matrix4f identity = new Matrix4f().identity();
	private Matrix4f cameraModelMatrix = new Matrix4f();
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f perspectiveMatrix = new Matrix4f();

	private static final float MOVEMENT_SPEED = 0.1f;
	private static final float ROTATION_SPEED = TAU / 6;
	private static final float SCALE_SPEED = 1.1f;
	private float cameraScale = 1;
	
	private void update() {
		long time = System.currentTimeMillis();
		float dt = (time - oldTime) / 1000f;
		oldTime = time;

		house.update(input, dt);
		
		// Camera movement using keypad KP_
		
		if (input.isKeyDown(GLFW_KEY_KP_1)) {
			float s = (float)Math.pow(SCALE_SPEED, dt);
			cameraScale *= s;
			cameraModelMatrix.scale(s); 		// scale up
			perspectiveMatrix.scale(1f/s); 	// scale down
		}
		if (input.isKeyDown(GLFW_KEY_KP_0)) {
			float s = (float)Math.pow(SCALE_SPEED, dt);
			cameraScale /= s;
			cameraModelMatrix.scale(1f/s); 		// scale down
			perspectiveMatrix.scale(s); 	// scale up
		}
		
		if (input.isKeyDown(GLFW_KEY_KP_7)) {
			cameraModelMatrix.rotateZ(ROTATION_SPEED * dt);	
			viewMatrix.rotateLocalZ(-ROTATION_SPEED * dt);	
		}
		if (input.isKeyDown(GLFW_KEY_KP_9)) {
			cameraModelMatrix.rotateZ(-ROTATION_SPEED * dt);	
			viewMatrix.rotateLocalZ(ROTATION_SPEED * dt);	
		}
		if (input.isKeyDown(GLFW_KEY_KP_8)) {
			cameraModelMatrix.translate(0, MOVEMENT_SPEED / cameraScale * dt, 0);	
			viewMatrix.translateLocal(0, -MOVEMENT_SPEED * dt, 0);	
		}
		if (input.isKeyDown(GLFW_KEY_KP_2)) {
			cameraModelMatrix.translate(0, -MOVEMENT_SPEED / cameraScale * dt, 0);	
			viewMatrix.translateLocal(0, MOVEMENT_SPEED * dt, 0);	
		}
		if (input.isKeyDown(GLFW_KEY_KP_4)) {
			cameraModelMatrix.translate(-MOVEMENT_SPEED / cameraScale *dt, 0, 0);	
			viewMatrix.translateLocal(MOVEMENT_SPEED * dt, 0, 0);	
		}
		if (input.isKeyDown(GLFW_KEY_KP_6)) {
			cameraModelMatrix.translate(MOVEMENT_SPEED / cameraScale *dt, 0, 0);	
			viewMatrix.translateLocal(-MOVEMENT_SPEED * dt, 0, 0);	
		}

		input.clear();
	}
	
	@Override
	/**
	 * Called when the canvas is redrawn
	 */
	public void draw() {	
		update();
		
		shader.enable();

		// MODEL COORDINATES
		
		glViewport(0,height/2,width/2,height/2);
		glScissor(0,height/2,width/2,height/2);
		glClearColor(0.1f, 0.0f, 0.0f, 1.0f);			
		glClear(GL_COLOR_BUFFER_BIT);		
		
		shader.setUniform("u_modelMatrix", identity);
		shader.setUniform("u_viewMatrix", identity);		
		shader.setUniform("u_perspectiveMatrix", identity);	
		
		modelAxes.draw(shader);
		house.draw(shader);

		// WORLD COORDINATES
		
		glViewport(width/2,height/2,width/2,height/2);
		glScissor(width/2,height/2,width/2,height/2);
		glClearColor(0.0f, 0.1f, 0.0f, 1.0f);			
		glClear(GL_COLOR_BUFFER_BIT);		
		
		shader.setUniform("u_modelMatrix", identity);
		shader.setUniform("u_viewMatrix", identity);		
		shader.setUniform("u_perspectiveMatrix", identity);		
		worldAxes.draw(shader);

		shader.setUniform("u_modelMatrix", house.getMatrix());
		modelAxes.draw(shader);
		house.draw(shader);

		shader.setUniform("u_modelMatrix", cameraModelMatrix);
		cameraAxes.draw(shader);
		camera.draw(shader);

		// VIEW COORDINATES

		glViewport(0,0,width/2,height/2);
		glScissor(0,0,width/2,height/2);
		glClearColor(0.0f, 0.0f, 0.1f, 1.0f);			
		glClear(GL_COLOR_BUFFER_BIT);		

		shader.setUniform("u_viewMatrix", viewMatrix);		
		shader.setUniform("u_perspectiveMatrix", identity);		

		shader.setUniform("u_modelMatrix", identity);
		worldAxes.draw(shader);

		shader.setUniform("u_modelMatrix", house.getMatrix());
		modelAxes.draw(shader);
		house.draw(shader);

		shader.setUniform("u_modelMatrix", cameraModelMatrix);
		cameraAxes.draw(shader);
		camera.draw(shader);

		// NDC COORDINATES
		
		glViewport(width/2,0,width/2,height/2);
		glScissor(width/2,0,width/2,height/2);
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);			
		glClear(GL_COLOR_BUFFER_BIT);		
		
		shader.setUniform("u_viewMatrix", viewMatrix);		
		shader.setUniform("u_perspectiveMatrix", perspectiveMatrix);		

		shader.setUniform("u_modelMatrix", identity);
		worldAxes.draw(shader);

		shader.setUniform("u_modelMatrix", house.getMatrix());
		modelAxes.draw(shader);
		house.draw(shader);

	}

	/**
	 * Called when the canvas is resized
	 */

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		glViewport(0, 0, width, height);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws IOException, OpenGLException {
		new CameraDemo();
	}

}
