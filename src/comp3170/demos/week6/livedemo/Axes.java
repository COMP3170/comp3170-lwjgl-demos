package comp3170.demos.week6.livedemo;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.GL_FLOAT_VEC2;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week6.shaders.ShaderLibrary;

public class Axes extends SceneObject {

	private Vector4f[] xAxis;
	private Vector4f[] yAxis;
	private Vector4f[] zAxis;
	private int xVertexBuffer;
	private int yVertexBuffer;
	private int zVertexBuffer;
	
	private float[] xColour = new float[] {1, 0, 0, 1}; // RED
	private float[] yColour = new float[] {0, 1, 0, 1}; // GREEN
	private float[] zColour = new float[] {0, 0, 1, 1}; // BLUE
	
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";
	private Shader shader;

	public Axes() {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		xAxis = new Vector4f[] {
			new Vector4f(0, 0, 0, 1),
			new Vector4f(1, 0, 0, 1),
		};

		yAxis = new Vector4f[] {
			new Vector4f(0, 0, 0, 1),
			new Vector4f(0, 1, 0, 1),
		};

		zAxis = new Vector4f[] {
				new Vector4f(0, 0, 0, 1),
				new Vector4f(0, 0, 1, 1),
		};

		xVertexBuffer = GLBuffers.createBuffer(xAxis);
		yVertexBuffer = GLBuffers.createBuffer(yAxis);
		zVertexBuffer = GLBuffers.createBuffer(zAxis);
	}
	
	
	
	@Override
	protected void drawSelf(Matrix4f matrix) {
		
		shader.enable();
		shader.setUniform("u_mvpMatrix", matrix);
		
		// X axis
		
	    shader.setAttribute("a_position", xVertexBuffer);
	    shader.setUniform("u_colour", xColour);	   	    
		glDrawArrays(GL_LINES, 0, xAxis.length);           	

		// Y axis
		
	    shader.setAttribute("a_position", yVertexBuffer);
	    shader.setUniform("u_colour", yColour);	   	    
		glDrawArrays(GL_LINES, 0, yAxis.length);           	

		// Z axis
		
	    shader.setAttribute("a_position", zVertexBuffer);
	    shader.setUniform("u_colour", zColour);	   	    
		glDrawArrays(GL_LINES, 0, zAxis.length);           	

	}

}
