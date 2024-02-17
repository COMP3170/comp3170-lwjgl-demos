package comp3170.demos.week11.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class TexturedCube extends SceneObject {

	private static final String VERTEX_SHADER = "textureVertex.glsl";
	private static final String FRAGMENT_SHADER = "textureFragment.glsl";
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector2f[] uvs;
	private int uvBuffer;
	private int texture;

	public TexturedCube(int texture) {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		this.texture = texture;

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

		vertices = new Vector4f[] {
			// front
			new Vector4f( 1, 1, 1, 1), // 0
			new Vector4f(-1, 1, 1, 1), // 1
			new Vector4f(-1,-1, 1, 1), // 2

			new Vector4f(-1,-1, 1, 1), // 2
			new Vector4f( 1,-1, 1, 1), // 3
			new Vector4f( 1, 1, 1, 1), // 0

			// back
			new Vector4f( 1,-1,-1, 1), // 4
			new Vector4f(-1,-1,-1, 1), // 5
			new Vector4f(-1, 1,-1, 1), // 6

			new Vector4f(-1, 1,-1, 1), // 6
			new Vector4f( 1, 1,-1, 1), // 7
			new Vector4f( 1,-1,-1, 1), // 4

			// top
			new Vector4f( 1, 1, 1, 1), // 0
			new Vector4f( 1, 1,-1, 1), // 7
			new Vector4f(-1, 1,-1, 1), // 6

			new Vector4f(-1, 1,-1, 1), // 6
			new Vector4f(-1, 1, 1, 1), // 1
			new Vector4f( 1, 1, 1, 1), // 0

			// bottom
			new Vector4f(-1,-1, 1, 1), // 2
			new Vector4f(-1,-1,-1, 1), // 5
			new Vector4f( 1,-1,-1, 1), // 4

			new Vector4f( 1,-1,-1, 1), // 4
			new Vector4f( 1,-1, 1, 1), // 3
			new Vector4f(-1,-1, 1, 1), // 2

			// left
			new Vector4f(-1, 1, 1, 1), // 1
			new Vector4f(-1, 1,-1, 1), // 6
			new Vector4f(-1,-1,-1, 1), // 5

			new Vector4f(-1,-1,-1, 1), // 5
			new Vector4f(-1,-1, 1, 1), // 2
			new Vector4f(-1, 1, 1, 1), // 1

			// right
			new Vector4f( 1, 1,-1, 1), // 7
			new Vector4f( 1, 1, 1, 1), // 0
			new Vector4f( 1,-1, 1, 1), // 3

			new Vector4f( 1,-1, 1, 1), // 3
			new Vector4f( 1,-1,-1, 1), // 4
			new Vector4f( 1, 1,-1, 1), // 7
		};

		vertexBuffer = GLBuffers.createBuffer(vertices);

		uvs = new Vector2f[] {
			// front
			new Vector2f(1, 1),
			new Vector2f(0, 1),
			new Vector2f(0, 0),

			new Vector2f(0, 0),
			new Vector2f(1, 0),
			new Vector2f(1, 1),

			// back
			new Vector2f(1, 1),
			new Vector2f(0, 1),
			new Vector2f(0, 0),

			new Vector2f(0, 0),
			new Vector2f(1, 0),
			new Vector2f(1, 1),

			// top
			new Vector2f(1, 1),
			new Vector2f(0, 1),
			new Vector2f(0, 0),

			new Vector2f(0, 0),
			new Vector2f(1, 0),
			new Vector2f(1, 1),

			// bottom
			new Vector2f(1, 1),
			new Vector2f(0, 1),
			new Vector2f(0, 0),

			new Vector2f(0, 0),
			new Vector2f(1, 0),
			new Vector2f(1, 1),

			// left
			new Vector2f(1, 1),
			new Vector2f(0, 1),
			new Vector2f(0, 0),

			new Vector2f(0, 0),
			new Vector2f(1, 0),
			new Vector2f(1, 1),

			// right
			new Vector2f(1, 1),
			new Vector2f(0, 1),
			new Vector2f(0, 0),

			new Vector2f(0, 0),
			new Vector2f(1, 0),
			new Vector2f(1, 1),

		};
		uvBuffer = GLBuffers.createBuffer(uvs);

		// @formatter:on

	}

	@Override
	protected void drawSelf(Matrix4f modelMatrix) {
		shader.enable();

		// matrices
		shader.setUniform("u_mvpMatrix", modelMatrix);

		// vertex attributes
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_texcoord", uvBuffer);

		// textures
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture);
		shader.setUniform("u_texture", 0);

		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDrawArrays(GL_TRIANGLES, 0, vertices.length);
	}
}
