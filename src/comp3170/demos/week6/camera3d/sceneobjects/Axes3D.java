package comp3170.demos.week6.camera3d.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week6.shaders.ShaderLibrary;

public class Axes3D extends SceneObject {

	private Vector4f[] vertices;
	private int vertexBuffer;
	private int indexBufferX;
	private int indexBufferY;
	private int indexBufferZ;
	
	private float[] xColour = new float[] {1, 0, 0, 1}; // RED
	private float[] yColour = new float[] {0, 1, 0, 1}; // GREEN
	private float[] zColour = new float[] {0, 0, 1, 1}; // BLUE
	
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";
	private Shader shader;

	public Axes3D() {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		//A set of i,j,k axes
		
		vertices = new Vector4f[] {
				new Vector4f(0,0,0,1),	
				new Vector4f(1,0,0,1),	//i axis
				new Vector4f(0,1,0,1),	//j axis
				new Vector4f(0,0,1,1),	//k axis
		};

		vertexBuffer = GLBuffers.createBuffer(vertices);
		
		indexBufferX = GLBuffers.createIndexBuffer(new int[] {0,1});
		indexBufferY = GLBuffers.createIndexBuffer(new int[] {0,2});
		indexBufferZ = GLBuffers.createIndexBuffer(new int[] {0,3});
		
	}
	
	
	
	@Override
	protected void drawSelf(Matrix4f matrix) {
		
		shader.enable();
		shader.setUniform("u_mvpMatrix", matrix);
	    shader.setAttribute("a_position", vertexBuffer);
		
		// X axis
	    
	    shader.setUniform("u_colour", xColour);	 
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferX);   
	    glDrawElements(GL_LINES, 2, GL_UNSIGNED_INT, 0);

		// Y axis
		
	    shader.setUniform("u_colour", yColour);	   	    
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferY);       
	    glDrawElements(GL_LINES, 2, GL_UNSIGNED_INT, 0);
		
		// Z axis
		
		shader.setUniform("u_colour", zColour);
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferZ);       
	    glDrawElements(GL_LINES, 2, GL_UNSIGNED_INT, 0);

	}

}
