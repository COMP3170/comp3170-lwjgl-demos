
package comp3170.demos.week4.bezier;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.io.File;
import java.io.IOException;

import org.joml.Vector3f;

import comp3170.IWindowListener;
import comp3170.OpenGLException;
import comp3170.Shader;
import comp3170.Window;

public class BezierDemo implements IWindowListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto
	
	private int width = 800;
	private int height = 800;

	private Window window;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/demos/week4/bezier"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private BezierCurve curve;


	public BezierDemo() throws OpenGLException {
		window = new Window("Week 4 Camera Demo", width, height, this);
		window.setResizable(true);
		window.run();
	}

	@Override
	public void init() {
		
		// set the background colour to black
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		// Compile the shader
		try {
			File vertexShader = new File(DIRECTORY, VERTEX_SHADER);
			File fragementShader = new File(DIRECTORY, FRAGMENT_SHADER);
			this.shader = new Shader(vertexShader, fragementShader);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (OpenGLException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Set up the scene

		Vector3f points[] = new Vector3f[] {
			new Vector3f(-0.9f, -1f, 1f),
			new Vector3f(-0.9f,  1f, 1f),
			new Vector3f( 0.9f,  0.8f, 1f),
			new Vector3f( 0.9f,  1f, 1f),				
		};
		
		this.curve = new BezierCurve(shader, points);
	}

	public void draw() {
		
        // clear the colour buffer
		glClear(GL_COLOR_BUFFER_BIT);	

		// activate the shader
		this.shader.enable();		
		
		// draw the curve
		this.curve.draw();
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
