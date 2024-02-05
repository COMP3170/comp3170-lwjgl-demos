package comp3170.demos.week3.camera2d;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.GL_FLOAT_VEC2;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.Shader;

import static comp3170.demos.week3.camera2d.CameraDemo.TAU;

public class House {

	private Vector4f[] vertices;
	private int vertexBuffer;
	
	private float[] colour = new float[] {1, 1, 0}; // YELLOW
	
	private Matrix4f modelMatrix = new Matrix4f();

	public House() {
		vertices = new Vector4f[] {
			// sides
			new Vector4f(-0.375f, 0,    0, 1), 
			new Vector4f(-0.375f, 0.5f, 0, 1),
			
			new Vector4f( 0.375f, 0,    0, 1),
			new Vector4f( 0.375f, 0.5f, 0, 1),
			 
			new Vector4f(-0.375f, 0f,   0, 1),
			new Vector4f( 0.375f, 0f,   0, 1),
			 
			// roof
			new Vector4f(-0.5f, 0.5f,   0, 1),
			new Vector4f( 0.5f, 0.5f,   0, 1),
			 
			new Vector4f(-0.5f, 0.5f,   0, 1),
			new Vector4f( 0,    1,      0, 1),
			 
			new Vector4f( 0.5f, 0.5f,   0, 1),
			new Vector4f( 0,    1,      0, 1),			
		};

		
		vertexBuffer = GLBuffers.createBuffer(vertices);
	}

	public Matrix4f getMatrix() {
		return modelMatrix;
	}
	
	public void draw(Shader shader) {
	
	    shader.setAttribute("a_position", vertexBuffer);
	    shader.setUniform("u_colour", colour);	   	    
		glDrawArrays(GL_LINES, 0, vertices.length);           	

	}

	private static final float MOVEMENT_SPEED = 0.1f;
	private static final float ROTATION_SPEED = TAU / 6;
	private static final float SCALE_SPEED = 1.1f;

	public void update(InputManager input, float dt) {
		if (input.isKeyDown(GLFW_KEY_Z)) {
			modelMatrix.scale((float) Math.pow(SCALE_SPEED, dt)); 	// scale up
		}
		if (input.isKeyDown(GLFW_KEY_X)) {
			modelMatrix.scale((float) Math.pow(1f / SCALE_SPEED, dt));	// scale down
		}
		
		if (input.isKeyDown(GLFW_KEY_Q)) {
			modelMatrix.rotateZ(ROTATION_SPEED * dt);	
		}
		if (input.isKeyDown(GLFW_KEY_E)) {
			modelMatrix.rotateZ(-ROTATION_SPEED * dt);	
		}
		if (input.isKeyDown(GLFW_KEY_W)) {
			// multiply translation matrix on the left to move in world space
			modelMatrix.translateLocal(0, MOVEMENT_SPEED * dt, 0);	
		}
		if (input.isKeyDown(GLFW_KEY_S)) {
			// multiply translation matrix on the left to move in world space
			modelMatrix.translateLocal(0, -MOVEMENT_SPEED * dt, 0);	
		}
		if (input.isKeyDown(GLFW_KEY_A)) {
			// multiply translation matrix on the left to move in world space
			modelMatrix.translateLocal(-MOVEMENT_SPEED * dt, 0, 0);	
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			// multiply translation matrix on the left to move in world space
			modelMatrix.translateLocal(MOVEMENT_SPEED * dt, 0, 0);	
		}		
	}
}
