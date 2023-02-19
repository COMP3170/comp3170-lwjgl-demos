
package comp3170.demos.week3.instancing;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.GL_FLOAT_VEC3;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import comp3170.GLBuffers;
import comp3170.OpenGLException;
import comp3170.Shader;

public class Squares {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto

	final private File DIRECTORY = new File("src/comp3170/demos/week3/instancing");
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private float[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;

	private Matrix3f[] modelMatrix;
	private Vector2f[] position;
	private float[] angle;
	private float[] scale;
	private Vector3f[] colour;
	private Shader shader;

	private int matrixBuffer;

	private int colourBuffer;

	public Squares(int nSquares) {

		// Compile the shader
		shader = compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		// Make one copy of the mesh
		makeMesh();

		// One transform per instance
		position = new Vector2f[nSquares];
		angle = new float[nSquares];
		scale = new float[nSquares];
		colour = new Vector3f[nSquares];
		modelMatrix = new Matrix3f[nSquares];

		for (int i = 0; i < nSquares; i++) {
			float x = (float) Math.random() * 2 - 1;
			float y = (float) Math.random() * 2 - 1;
			position[i] = new Vector2f(x, y);
			angle[i] = 0;
			scale[i] = 1;
			Color c = Color.getHSBColor((float) Math.random(), 1, 1);
			colour[i] = new Vector3f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f);
			modelMatrix[i] = new Matrix3f();
		}
		
		// create buffers for all the matrices and colours
		matrixBuffer = GLBuffers.createBuffer(modelMatrix);
		colourBuffer = GLBuffers.createBuffer(colour);

	}

	private Shader compileShader(String vertexShader, String fragmentShader) {
		Shader shader = null;
		try {
			File vs = new File(DIRECTORY, vertexShader);
			File fs = new File(DIRECTORY, fragmentShader);
			shader = new Shader(vs, fs);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (OpenGLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return shader;
	}

	private void makeMesh() {

		vertices = new float[] { 
			-0.5f, -0.5f, 
			 1,     0.5f, 
			-0.5f,  1, 
			-0.5f,  0.5f, 
			 1,     0.5f, 
			 0.5f, 1, 
		};

		// copy the data into a Vertex Buffer Object in graphics memory
		vertexBuffer = GLBuffers.createBuffer(vertices, GL_FLOAT_VEC3);

		indices = new int[] { 
			0, 1, 2, 
			3, 2, 1, 
		};

		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}

	public void draw() {

		shader.enable();
		
		// set the model matrices

		calculateModelMatrices();

		// pass in all the model matrices
		shader.setAttributeInstanced("a_modelMatrix", modelMatrix);

		// write the colour value into the u_colour uniform
		shader.setAttributeInstanced("a_colour", colour);
		
		// connect the vertex buffer to the a_position attribute
		shader.setAttribute("a_position", vertexBuffer);

		// draw using an index buffer
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}

	private Matrix3f translationMatrix = new Matrix3f();
	private Matrix3f rotationMatrix = new Matrix3f();
	private Matrix3f scaleMatrix = new Matrix3f();

	private void calculateModelMatrices() {
		
		for (int i = 0; i < modelMatrix.length; i++) {

			translationMatrix.m20(position[i].x);
			translationMatrix.m21(position[i].y);
	
	
			float s = (float) Math.sin(angle[i]);
			float c = (float) Math.cos(angle[i]);
			rotationMatrix.m00(c);
			rotationMatrix.m01(s);
			rotationMatrix.m10(-s);
			rotationMatrix.m11(c);
		
			scaleMatrix.m00(scale[i]);
			scaleMatrix.m11(scale[i]);
	
			// M = MT * MR * MS (in TRaSheS order)
	
			modelMatrix[i].identity();
			modelMatrix[i].mul(translationMatrix);
			modelMatrix[i].mul(rotationMatrix);
			modelMatrix[i].mul(scaleMatrix);
		}
	}

	private static final Vector2f MOVEMENT_SPEED = new Vector2f(0.0f, 0.0f);
	private static final float ROTATION_SPEED = TAU / 6;
	private static final float SCALE_SPEED = 1.0f;
	private Vector2f movement = new Vector2f();
	
	public void update(float dt) {
		// update all the squares
		MOVEMENT_SPEED.mul(dt, movement); // movement = speed * dt

		for (int i = 0; i < modelMatrix.length; i++) {
			position[i].add(movement);
			angle[i] = (angle[i] + ROTATION_SPEED * dt) % TAU;
			scale[i] = scale[i] * (float) Math.pow(SCALE_SPEED, dt);
		}
		
	}

}
