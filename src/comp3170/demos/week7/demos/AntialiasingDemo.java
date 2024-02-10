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

import comp3170.IWindowListener;
import comp3170.OpenGLException;
import comp3170.ShaderLibrary;
import comp3170.Window;

public class AntialiasingDemo implements IWindowListener {

	private static final File COMMON_DIR = new File("src/comp3170/demos/common/shaders"); 

	private Window window;
	private int screenWidth = 800;
	private int screenHeight = 800;
	private Vector4f[] vertices;
	private int vertexBuffer;

	private AntialiasingScene scene;

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

		scene = new AntialiasingScene();
		

	}

	private static final Matrix4f mvpMatrix = new Matrix4f();
	
	@Override
	public void draw() {
		glClear(GL_COLOR_BUFFER_BIT);		

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
		new AntialiasingDemo();
	}
}
