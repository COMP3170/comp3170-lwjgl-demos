package comp3170.demos.week12.sceneobjects;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.*;
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
import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week12.shaders.ShaderLibrary;

public class Frustum extends SceneObject {

	private float aspect = 1;
	private float fovy = TAU / 4;
	private float near = 1f;
	private float far = 5f;
	private float offAngleX = 0;
	private float offAngleY = 0;
	
	private static final String VERTEX_SHADER = "simpleVertex.glsl";
	private static final String FRAGMENT_SHADER = "simpleFragment.glsl";
	private Shader shader;
	private Vector4f[] vertices;
	private Vector4f[] verticesNDC;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector4f colour = new Vector4f(1,1,1,1);
	private Matrix4f projectionMatrix = new Matrix4f();
	
	public Frustum() {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		projectionMatrix.setPerspectiveOffCenter(fovy, offAngleX, offAngleY, aspect, near, far);
		projectionMatrix.invert();
		
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
		}
		
		vertexBuffer = GLBuffers.createBuffer(vertices);

		// indices for the lines forming each face

		indices = new int[] {
			// front
			0, 1, 2,
			2, 3, 0,
			
			// back
			4, 5, 6,
			6, 7, 4,
			
			// top
			0, 7, 6,
			6, 1, 0,
			
			// bottom 
			2, 5, 4,
			4, 3, 2,
			
			// left
			2, 1, 6,
			6, 5, 2,
			
			// right
			7, 0, 3,
			3, 4, 7,
			
		};
		
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

	private final static float ANGLE_CHANGE = TAU / 4;

	public void update(InputManager input, float deltaTime) {
		
		if (input.isKeyDown(GLFW_KEY_W)) {
			offAngleY += ANGLE_CHANGE * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_S)) {
			offAngleY -= ANGLE_CHANGE * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_A)) {
			offAngleX -= ANGLE_CHANGE * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			offAngleX += ANGLE_CHANGE * deltaTime;
		}

		
		projectionMatrix.setPerspectiveOffCenter(fovy, offAngleX, offAngleY, aspect, near, far);
		projectionMatrix.invert();
		
		for (int i = 0; i < vertices.length; i++) {
			verticesNDC[i].mul(projectionMatrix, vertices[i]);
		}
		GLBuffers.updateBuffer(vertexBuffer, vertices);

	}

}
