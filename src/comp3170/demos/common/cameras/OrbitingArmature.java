package comp3170.demos.common.cameras;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import comp3170.InputManager;
import comp3170.SceneObject;

public class OrbitingArmature extends SceneObject {

	final static float ROTATION_SPEED = TAU / 4;
	final static float MOVEMENT_SPEED = 1;

	private float distance;
	private Vector3f angle = new Vector3f(0, 0, 0);

	public OrbitingArmature(float distance) {
		this.distance = distance;
	}
	
	public void update(float deltaTime, InputManager input) {

		// key controls to orbit camera around the origin

		if (input.isKeyDown(GLFW_KEY_UP)) {
			angle.x -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			angle.x += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			angle.y -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			angle.y += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_S)) {
			distance += MOVEMENT_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_W)) {
			distance -= MOVEMENT_SPEED * deltaTime;
		}
		
		Matrix4f modelMatrix = getMatrix();
		modelMatrix.identity();
		modelMatrix.rotateY(angle.y);	// heading
		modelMatrix.rotateX(angle.x);	// pitch
		modelMatrix.rotateZ(angle.z);	// roll
		modelMatrix.translate(0,0,distance);
	}

}
