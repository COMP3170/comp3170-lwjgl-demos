package comp3170.demos.week12.cameras;

import static comp3170.Math.TAU;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.SceneObject;
import comp3170.demos.week12.sceneobjects.Mirror;

public class MirrorCamera extends SceneObject implements Camera {

	private static final float ASPECT = 1;
	private static final float NEAR = 0.1f;
	private static final float FAR = 30f;
	private float fovy = TAU/4;
	private Matrix4f cameraMatrix = new Matrix4f();
	
	private Mirror mirror;
	private Camera camera;

	public MirrorCamera(Mirror mirror, Camera camera) {
		this.mirror = mirror;
		this.camera = camera; 		
	}

	@Override
	public Matrix4f getCameraMatrix(Matrix4f dest) {
		return getModelToWorldMatrix(dest);
	}

	@Override
	public Matrix4f getViewMatrix(Matrix4f dest) {
		getModelToWorldMatrix(cameraMatrix);
		cameraMatrix.normalize3x3();
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

	private Matrix4f modelMatrix = new Matrix4f();
	private Matrix4f mirrorMatrix = new Matrix4f();
	private Matrix4f inverseMirrorMatrix = new Matrix4f();
	private Vector4f position = new Vector4f(); 

	public void update() {
		// the mirror camera sits in the mirror's model space
		// as far behind the mirror as the main camera is in front
		
		// get the position of the other camera
		camera.getCameraMatrix(modelMatrix);
		modelMatrix.getColumn(3, position);
		
		// convert to mirror's model space
		mirror.getModelToWorldMatrix(mirrorMatrix);
		mirrorMatrix.invert(inverseMirrorMatrix);
		position.mul(inverseMirrorMatrix);
		
		// reflect in the Z-axis
		position.z *= -1;		
		getMatrix().translation(position.x, position.y, position.z);
	}
}
