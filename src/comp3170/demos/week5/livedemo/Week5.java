package comp3170.demos.week5.livedemo;

import static comp3170.demos.week5.livedemo.Scene.TAU;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.Window;

public class Week5 implements IWindowListener {

	private Window window;
	private int screenWidth = 800;
	private int screenHeight = 800;
	private InputManager input;
	private long oldTime;
	private Scene scene;

	public Week5() throws OpenGLException {
		window = new Window("Week 5 mesh demo", screenWidth, screenHeight, this);
		window.run();		
	}
	
	@Override
	public void init() {
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		scene = new Scene();
		
	    // initialise oldTime
		input = new InputManager(window);
	    oldTime = System.currentTimeMillis();
	}

	
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;
	}
		
	private Matrix4f cameraModelMatrix = new Matrix4f();
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();
	
	private Vector4f cameraPos = new Vector4f(0f,0,0,1);
	private float cameraRotation = 0;	
	private float cameraWidth = 4;
	private float cameraHeight = 4;
	
	@Override
	public void draw() {
		update();
		
		glClear(GL_COLOR_BUFFER_BIT);	
		
		cameraModelMatrix.identity();
		cameraModelMatrix.rotateZ(cameraRotation); 		
		cameraModelMatrix.translate(cameraPos.x, cameraPos.y, cameraPos.z); 
		cameraModelMatrix.invert(viewMatrix);	// Mv = Mc ^ -1
						
		projectionMatrix.scaling(2/ cameraWidth, 2/ cameraHeight, 1);
		projectionMatrix.mul(viewMatrix, mvpMatrix); // MVP = P * V;
		
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
		new Week5();
	}
	
}
