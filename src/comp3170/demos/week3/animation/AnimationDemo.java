package comp3170.demos.week3.animation;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.joml.Vector2f;

import comp3170.IWindowListener;
import comp3170.OpenGLException;
import comp3170.Shader;
import comp3170.Window;

/**
 * @author malcolmryan
 *
 */

public class AnimationDemo implements IWindowListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto

	final private File DIRECTORY = new File("src/comp3170/demos/week3/animation");
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	final private int NSQUARES = 100;
	
	private Shader shader;
	private Window window;

	private int screenWidth = 800;
	private int screenHeight = 800;
	private ArrayList<Square> squares;
	
	private int frameRate = 100;
	
	private long oldTime;
	
	public AnimationDemo() throws OpenGLException {
		window = new Window("Week 3", screenWidth, screenHeight, this);
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
				
		// Compile the shader
		shader = compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

	    squares = new ArrayList<Square>();
	    
	    for (int i = 0; i < NSQUARES; i++) {
			Square square = new Square();
			float x = (float) Math.random() * 2 - 1;
			float y = (float) Math.random() * 2 - 1;
			square.setPosition(x, y);
			Color colour = Color.getHSBColor((float) Math.random(), 1, 1);
			square.setColour(colour);
			square.setAngle(0);
			square.setScale(0.1f, 0.1f);
			squares.add(square);
	    }
	    
	    // initialise oldTime
	    oldTime = System.currentTimeMillis();
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

	private static final Vector2f MOVEMENT_SPEED = new Vector2f(0.0f, 0);
	private static final float ROTATION_SPEED = TAU / 6;
	private static final float SCALE_SPEED = 1.0f;

	private Vector2f movement = new Vector2f();

	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;
		System.out.println("update: dt = " + deltaTime + "s");
				
		for (Square sq : squares) {
			MOVEMENT_SPEED.mul(deltaTime, movement);  // movement = speed * dt;
			sq.translate(movement);
			sq.rotate(ROTATION_SPEED * deltaTime); 
			sq.scale((float) Math.pow(SCALE_SPEED, deltaTime)); 
		}	
	}
	
	@Override
	/**
	 * Called when the canvas is redrawn
	 */
	public void draw() {
		update();
		
		// clear the colour buffer to black

		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);
		
		// activate the shader
		shader.enable();

		for (Square square : squares) {
			square.draw(shader);
		}		

		// restrict the framerate by sleeping between frames
		try {
			TimeUnit.MILLISECONDS.sleep(1000 / frameRate);
		} catch (InterruptedException e) {
		}

	}

	/**
	 * Called when the canvas is resized
	 */

	@Override
	public void resize(int width, int height) {
		this.screenWidth = width;
		this.screenHeight = height;
		glViewport(0, 0, width, height);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws IOException, OpenGLException {
		new AnimationDemo();
	}

}
