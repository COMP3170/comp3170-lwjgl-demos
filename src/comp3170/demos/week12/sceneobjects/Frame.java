package comp3170.demos.week12.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week12.shaders.ShaderLibrary;

public class Frame extends SceneObject {
	
	static final private String VERTEX_SHADER = "simpleVertex.glsl";
	static final private String FRAGMENT_SHADER = "simpleFragment.glsl";
	static final private float WIDTH = 0.1f;
	
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;

	private Vector4f colour = new Vector4f(1,1,0,1);
	
	public Frame() {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		//           2
		//          /|
	    //   +-----0 | 
		//   |     | |
		//   |  *  | |
		//   |     | |
		//   +-----1 |
		//          \|
		//           3
	
		vertices = new Vector4f[] {
			new Vector4f( 1, 1,0,1),
			new Vector4f( 1,-1,0,1),
			new Vector4f( 1+WIDTH, 1+WIDTH,0,1),
			new Vector4f( 1+WIDTH,-1-WIDTH,0,1),
		};
		vertexBuffer = GLBuffers.createBuffer(vertices);
		
		indices = new int[] {
			0,1,2,
			3,2,1,
		};
		indexBuffer = GLBuffers.createIndexBuffer(indices);
		
	}
	
	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_colour", colour);
		shader.setAttribute("a_position", vertexBuffer);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}
}
