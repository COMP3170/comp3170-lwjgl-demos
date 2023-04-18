package comp3170.demos.week8.cameras;

import org.joml.Matrix4f;

import comp3170.InputManager;

public interface Camera {
	public Matrix4f getViewMatrix(Matrix4f dest);
	public Matrix4f getProjectionMatrix(Matrix4f dest);
	public void update(InputManager input, float deltaTime);
	
}
