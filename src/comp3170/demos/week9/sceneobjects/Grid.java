package comp3170.demos.week9.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week7.shaders.ShaderLibrary;

public class Grid extends SceneObject {

	static final private String VERTEX_SHADER = "simpleVertex.glsl";
	static final private String FRAGMENT_SHADER = "simpleFragment.glsl";
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector4f colour = new Vector4f(1,1,1,1); // white

	
	public Grid(int nLines) {
		
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		vertices = new Vector4f[nLines * 4];
		
		int k = 0;
		
		for (int i = 0; i < nLines; i++) {
			float x = (2f * i / (nLines - 1)) - 1;
			
			vertices[k++] = new Vector4f( x, 0, -1, 1);
			vertices[k++] = new Vector4f( x, 0,  1, 1);
			vertices[k++] = new Vector4f(-1, 0,  x, 1);
			vertices[k++] = new Vector4f( 1, 0,  x, 1);
		}
		
		vertexBuffer = GLBuffers.createBuffer(vertices);		
	}


	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_colour", colour);
		
		glDrawArrays(GL_LINES, 0, vertices.length);
	}
	
	
}
