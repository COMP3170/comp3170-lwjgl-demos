package comp3170.demos.common.sceneobjects;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
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

/**
 * The OrbitingArmature is designed to allow a camera (or light) to be controlled to orbit around an
 * object (the parent SceneObject) at a fixed distance and look at it from different directions.
 */

public class OrbitingArmature extends SceneObject {

	final static float ROTATION_SPEED = TAU / 4;
	final static float MOVEMENT_SPEED = 1;

	// key bindings
	private int keyUp = GLFW_KEY_UP;
	private int keyDown = GLFW_KEY_DOWN;
	private int keyLeft = GLFW_KEY_LEFT;
	private int keyRight = GLFW_KEY_RIGHT;
	private int keyOut = GLFW_KEY_S;
	private int keyIn = GLFW_KEY_W;
	
	private float distance;
	private Vector3f angle = new Vector3f(0, 0, 0);

	// to draw:
	private boolean isDrawing = false;
	static final private String VERTEX_SHADER = "simpleVertex.glsl";
	static final private String FRAGMENT_SHADER = "simpleFragment.glsl";
	private Shader shader;

	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector4f colour = new Vector4f(1,1,0,1); // yellow
	
	public OrbitingArmature(float distance) {

		this.distance = distance;

		// draw a line from this object to its parent
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		vertices = new Vector4f[] {
			new Vector4f(0,0,0,1),
			new Vector4f(0,0,-distance,1),
		};
		vertexBuffer = GLBuffers.createBuffer(vertices);
	}
	
	public void setDrawArm(boolean draw)
	{
		isDrawing = draw;
	}

	public void setColour(Vector4f colour)
	{
		this.colour.set(colour);
	}

	/**
	 * Rebind the keys used to control the armature
	 * @param up
	 * @param down
	 * @param left
	 * @param right
	 * @param in
	 * @param out
	 */
	
	public void rebindKeys(int up, int down, int left, int right, int in, int out) {
		keyUp = up;
		keyDown = down;
		keyLeft = left;
		keyRight = right;
		keyIn = in;
		keyOut = out;
	}
	
	/**
	 * Rotate the armature around the parent object, at a given distance
	 * 
	 * @param deltaTime
	 * @param input
	 */
	public void update(float deltaTime, InputManager input) {

		// key controls to orbit camera around the origin

		if (input.isKeyDown(keyUp)) {
			angle.x -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(keyDown)) {
			angle.x += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(keyLeft)) {
			angle.y -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(keyRight)) {
			angle.y += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(keyOut)) {
			distance += MOVEMENT_SPEED * deltaTime;
		}
		if (input.isKeyDown(keyIn)) {
			distance -= MOVEMENT_SPEED * deltaTime;
		}
		
		Matrix4f modelMatrix = getMatrix();
		modelMatrix.identity();
		modelMatrix.rotateY(angle.y);	// heading
		modelMatrix.rotateX(angle.x);	// pitch
		modelMatrix.rotateZ(angle.z);	// roll
		modelMatrix.translate(0,0,distance);
	}

	
	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		if (isDrawing)
		{
			shader.enable();

			shader.setAttribute("a_position", vertexBuffer);
			shader.setUniform("u_mvpMatrix", mvpMatrix);
			shader.setUniform("u_colour", colour);

			glDrawArrays(GL_LINES, 0, vertices.length);			
		}
	}

}
