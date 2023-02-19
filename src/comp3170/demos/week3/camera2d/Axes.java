package comp3170.demos.week3.camera2d;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.GL_FLOAT_VEC2;

import comp3170.GLBuffers;
import comp3170.Shader;

public class Axes {

	private float[] xAxis;
	private float[] yAxis;
	private int xVertexBuffer;
	private int yVertexBuffer;
	
	private float[] xColour = new float[] {1, 0, 0}; // RED
	private float[] yColour = new float[] {0, 1, 0}; // GREEN
	

	public Axes() {
		xAxis = new float[] {
			0, 0,
			1, 0,
		};

		yAxis = new float[] {
			0, 0,
			0, 1,
		};
		
		xVertexBuffer = GLBuffers.createBuffer(xAxis, GL_FLOAT_VEC2);
		yVertexBuffer = GLBuffers.createBuffer(yAxis, GL_FLOAT_VEC2);
	}
	
	public void draw(Shader shader) {
	
		// X axis
		
	    shader.setAttribute("a_position", xVertexBuffer);
	    shader.setUniform("u_colour", xColour);	   	    
		glDrawArrays(GL_LINES, 0, xAxis.length / 2);           	

		// Y axis
		
	    shader.setAttribute("a_position", yVertexBuffer);
	    shader.setUniform("u_colour", yColour);	   	    
		glDrawArrays(GL_LINES, 0, yAxis.length / 2);           	

	}
}
