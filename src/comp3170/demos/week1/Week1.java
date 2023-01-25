package comp3170.demos.week1;

import java.io.File;
import java.io.IOException;

import org.lwjgl.opengl.GLCapabilities;
import static org.lwjgl.opengl.GL20.*;

import comp3170.GLBuffers;
import comp3170.IWindowListener;
import comp3170.OpenGLException;
import comp3170.Shader;
import comp3170.Window;

/**
 * @author malcolmryan
 *
 */

public class Week1 implements IWindowListener {

	final private File DIRECTORY = new File("src/comp3170/demos/week1");
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private Shader shader;
	private Window window;

	private float[] vertices;
	private int vertexBuffer;

	private int screenWidth = 800;
	private int screenHeight = 800;

	public Week1() throws OpenGLException {
		window = new Window("Week 1", screenWidth, screenHeight, true, this);
		window.run();
	}

	/**
	 * Initialise the GLCanvas
	 * 
	 * <img src="images/square.png" />
	 * 
	 */
	@Override
	public void init(GLCapabilities capabilities) {
		// Compile the shader
		try {
			File vertexShader = new File(DIRECTORY, VERTEX_SHADER);
			File fragementShader = new File(DIRECTORY, FRAGMENT_SHADER);
			shader = new Shader(vertexShader, fragementShader);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (OpenGLException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// create the shape

		// vertices of a square as (x,y) pairs
		// @formatter:off

		vertices = new float[] {
			1.0f, 1.0f, 
			-1.0f, 1.0f, 
			-1.0f, -1.0f,

			-1.0f, -1.0f, 
			1.0f, -1.0f, 
			1.0f, 1.0f, 
		};
		// @formatter:on

		// copy the data into a Vertex Buffer Object in graphics memory
		vertexBuffer = GLBuffers.createBuffer(vertices, GL_FLOAT_VEC2);
	}

	@Override
	/**
	 * Called when the canvas is redrawn
	 */
	public void draw() {
		// clear the colour buffer to black

		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);
		
		// activate the shader
		shader.enable();

		// connect the vertex buffer to the a_position attribute
		shader.setAttribute("a_position", vertexBuffer);

	    // write the colour value into the u_colour uniform 
	    float[] colour = {1.0f, 0.0f, 0.0f};	    
        shader.setUniform("u_colour", colour);
        
        float[] screenSize = new float[] { screenWidth, screenHeight };
        shader.setUniform("u_screenSize", screenSize);	

		// mode = GL_TRIANGLES
		// starting offset = 0
		// number of elements = 6
		glDrawArrays(GL_TRIANGLES, 0, vertices.length / 2);

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
