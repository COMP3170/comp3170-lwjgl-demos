package comp3170.demos.week12.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.demos.week12.cameras.Camera;

public class ViewVolume extends SceneObject {

	private static final String VERTEX_SHADER = "simpleVertex.glsl";
	private static final String FRAGMENT_SHADER = "simpleFragment.glsl";
	private Shader shader;
	private Vector4f[] vertices;
	private Vector4f[] verticesNDC;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector4f colour = new Vector4f(1,1,1,1);
	private Matrix4f modelMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Camera camera;

	public ViewVolume(Camera camera) {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		this.camera = camera;

		modelMatrix = getMatrix();
		camera.getCameraMatrix(modelMatrix);
		camera.getProjectionMatrix(projectionMatrix);
		projectionMatrix.invert();

		// @formatter:off

		//          6-----7
		//         /|    /|
		//        / |   / |
		//       1-----0  |     y    RHS coords
		//       |  |  |  |     |
		//       |  5--|--4     +--x
		//       | /   | /     /
		//       |/    |/     z
		//       2-----3

		verticesNDC = new Vector4f[] {
			new Vector4f( 1, 1, 1, 1),
			new Vector4f(-1, 1, 1, 1),
			new Vector4f(-1,-1, 1, 1),
			new Vector4f( 1,-1, 1, 1),
			new Vector4f( 1,-1,-1, 1),
			new Vector4f(-1,-1,-1, 1),
			new Vector4f(-1, 1,-1, 1),
			new Vector4f( 1, 1,-1, 1),
		};

		vertices = new Vector4f[verticesNDC.length];
		for (int i = 0; i <vertices.length; i++) {
			vertices[i] = verticesNDC[i].mul(projectionMatrix, new Vector4f());
			vertices[i].mul(1/vertices[i].w);
		}

		vertexBuffer = GLBuffers.createBuffer(vertices);

		// indices for the lines forming each face

		indices = new int[] {
			// far
			0, 1,
			1, 2,
			2, 3,
			3, 0,

			// near
			4, 5,
			5, 6,
			6, 7,
			7, 4,

			// sides
			0, 7,
			1, 6,
			2, 5,
			3, 4,

		};

		indexBuffer = GLBuffers.createIndexBuffer(indices);

		// @formatter:on

	}

	public void update(float deltaTime, InputManager input) {

		modelMatrix = getMatrix();
		camera.getCameraMatrix(modelMatrix);
		camera.getProjectionMatrix(projectionMatrix);
		projectionMatrix.invert();

		for (int i = 0; i < vertices.length; i++) {
			verticesNDC[i].mul(projectionMatrix, vertices[i]);
			vertices[i].mul(1/vertices[i].w);
		}
		GLBuffers.updateBuffer(vertexBuffer, vertices);

	}

	@Override
	protected void drawSelf(Matrix4f modelMatrix) {
		shader.enable();
		shader.setUniform("u_mvpMatrix", modelMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_colour", colour);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glDrawElements(GL_LINES, indices.length, GL_UNSIGNED_INT, 0);
	}


}
