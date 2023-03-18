package comp3170.demos.week6.backface;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week6.shaders.ShaderLibrary;

public class Triangle extends SceneObject {

	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private float[] colour = {1f, 1f, 1f, 1f};
	
	public Triangle(Shader shader, Color colour) {
		this.shader = shader;

		// convert colour to RGB array of floats
		colour.getRGBColorComponents(this.colour);

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
