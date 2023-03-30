package comp3170.demos.week7.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week7.shaders.ShaderLibrary;

public class Cube extends SceneObject {

	static final private String VERTEX_SHADER = "simpleVertex.glsl";
	static final private String FRAGMENT_SHADER = "simpleFragment.glsl";
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector4f colour;

	public Cube(Color colour) {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		//      4----5
		//     /|   /|    y
		//    / |  / |    |
		//   1----0  |    .--x
		//   |  | |  |   / 
		//   |  6-|--7  z
		//   | /  | /
		//   |/   |/
		//   3----2
		
		vertices = new Vector4f[] {
			new Vector4f( 1,  1,  1, 1),
			new Vector4f(-1,  1,  1, 1),
			new Vector4f( 1, -1,  1, 1),
			new Vector4f(-1, -1,  1, 1),
			
			new Vector4f(-1,  1, -1, 1),
			new Vector4f( 1,  1, -1, 1),
			new Vector4f(-1, -1, -1, 1),
			new Vector4f( 1, -1, -1, 1),
		};
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		
		indices = new int[] {
			// front
			0, 1, 2,
			3, 2, 1,
			
			// back
			4, 5, 6,
			7, 6, 5,
			
			// left
			1, 4, 3,
			6, 3, 4,
			
			// right
			0, 5, 2,
			7, 2, 5,
			
			// top
			0, 5, 1,
			4, 1, 5, 
			
			// bottom
			2, 3, 7,
			6, 7, 3,
		};
		
		indexBuffer = GLBuffers.createIndexBuffer(indices);
		
		// convert java Color into RGBA
		float[] rgb = colour.getComponents(new float[4]);
		this.colour = new Vector4f(rgb[0], rgb[1], rgb[2], 1);
		
	}
	
	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();

		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_colour", colour);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

	}
	

}
