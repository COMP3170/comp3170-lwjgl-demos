package comp3170.demos.week5.mesh.sceneobjects;

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

import static comp3170.demos.week5.mesh.MeshDemo.TAU;

public class UVSphere extends SceneObject {

	private static final String VERTEX_SHADER = "simpleVertex.glsl";
	private static final String FRAGMENT_SHADER = "simpleFragment.glsl";
	private static final int NSEGMENTS = 20;
	
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private float[] colour = {1f, 1f, 1f, 1f};

	public UVSphere() {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		int width = 2 * NSEGMENTS;
		int height = NSEGMENTS;
		
		createVertexBuffer(width, height);
	    createIndexBuffer(width, height);

	}

	private void createVertexBuffer(int width, int height) {
		//
		// Create a (2n+1) * (n+1) grid of points in polar space
		//

		vertices = new Vector4f[(width +1) * (height+1)];
		
		Matrix4f rotateY = new Matrix4f();
		Matrix4f rotateX = new Matrix4f();
		Matrix4f translateY = new Matrix4f();

		int k = 0;
		for (int i = 0; i <= width; i++) {
			float heading = i * TAU / width;	// from 0 to TAU
			rotateY.rotationY(heading);
			
			for (int j = 0; j <= height; j++) {
				float t = j * 2f / height - 1;
				float pitch = (TAU /4) * t; // from -TAU/4 to TAU/4
				rotateX.rotationX(pitch);
				translateY.translation(0, t, 0);
				
				vertices[k] = new Vector4f(0, 0, 1, 1);	// unit vector in z direction
				vertices[k].mul(rotateX).mul(rotateY);  // vk = Ry * Rx * v;
				k++;
			}			
		}
		
	    vertexBuffer = GLBuffers.createBuffer(vertices);
	}

	
	private void createIndexBuffer(int width, int height) {
		// Each quad looks like
	    //
	    // k+1 +--+ k + h + 2
	    //     |\ |
	    //     | \|
	    //   k +--+ k + h + 1
	    
	    indices = new int[12 * width * height]; // 2 tris * 3  verts * width height

	    int n = 0;
		for (int i = 0; i < width; i++) {		// there is no quad for the right-hand column
			for (int j = 0; j < height; j++) { 	// or the top row
				int k = i * (height+1) + j;
				
				indices[n++] = k;
				indices[n++] = k + height + 1;
				indices[n++] = k + 1;

				indices[n++] = k + height + 2;
				indices[n++] = k + 1;
				indices[n++] = k + height + 1;
			}			
		}
	    
	    indexBuffer = GLBuffers.createIndexBuffer(indices);
	}

	
	@Override
	protected void drawSelf(Matrix4f modelMatrix) {
		shader.enable();
		shader.setUniform("u_mvpMatrix", modelMatrix);
	    shader.setAttribute("a_position", vertexBuffer);
	    shader.setUniform("u_colour", colour);	    
	    
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}

}
