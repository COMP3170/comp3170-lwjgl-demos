package comp3170.demos.week6.camera3d.sceneobjects;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.demos.SceneObject;

public class Cube extends SceneObject {
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	
	private float[] colour = {1f, 1f, 1f}; 
	

	public Cube(Shader shader, Color colour) {
		this.shader = shader;

		// convert colour to RGB array of floats
		colour.getRGBColorComponents(this.colour);
		
		//    6-----7
		//   /|    /|
		//  / |   / |
		// 1-----0  | y RH coords
		// |  |  |  | |
		// |  5--|--4 +--x
		// | /   | / /
		// |/    |/ z
		// 2-----3

		vertices = new Vector4f[] { 
			new Vector4f(1, 1, 1, 1), 
			new Vector4f(-1, 1, 1, 1), 
			new Vector4f(-1, -1, 1, 1),
			new Vector4f(1, -1, 1, 1), 
			new Vector4f(1, -1, -1, 1), 
			new Vector4f(-1, -1, -1, 1),
			new Vector4f(-1, 1, -1, 1), 
			new Vector4f(1, 1, -1, 1), 
		};

		vertexBuffer = GLBuffers.createBuffer(vertices);

		// indices for the lines forming each face

		indices = new int[] {
			// front
			0, 1, 2, 
			2, 3, 0, 

			// back
			4, 5, 6, 
			6, 7, 4, 

			// top
			0, 7, 6, 
			6, 1, 0,

			// bottom
			2, 5, 4, 
			4, 3, 2,

			// left 
			1, 6, 5,
			5, 2, 1,
			
			// right
			0, 3, 4,
			4, 7, 0,

		};

		indexBuffer = GLBuffers.createIndexBuffer(indices);

		// scale down to fit in window
		getMatrix().scale((float) (1.0f / Math.sqrt(3)));

	}

	@Override
	public void drawSelf(Matrix4f mvpMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.enable();
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_colour", colour);

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);

		// DEBUG: Just draw the vertices
//		gl.glDrawElements(GL.GL_POINTS, indices.length, GL.GL_UNSIGNED_INT, 0);

		// Draw the wireframe
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL4.GL_LINE);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
	}

}
