package comp3170.demos.week7.demos;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import org.joml.Vector4f;

import comp3170.IWindowListener;
import comp3170.OpenGLException;
import comp3170.Window;
import comp3170.demos.week7.sceneobjects.BarycentricTriangle;

public class BarycentricDemo implements IWindowListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto

	private Window window;
	private int screenWidth = 800;
	private int screenHeight = 800;
	private Vector4f[] vertices;
	private int vertexBuffer;

	private BarycentricTriangle triangle;

	public BarycentricDemo() throws OpenGLException {
		window = new Window("Barycentric interpoliation demo", screenWidth, screenHeight, this);
		window.setSamples(4);	// set the number of samples or 0 to disable
		window.run();
	}

	
	@Override
	public void init() {
		glClearColor(0.5f, 0.0f, 0.0f, 1.0f);

		triangle = new BarycentricTriangle();
	}

	
	@Override
	public void draw() {
		glClear(GL_COLOR_BUFFER_BIT);		
		triangle.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void close() {

	}

	public static void main(String[] args) throws OpenGLException {
		new BarycentricDemo();
	}
}
