package comp3170.demos.week10.cameras;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.InputManager;

public interface Camera {
	public Matrix4f getCameraMatrix(Matrix4f dest);
	public Matrix4f getViewMatrix(Matrix4f dest);
	public Matrix4f getProjectionMatrix(Matrix4f dest);
	public Vector4f getViewVector(Vector4f dest);	
}
