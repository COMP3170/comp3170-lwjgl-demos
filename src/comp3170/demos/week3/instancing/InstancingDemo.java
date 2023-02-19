package comp3170.demos.week3.instancing;

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

public class InstancingDemo implements IWindowListener {


	final private int NSQUARES = 100;
	
	private Window window;

	private int screenWidth = 800;
	private int screenHeight = 800;
	private Squares squares;
	
	private long oldTime;
	
	public InstancingDemo() throws OpenGLException {
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
				
	    squares = new Squares(NSQUARES);
	   	    	    
	    // initialise oldTime
	    oldTime = System.currentTimeMillis();
	}


	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;
		System.out.println("update: dt = " + deltaTime + "s");
				
		squares.update(deltaTime);
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

		// draw all the sqaures
		squares.draw();
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
		new InstancingDemo();
	}

}
