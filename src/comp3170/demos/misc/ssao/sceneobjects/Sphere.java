package comp3170.demos.misc.ssao.sceneobjects;

import static comp3170.Math.TAU;
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
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.demos.common.cameras.Camera;
import comp3170.demos.misc.ssao.Scene;

public class Sphere extends SceneObject {

	private static final String VERTEX_SHADER = "simpleVertex.glsl";
	private static final String FRAGMENT_SHADER = "simpleFragment.glsl";
	private static final int NSEGMENTS = 10;
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector4f[] normals;
	private int normalBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector3f colour = new Vector3f();

	public Sphere(Color c) {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		shader.setStrict(false);
		
		Vector4f[] grid = createGrid();
		createVertexBuffer(grid);
		createIndexBuffer();
		colour.set(c.getColorComponents(null));
	}

	private Vector4f[] createGrid() {
		//
		// 1. create the vertices on the grid for one face
		//

		Vector4f[] grid = new Vector4f[(NSEGMENTS + 1) * (NSEGMENTS + 1)];

		int k = 0;
		for (int i = 0; i <= NSEGMENTS; i++) {
			float x = (float) 2 * i / NSEGMENTS - 1; // from -1 to 1

			for (int j = 0; j <= NSEGMENTS; j++) {
				float y = (float) 2 * j / NSEGMENTS - 1; // from -1 to 1

				grid[k++] = new Vector4f(x, y, 1, 1);

			}
		}

		return grid;
	}
	
	private void createVertexBuffer(Vector4f[] grid) {
		//
		// 2. Rotate copies of the grid point to form the six faces
		//

		Matrix4f[] sides = new Matrix4f[] { 
			new Matrix4f(), // front
			new Matrix4f().rotateY(TAU / 2), // back
			new Matrix4f().rotateY(TAU / 4), // right
			new Matrix4f().rotateY(-TAU / 4), // left
			new Matrix4f().rotateX(TAU / 4), // bottom
			new Matrix4f().rotateX(-TAU / 4), // top
		};

		int n = sides.length * grid.length;
		vertices = new Vector4f[n];
		normals = new Vector4f[n];
		
		int k = 0;
		for (int s = 0; s < sides.length; s++) {
			for (int i = 0; i < grid.length; i++) {
				// rotate and scale each point
				vertices[k] = new Vector4f(grid[i]).mul(sides[s]);					
				vertices[k].normalize3(); // vk = vk / |vk.xyz]
				vertices[k].w = 1;	// correct w
				
				// sphere normals are the same as vertices
				normals[k] = new Vector4f(vertices[k]);
				normals[k].w = 0;
				
				k++;
			}
		}
		vertexBuffer = GLBuffers.createBuffer(vertices);
		normalBuffer = GLBuffers.createBuffer(normals);
	}
	
	private void createIndexBuffer() {
		//
		// 3. create the index buffer for each face
		//

		// Each quad looks like
		//
		// k+1 +--+ k + n + 2
		//     |\ |
		//     | \|
		//   k +--+ k + n + 1

		indices = new int[6 * vertices.length]; // 2 tris * 3 verts * width * height

		int ns = NSEGMENTS + 1;
		
		int n = 0;
		for (int s = 0; s < 6; s++) {
			for (int i = 0; i < NSEGMENTS; i++) { // note there is no quad for the last row
				for (int j = 0; j < NSEGMENTS; j++) { // ... or column
					int k = s * ns * ns + i * ns + j;

					indices[n++] = k;
					indices[n++] = k + NSEGMENTS + 1;
					indices[n++] = k + 1;

					indices[n++] = k + NSEGMENTS + 2;
					indices[n++] = k + 1;
					indices[n++] = k + NSEGMENTS + 1;
				}
			}
		}

		indexBuffer = GLBuffers.createIndexBuffer(indices);

	}
	
	private Matrix4f modelMatrix = new Matrix4f();
	private Matrix4f normalMatrix = new Matrix4f();
	private Vector4f cameraPosition = new Vector4f();
	
	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		
		// matrices
		getModelToWorldMatrix(modelMatrix);
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_modelMatrix", modelMatrix);
		shader.setUniform("u_normalMatrix", modelMatrix.normal(normalMatrix));
		
		// vertex attributes
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_normal", normalBuffer);

		// material
		shader.setUniform("u_colour", colour);		
		
		// camera
		Camera camera = Scene.theScene.getCamera();
		shader.setUniform("u_cameraPosition", camera.getViewVector(cameraPosition));
		
		// draw
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}

}
