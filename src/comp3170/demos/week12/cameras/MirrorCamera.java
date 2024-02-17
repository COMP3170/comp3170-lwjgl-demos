package comp3170.demos.week12.cameras;

import static comp3170.Math.TAU;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.SceneObject;
import comp3170.demos.week12.sceneobjects.Mirror;

public class MirrorCamera extends SceneObject implements Camera {

	private Matrix4f cameraMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();

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
		return dest.set(projectionMatrix);
	}

	@Override
	public Vector4f getViewVector(Vector4f dest) {
		// for a perspective camera
		// the view vector is the origin point of the cameraMatrix
		return cameraMatrix.getColumn(3, dest);
	}

	private Matrix4f modelMatrix = new Matrix4f();
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f mirrorMatrix = new Matrix4f();
	private Matrix4f inverseMirrorMatrix = new Matrix4f();
	private Vector4f position = new Vector4f();

	public void update() {
		setCameraPosition();
		setCameraPespective();
	}

	private void setCameraPosition() {
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

		// rotate so the Z axis faces away for the mirror plane
		if (position.z < 0) {
			getMatrix().rotateY(TAU/2);
		}
	}

	private static final float NEAR = 0f;
	private static final float FAR = 10f;

	private void setCameraPespective() {
		// find the min and max view coordinates of the mirror corners
		getViewMatrix(viewMatrix);

		float minX = Float.POSITIVE_INFINITY;
		float maxX = Float.NEGATIVE_INFINITY;
		float minY = Float.POSITIVE_INFINITY;
		float maxY = Float.NEGATIVE_INFINITY;

		for (int i = 0; i < 4; i++) {
			mirror.getVertex(i, position);
			position.mul(mirrorMatrix).mul(viewMatrix);

			minX = Math.min(minX, position.x);
			maxX = Math.max(maxX, position.x);
			minY = Math.min(minY, position.y);
			maxY = Math.max(maxY, position.y);
		}
		float midX = (maxX + minX) / 2;
		float midY = (maxY + minY) / 2;

		// distance from the mirror plane in the z direction
		getMatrix().getColumn(3, position);
		float z = Math.abs(position.z);

		float offAngleX = (float) Math.atan2(midX, z);
		float offAngleY = (float) Math.atan2(midY, z);

		float h = maxY - minY;

//		float fovy = yAngleMax - yAngleMin;
		float fovy = (float) (Math.atan2(h / 2, z) * 2);
		float aspect = (maxX - minX) / (maxY - minY);

		float near = Math.abs(z) + NEAR;
		float far = near + FAR;

		projectionMatrix.setPerspectiveOffCenter(fovy, offAngleX, offAngleY, aspect, near, far);

	}


}
