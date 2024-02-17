package comp3170.demos.week6.backface;

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

	final private String VERTEX_SHADER = "simpleVertex.glsl";
	final private String FRAGMENT_SHADER = "simpleFragment.glsl";

	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private float[] colour = {1f, 1f, 1f, 1f};
	
	public Triangle(Color colour) {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		// convert colour to RGB array of floats
		colour.getRGBColorComponents(this.colour);

		// @formatter:off

		//         0
		//       /   \
		//     /       \       y
		//   /           \     |   RH 
		//  1------*------2    +-x
		//                    /
		//                   z
				
		vertices = new Vector4f[] {
			new Vector4f(0, 1, 0, 1),
			new Vector4f(-1, 0, 0, 1),
			new Vector4f( 1, 0, 0, 1),
		};
			
		vertexBuffer = GLBuffers.createBuffer(vertices);
		
		// @formatter:on
	}

	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {

		shader.enable();
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_colour", colour);

		// Draw a solid triangle
		glDrawArrays(GL_TRIANGLES, 0, vertices.length);           	
	}

	
}
