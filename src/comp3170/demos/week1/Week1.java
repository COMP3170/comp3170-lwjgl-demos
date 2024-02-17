package comp3170.demos.week1;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.File;
import java.io.IOException;

import comp3170.IWindowListener;
import comp3170.OpenGLException;
import comp3170.ShaderLibrary;
import comp3170.Window;

/**
 * @author malcolmryan
 *
 */

public class Week1 implements IWindowListener {

	final private File DIRECTORY = new File("src/comp3170/demos/week1");

	private Window window;

	private int screenWidth = 800;
	private int screenHeight = 800;
	private Scene scene;

	public Week1() throws OpenGLException {
		window = new Window("Week 1", screenWidth, screenHeight, this);
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

		ShaderLibrary shaderLibrary = new ShaderLibrary(DIRECTORY);

		scene = new Scene(screenWidth, screenHeight);
	}

	@Override
	/**
	 * Called when the canvas is redrawn
	 */
	public void draw() {
		// clear the colour buffer to black

		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);

		// draw the scene
		scene.draw();
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
		new Week1();
	}

}
