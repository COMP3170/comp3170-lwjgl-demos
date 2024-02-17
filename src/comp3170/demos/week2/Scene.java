package comp3170.demos.week2;

import static comp3170.Math.TAU;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Scene {

	final private String VERTEX_SHADER = "colour_vertex.glsl";
	final private String FRAGMENT_SHADER = "colour_fragment.glsl";

	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector3f[] colours;
	private int colourBuffer;
	private int[] indices;
	private int indexBuffer;
	private Shader shader;


	public Scene() {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		// calculate the vertices of a hexagon as (x,y) pairs

		vertices = new Vector4f[7];

		// the centre

		int n = 0;
		vertices[n++] = new Vector4f(0,0,0,1);	// x

		// the outer ring

		float radius = 0.8f;

		for (int i = 1; i <= 6; i++) {
			double angle = i * TAU / 6;
			float x = (float) (radius * Math.cos(angle));	// x
			float y = (float) (radius * Math.sin(angle)); // y
			vertices[n++] = new Vector4f(x, y, 0 , 1);
		}

		// copy the data into a Vertex Buffer Object in graphics memory
	    vertexBuffer = GLBuffers.createBuffer(vertices);

	    // @formatter: off
		colours = new Vector3f[] {
			 new Vector3f(1.0f, 1.0f, 1.0f),  // WHITE
			 new Vector3f(1.0f, 0.0f, 0.0f),  // RED
			 new Vector3f(1.0f, 1.0f, 0.0f),  // YELLOW
			 new Vector3f(0.0f, 1.0f, 0.0f),  // GREEN
			 new Vector3f(0.0f, 1.0f, 1.0f),  // CYAN
			 new Vector3f(0.0f, 0.0f, 1.0f),  // BLUE
			 new Vector3f(1.0f, 0.0f, 1.0f),  // MAGENTA
		};
	    // @formatter: on

		// copy the data into a Vertex Buffer Object in graphics memory
	    colourBuffer = GLBuffers.createBuffer(colours);

	    // @formatter: off
	    indices = new int[] {
	    	0, 1, 2,
	    	0, 2, 3,
	    	0, 3, 4,
	    	0, 4, 5,
	    	0, 5, 6,
	    	0, 6, 1,
	    };
	    // @formatter: on

	    indexBuffer = GLBuffers.createIndexBuffer(indices);

	}


	public void draw() {
		// activate the shader
		shader.enable();

        // connect the vertex buffer to the a_position attribute
	    shader.setAttribute("a_position", vertexBuffer);
	    shader.setAttribute("a_colour", colourBuffer);

	    // write the colour value into the u_colour uniform
//	    float[] colour = {1.0f, 0.0f, 1.0f};
//	    shader.setUniform("u_colour", colour);

	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);

	    // draw triangles as wireframe or filled
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	    glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}
}
