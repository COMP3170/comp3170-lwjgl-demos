
package comp3170.demos.week4.bezier;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.io.File;

import comp3170.IWindowListener;
import comp3170.OpenGLException;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.Window;

public class BezierDemo implements IWindowListener {

	private int width = 800;
	private int height = 800;

	private Window window;
	private Shader shader;

	final private File DIRECTORY = new File("src/comp3170/demos/week4/bezier");
	private Scene scene;


	public BezierDemo() throws OpenGLException {
		window = new Window("Week 4 Camera Demo", width, height, this);
		window.setResizable(true);
		window.run();
	}

	@Override
	public void init() {

		// set the background colour
		glClearColor(0.1f, 0.f, 0.1f, 1.0f);

		new ShaderLibrary(DIRECTORY);
		scene = new Scene();
	}

	@Override
	public void draw() {

        // clear the colour buffer
		glClear(GL_COLOR_BUFFER_BIT);

		// draw the curve
		scene.draw();
	}

	public static void main(String[] args) throws OpenGLException {
		new BezierDemo();
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}


}
