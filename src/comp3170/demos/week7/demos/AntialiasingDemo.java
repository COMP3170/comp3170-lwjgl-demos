package comp3170.demos.week7.demos;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;

import java.io.File;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.IWindowListener;
import comp3170.OpenGLException;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.Window;

public class AntialiasingDemo implements IWindowListener {

	private static final File COMMON_DIR = new File("src/comp3170/demos/common/shaders");

	private Window window;
	private int screenWidth = 800;
	private int screenHeight = 800;

	private static final String VERTEX_SHADER = "simpleVertex.glsl";
	private static final String FRAGMENT_SHADER = "simpleFragment.glsl";
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private static final Vector4f colour = new Vector4f(1,0,0,1);

	public AntialiasingDemo() throws OpenGLException {
		window = new Window("Antialiasing demo", screenWidth, screenHeight, this);
		window.setSamples(4);	// set the number of samples or 0 to disable
		window.run();
	}


	@Override
	public void init() {
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glEnable(GL_MULTISAMPLE);

		new ShaderLibrary(COMMON_DIR);

		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		// draw a triangle

		vertices = new Vector4f[] {
			new Vector4f(   0,   0.8f, 0, 1),
			new Vector4f(-0.4f, -0.8f, 0, 1),
			new Vector4f( 0.5f, -0.7f, 0, 1),
		};

		vertexBuffer = GLBuffers.createBuffer(vertices);

	}

	private static final Matrix4f mvpMatrix = new Matrix4f();

	@Override
	public void draw() {
		glClear(GL_COLOR_BUFFER_BIT);

		shader.enable();
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_colour", colour);

		glDrawArrays(GL_TRIANGLES, 0, vertices.length);
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
		new AntialiasingDemo();
	}
}
