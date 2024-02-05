
package comp3170.demos.week2;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.GL_FLOAT_VEC2;
import static org.lwjgl.opengl.GL20.GL_FLOAT_VEC3;

import java.io.File;
import java.io.IOException;

import comp3170.GLBuffers;
import comp3170.IWindowListener;
import comp3170.OpenGLException;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.Window;

public class Week2 implements IWindowListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto
	
	private int width = 800;
	private int height = 800;

	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/demos/week2");

	private Scene scene; 

	public Week2() throws OpenGLException {
		Window window = new Window("Week 2 example", width, height, this);
		window.run();
	}
	

	@Override
	public void init() {
		new ShaderLibrary(DIRECTORY);
		scene = new Scene();
	}


	@Override
	public void draw() {
		// set the background colour to white
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        // clear the colour buffer
		glClear(GL_COLOR_BUFFER_BIT);		

		scene.draw();
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;		
	}

	@Override
	public void close() {
		
	}
	
	public static void main(String[] args) throws OpenGLException { 
		new Week2();
	}


}
