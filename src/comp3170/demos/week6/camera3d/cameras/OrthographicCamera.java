package comp3170.demos.week6.camera3d.cameras;

import org.joml.Matrix4f;

import comp3170.InputManager;

public class OrthographicCamera extends Camera {

	private float width;
	private float height;
	private float near;
	private float far;

	public OrthographicCamera(float distance, float width, float height, float near, float far) {
		super(distance);
		
		this.width = width;
		this.height = height;
		this.near = near;
		this.far = far;
	}
	
	@Override
	public Matrix4f getProjectionMatrix(Matrix4f dest) {		
		return dest.setOrtho(-width/2, width/2, -height/2, height/2, near, far);
	}

}
