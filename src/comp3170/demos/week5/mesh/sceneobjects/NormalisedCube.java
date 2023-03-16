package comp3170.demos.week5.mesh.sceneobjects;

import static comp3170.demos.week5.mesh.MeshDemo.TAU;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week5.shaders.ShaderLibrary;

public class NormalisedCube extends SceneObject {

	private static final String VERTEX_SHADER = "colourVertex.glsl";
	private static final String FRAGMENT_SHADER = "colourFragment.glsl";
	private static final int NSEGMENTS = 6;

	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector4f[] colours;
	private int colourBuffer;
	private int[] indices;
	private int indexBuffer;
	private boolean isNormalised;

	public NormalisedCube(boolean normalise) {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		isNormalised = normalise;
		//		
		// Create a cube with each face divided into an n x n grid
		//
		
		Vector4f[] grid = createGrid();
		createVertexBuffer(grid);
		createIndexBuffer();
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


		Vector4f[] sideColour = new Vector4f[] { 
			new Vector4f(0,0,1,1), // front
			new Vector4f(0,0,1,1), // back
			new Vector4f(1,0,0,1), // right
			new Vector4f(1,0,0,1), // left
			new Vector4f(0,1,0,1), // bottom
			new Vector4f(0,1,0,1), // top
		};

		int n = sides.length * grid.length;
		vertices = new Vector4f[n];
		colours = new Vector4f[n];
		
		// scale of 1/sqrt(3) means the corner points lie on the unit cube
		Matrix4f scale = new Matrix4f().scaling(1.0f / (float) Math.sqrt(3));

		int k = 0;
		for (int s = 0; s < sides.length; s++) {
			for (int i = 0; i < grid.length; i++) {
				// rotate and scale each point
				vertices[k] = new Vector4f(grid[i]).mul(sides[s]).mul(scale);					
				if (isNormalised) {
					vertices[k].normalize3(); // vk = vk / |vk.xyz]
					vertices[k].w = 1;	// correct w
				}
				colours[k] = sideColour[s];
				k++;
			}
		}
		vertexBuffer = GLBuffers.createBuffer(vertices);
		colourBuffer = GLBuffers.createBuffer(colours);

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
	
	@Override
	protected void drawSelf(Matrix4f modelMatrix) {
		shader.enable();
		shader.setUniform("u_mvpMatrix", modelMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_colour", colourBuffer);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}

}
