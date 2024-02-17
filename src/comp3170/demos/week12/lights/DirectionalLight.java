package comp3170.demos.week12.lights;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.demos.common.lights.Light;

public class DirectionalLight extends SceneObject implements Light {

	static final private String VERTEX_SHADER = "simpleVertex.glsl";
	static final private String FRAGMENT_SHADER = "simpleFragment.glsl";
	private Shader shader;

	private Vector4f direction = new Vector4f(1,0,0,0);
	private Vector3f intensity = new Vector3f(1,1,1);	// white
	private Vector3f ambient = new Vector3f(0.1f,0.1f,0.1f); // dim white
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector4f colour = new Vector4f(1,1,0,1); // yellow
	private float length = 10;

	
	public DirectionalLight() {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		vertices = new Vector4f[] {
			new Vector4f(0,0,0,1),
			new Vector4f(0,0,0,1),
		};
		vertices[1].add(direction);

		vertexBuffer = GLBuffers.createBuffer(vertices);
		getMatrix().scale(length);
	}
	
	@Override
	public Vector4f getSourceVector(Vector4f dest) {
		return dest.set(direction);
	}

	@Override
	public Vector3f getIntensity(Vector3f dest) {
		return dest.set(intensity);
	}

	@Override
	public Vector3f getAmbient(Vector3f dest) {
		return dest.set(ambient);
	}
	
	private final static float ROTATION_SPEED = TAU / 6;
	private Vector3f angle = new Vector3f();

	public void update(InputManager input, float deltaTime) {
		
		if (input.isKeyDown(GLFW_KEY_A)) {
			angle.y = (angle.y - ROTATION_SPEED * deltaTime) % TAU;
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			angle.y = (angle.y + ROTATION_SPEED * deltaTime) % TAU;
		}
		if (input.isKeyDown(GLFW_KEY_W)) {
			angle.z = (angle.z - ROTATION_SPEED * deltaTime) % TAU;
		}
		if (input.isKeyDown(GLFW_KEY_S)) {
			angle.z = (angle.z + ROTATION_SPEED * deltaTime) % TAU;
		}
		
		direction.set(1,0,0,0);
		direction.rotateZ(angle.z).rotateY(angle.y);
		
		// update the vertex buffer
		vertices[1].set(0,0,0,1).add(direction);
		GLBuffers.updateBuffer(vertexBuffer, vertices);
	}
	
	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_colour", colour);
		
		glDrawArrays(GL_LINES, 0, vertices.length);
	}


}
