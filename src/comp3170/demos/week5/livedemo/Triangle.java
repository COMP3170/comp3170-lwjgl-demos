package comp3170.demos.week5.livedemo;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week5.shaders.ShaderLibrary;

public class Triangle extends SceneObject {

	private static final String VERTEX_SHADER = "simpleVertex.glsl";
	private static final String FRAGMENT_SHADER = "simpleFragment.glsl";
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector4f colour; // rgba

	public Triangle(Vector4f colour) {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		this.colour = colour;

		// @formatter:off
		
		//           1 (0,1)
		//          /|
		//         / |         y
		//        /  |         ^ z
		//       /   |         |/
		//      2----0 (0,0)   +--> x
		//  (-1,0)          
		//                   
		
		vertices = new Vector4f[] {
			new Vector4f( 0, 0, 0, 1),
			new Vector4f( 0, 1, 0, 1),
			new Vector4f(-1, 0, 0, 1),
		};

		// @formatter:on

		vertexBuffer = GLBuffers.createBuffer(vertices);
	}

	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		
		shader.enable();
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_colour", colour);
		
		glDrawArrays(GL_TRIANGLES, 0, vertices.length);
	}

	
	
}
