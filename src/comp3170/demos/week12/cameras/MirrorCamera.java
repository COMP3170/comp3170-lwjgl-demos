package comp3170.demos.week12.cameras;

import static comp3170.Math.TAU;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.demos.week12.sceneobjects.Mirror;

public class MirrorCamera implements Camera {

	private static final float ASPECT = 1;
	private static final float NEAR = 0.1f;
	private static final float FAR = 30f;
	private Matrix4f cameraMatrix = new Matrix4f();
	
	private float fovy = TAU/4;

	public MirrorCamera(Mirror mirror, comp3170.demos.week10.cameras.Camera camera) {
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public Matrix4f getCameraMatrix(Matrix4f dest) {
		return dest.set(cameraMatrix);
	}

	@Override
	public Matrix4f getViewMatrix(Matrix4f dest) {
		return cameraMatrix.invert(dest);
	}

	@Override
	public Matrix4f getProjectionMatrix(Matrix4f dest) {
		return dest.setPerspective(fovy, ASPECT, NEAR, FAR);
	}
	
	@Override
	public Vector4f getViewVector(Vector4f dest) {
		// for a perspective camera
		// the view vector is the origin point of the cameraMatrix
		return cameraMatrix.getColumn(3, dest);
	}

}
