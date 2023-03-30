package comp3170.demos.week7.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week7.shaders.ShaderLibrary;

public class BarycentricTriangle extends SceneObject {

	private static final String VERTEX_SHADER = "barycentricVertex.glsl";
	private static final String FRAGMENT_SHADER = "barycentricFragment.glsl";
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector3f[] barcentricCoords;
	private int barycentricBuffer;

	public BarycentricTriangle() {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		// draw a triangle
		
		vertices = new Vector4f[] {
			new Vector4f(   0,   0.8f, 0, 1),
			new Vector4f(-0.4f, -0.8f, 0, 1),
			new Vector4f( 0.5f, -0.7f, 0, 1),
		};

		vertexBuffer = GLBuffers.createBuffer(vertices);

		barcentricCoords = new Vector3f[] {
			new Vector3f(1,0,0),
			new Vector3f(0,1,0),
			new Vector3f(0,0,1),
		};
		
		barycentricBuffer = GLBuffers.createBuffer(barcentricCoords);

		
	}
	
	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_barycentric", barycentricBuffer);
		
		glDrawArrays(GL_TRIANGLES, 0, vertices.length);

	}

	
}
