package comp3170.demos.week3.camera2d;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.GL_FLOAT_VEC2;

import comp3170.GLBuffers;
import comp3170.Shader;

public class House {

	private float[] vertices;
	private int vertexBuffer;
	
	private float[] colour = new float[] {1, 1, 0}; // YELLOW
	

	public House() {
		vertices = new float[] {
			// sides
			-0.375f, 0,
			-0.375f, 0.5f,
			 0.375f, 0,
			 0.375f, 0.5f,
			-0.375f, 0f,
			 0.375f, 0f,			
			// roof
			-0.5f,0.5f,
			 0.5f,0.5f,
			-0.5f,0.5f,
			 0,1,
			 0.5f,0.5f,
			 0,1,			
		};

		
		vertexBuffer = GLBuffers.createBuffer(vertices, GL_FLOAT_VEC2);
	}
	
	public void draw(Shader shader) {
	
	    shader.setAttribute("a_position", vertexBuffer);
	    shader.setUniform("u_colour", colour);	   	    
		glDrawArrays(GL_LINES, 0, vertices.length / 2);           	

	}
}
