package comp3170.demos.week6.camera3d.cameras;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;

import org.joml.Matrix4f;

import comp3170.InputManager;

public class OrthographicCamera extends Camera {

	private float width;
	private float height;
	private float near;
	private float far;

	private float ZOOM_DEFAULT;
	private float ZOOM_CHANGE = 2f;

	public OrthographicCamera(float distance, float width, float height, float near, float far) {
		super(distance);

		this.width = width;
		this.height = height;
		this.near = near;
		this.far = far;

		ZOOM_DEFAULT = width;
	}

	@Override
	public Matrix4f getProjectionMatrix(Matrix4f dest) {
		return dest.setOrtho(-width / 2, width / 2, -height / 2, height / 2, near, far);
	}

	public void update(InputManager input, float deltaTime) {

		super.update(deltaTime, input);
		if (input.isKeyDown(GLFW_KEY_Z)) {
			width += ZOOM_CHANGE * deltaTime;
			height += ZOOM_CHANGE * deltaTime;
		}
		
		if (input.isKeyDown(GLFW_KEY_A)) {
			width = ZOOM_DEFAULT;
			height = ZOOM_DEFAULT;
		}
		
		if (input.isKeyDown(GLFW_KEY_Q)) {
			width -= ZOOM_CHANGE * deltaTime;
			height -= ZOOM_CHANGE * deltaTime;
		}
	}
}
