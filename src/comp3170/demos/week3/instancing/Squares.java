
package comp3170.demos.week3.instancing;

import static comp3170.Math.TAU;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.GL_FLOAT_VEC2;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

import java.awt.Color;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Squares {

	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;

	private int nSquares;
	private Vector2f[] position;
	private float[] angle;
	private float[] scale;
	private Vector3f[] colour;
	private Shader shader;

	private int positionBuffer;
	private int rotationBuffer;
	private int scaleBuffer;
	private int colourBuffer;

	public Squares(int nSquares) {
		this.nSquares = nSquares;
		
		// Compile the shader
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		// Make one copy of the mesh
		makeMesh();

		// One transform per instance
		position = new Vector2f[nSquares];
		angle = new float[nSquares];
		scale = new float[nSquares];
		colour = new Vector3f[nSquares];

		for (int i = 0; i < nSquares; i++) {
			float x = (float) Math.random() * 2 - 1;
			float y = (float) Math.random() * 2 - 1;
			position[i] = new Vector2f(x, y);
			angle[i] = 0;
			scale[i] = 0.1f;
			Color c = Color.getHSBColor((float) Math.random(), 1, 1);
			colour[i] = new Vector3f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f);
		}
		
		
		// create buffers for all the matrices and colours
		positionBuffer = GLBuffers.createBuffer(position);
		rotationBuffer = GLBuffers.createBuffer(angle, GL_FLOAT);
		scaleBuffer = GLBuffers.createBuffer(scale, GL_FLOAT);
		colourBuffer = GLBuffers.createBuffer(colour);

	}

	private void makeMesh() {

		// @formatter:off

		vertices = new Vector4f[] { 
			new Vector4f(-0.5f, -0.5f, 0, 1),     
			new Vector4f( 0.5f, -0.5f, 0, 1),     
			new Vector4f(-0.5f,  0.5f, 0, 1),     
			new Vector4f( 0.5f,  0.5f, 0, 1),     
		};

		// copy the data into a Vertex Buffer Object in graphics memory
		vertexBuffer = GLBuffers.createBuffer(vertices);

		indices = new int[] { 
			0, 1, 2, 
			3, 2, 1, 
		};

		indexBuffer = GLBuffers.createIndexBuffer(indices);
		// @formatter:on
	}

	public void draw() {

		shader.enable();
		
		// pass in all the model matrices
		shader.setAttribute("a_worldPos", positionBuffer);
		glVertexAttribDivisor(shader.getAttribute("a_worldPos"), 1);

		shader.setAttribute("a_rotation", rotationBuffer);
		glVertexAttribDivisor(shader.getAttribute("a_rotation"), 1);

		shader.setAttribute("a_scale", scaleBuffer);
		glVertexAttribDivisor(shader.getAttribute("a_scale"), 1);

		// write the colour value into the u_colour uniform
		shader.setAttribute("a_colour", colourBuffer);
		glVertexAttribDivisor(shader.getAttribute("a_colour"), 1);
		
		// connect the vertex buffer to the a_position attribute
		shader.setAttribute("a_position", vertexBuffer);

		// draw using an index buffer
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glDrawElementsInstanced(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0, nSquares);
	}


	private static final Vector2f MOVEMENT_SPEED = new Vector2f(0.0f, 0.0f);
	private static final float ROTATION_SPEED = TAU / 6;
	private static final float SCALE_SPEED = 1.0f;
	private Vector2f movement = new Vector2f();
	
	public void update(float dt) {
		// update all the squares
		MOVEMENT_SPEED.mul(dt, movement); // movement = speed * dt

		for (int i = 0; i < position.length; i++) {
			position[i].add(movement);
			angle[i] = (angle[i] + ROTATION_SPEED * dt) % TAU;
			scale[i] = scale[i] * (float) Math.pow(SCALE_SPEED, dt);
		}

		// update the data in the buffers
		GLBuffers.updateBuffer(positionBuffer, position);
		GLBuffers.updateBuffer(rotationBuffer, angle, GL_FLOAT);
		GLBuffers.updateBuffer(scaleBuffer, scale, GL_FLOAT);

	}

}
