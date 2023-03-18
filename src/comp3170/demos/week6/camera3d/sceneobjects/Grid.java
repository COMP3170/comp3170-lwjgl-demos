package comp3170.demos.week6.camera3d.sceneobjects;

import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;

public class Grid extends SceneObject {
	
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector4f colour = new Vector4f(0.4f, 0.4f, 0.4f, 1f);

	public Grid(Shader shader, int nLines) {
		this.shader = shader;
		// Create a 2x2 square grid with the origin in the centre		
		
		vertices = new Vector4f[4 * nLines];
		indices = new int[4 * nLines];
			
		for (int i = 0; i < nLines; i++) {
			float x = 2.0f * i / (nLines-1) - 1; // -1 to 1 inclusive
			
			vertices[4*i]   = new Vector4f(-1, 0, x, 1);
			vertices[4*i+1] = new Vector4f( 1, 0, x, 1);
			indices[4*i] = 4*i;
			indices[4*i+1] = 4*i+1;
			
			vertices[4*i+2] = new Vector4f(x, 0, -1, 1);
			vertices[4*i+3] = new Vector4f(x, 0,  1, 1);
			indices[4*i+2] = 4*i+2;
			indices[4*i+3] = 4*i+3;
		}
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		indexBuffer = GLBuffers.createIndexBuffer(indices);		
	}
	

	@Override
	protected void drawSelf(Matrix4f matrix) {
		shader.enable();
		shader.setUniform("u_mvpMatrix", matrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_colour", colour);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glDrawElements(GL_LINES, indices.length, GL_UNSIGNED_INT, 0);		
	}

}
