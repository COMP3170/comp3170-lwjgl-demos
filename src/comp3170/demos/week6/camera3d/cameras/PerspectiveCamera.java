package comp3170.demos.week6.camera3d.cameras;

import java.awt.event.KeyEvent;

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
	public void update(InputManager input, float deltaTime) {
		super.update(input, deltaTime);	
		
		if (input.isKeyDown(KeyEvent.VK_Q)) {
			fovy += FOV_CHANGE * deltaTime;
		}
		if (input.isKeyDown(KeyEvent.VK_A)) {
			fovy = FOV_DEFAULT;
		}
		if (input.isKeyDown(KeyEvent.VK_Z)) {
			fovy -= FOV_CHANGE * deltaTime;
		}		
	}

}
