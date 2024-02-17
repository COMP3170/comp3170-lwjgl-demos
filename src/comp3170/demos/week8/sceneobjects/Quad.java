package comp3170.demos.week8.sceneobjects;

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
import comp3170.ShaderLibrary;

public class Quad extends SceneObject {

	static final private String VERTEX_SHADER = "simpleVertex.glsl";
	static final private String FRAGMENT_SHADER = "simpleFragment.glsl";
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector4f colour;
	private int[] indices;
	private int indexBuffer;

	public Quad(Color colour) {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		// @formatter:off

		vertices = new Vector4f[] {
			new Vector4f( 1, 1, 0, 1),
			new Vector4f(-1, 1, 0, 1),
			new Vector4f( 1,-1, 0, 1),
			new Vector4f(-1,-1, 0, 1),
		};

		vertexBuffer = GLBuffers.createBuffer(vertices);

		indices = new int[] {
			0, 1, 2,
			3, 2, 1,
		};

		indexBuffer = GLBuffers.createIndexBuffer(indices);

		// @formatter:on

		// convert java COlor into RGBA
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
