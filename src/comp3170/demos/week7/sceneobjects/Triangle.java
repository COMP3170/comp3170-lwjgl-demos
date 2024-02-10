package comp3170.demos.week7.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Triangle extends SceneObject {

	static final private String VERTEX_SHADER = "simpleVertex.glsl";
	static final private String FRAGMENT_SHADER = "simpleFragment.glsl";
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector4f colour;

	public Triangle(Color colour) {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		vertices = new Vector4f[] {
			new Vector4f( 0, 1, 0, 1),
			new Vector4f( 1, 0, 0, 1),
			new Vector4f(-1, 0, 0, 1),
		};
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		
		// convert java COlor into RGBA
		float[] rgb = colour.getComponents(new float[4]);
		this.colour = new Vector4f(rgb[0], rgb[1], rgb[2], 1);
	}

	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		
		if (shader.hasUniform("u_colour")) {
			shader.setUniform("u_colour", colour);
		}

		glDrawArrays(GL_TRIANGLES, 0, vertices.length);

	}
}
