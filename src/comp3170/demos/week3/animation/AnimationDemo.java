package comp3170.demos.week3.animation;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import comp3170.IWindowListener;
import comp3170.OpenGLException;
import comp3170.ShaderLibrary;
import comp3170.Window;

/**
 * @author malcolmryan
 *
 */

public class AnimationDemo implements IWindowListener {

	final private File DIRECTORY = new File("src/comp3170/demos/week3/animation");


	private Window window;

	private int screenWidth = 800;
	private int screenHeight = 800;

	private int frameRate = 100;

	private long oldTime;


	private Scene scene;

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

		new ShaderLibrary(DIRECTORY);
		scene = new Scene();

	    // initialise oldTime
	    oldTime = System.currentTimeMillis();
	}


	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;
		System.out.println("update: dt = " + deltaTime + "s");

		scene.update(deltaTime);
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

		scene.draw();

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
