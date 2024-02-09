package comp3170.demos.week6.camera3d.cameras;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static comp3170.Math.TAU;

import org.joml.Matrix4f;

import comp3170.InputManager;

public class PerspectiveCamera extends Camera {

	private float near;
	private float far;
	private float fovy;
	private float aspect;

	public PerspectiveCamera(float distance, float fovy, float aspect, float near, float far) {
		super(distance);
		
		this.fovy = fovy;
		this.aspect = aspect;
		this.near = near;
		this.far = far;
	}
	
	@Override
	public Matrix4f getProjectionMatrix(Matrix4f dest) {		
		return dest.setPerspective(fovy, aspect, near, far);
	}
	
	private float FOV_DEFAULT = TAU / 6;
	private float FOV_CHANGE = TAU / 6;
	
	@Override
	public void update(float deltaTime, InputManager input) {
		
		super.update(deltaTime, input);	
		if (input.isKeyDown(GLFW_KEY_Z)) {
			fovy += FOV_CHANGE * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_A)) {
			fovy = FOV_DEFAULT;
		}
		if (input.isKeyDown(GLFW_KEY_Q)) {
			fovy -= FOV_CHANGE * deltaTime;
		}		
	}

}
