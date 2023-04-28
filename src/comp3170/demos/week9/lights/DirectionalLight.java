package comp3170.demos.week9.lights;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.InputManager;

public class DirectionalLight implements Light {

	private Vector4f direction = new Vector4f(1,0,0,0);
	private Vector3f intensity = new Vector3f(1,1,1);	// white
	private Vector3f ambient = new Vector3f(0.1f,0.1f,0.1f); // dim white
	
	public DirectionalLight() {
		
	}
	
	@Override
	public Vector4f getSourceVector(Vector4f dest) {
		return dest.set(direction);
	}

	@Override
	public Vector3f getIntensity(Vector3f dest) {
		return dest.set(intensity);
	}

	@Override
	public Vector3f getAmbient(Vector3f dest) {
		return dest.set(ambient);
	}
	
	private final static float ROTATION_SPEED = TAU / 6;
	private Vector3f angle = new Vector3f();

	public void update(InputManager input, float deltaTime) {
		
		if (input.isKeyDown(GLFW_KEY_A)) {
			angle.y = (angle.y + ROTATION_SPEED * deltaTime) % TAU;
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			angle.y = (angle.y - ROTATION_SPEED * deltaTime) % TAU;
		}
		if (input.isKeyDown(GLFW_KEY_W)) {
			angle.x = (angle.x + ROTATION_SPEED * deltaTime) % TAU;
		}
		if (input.isKeyDown(GLFW_KEY_S)) {
			angle.x = (angle.x - ROTATION_SPEED * deltaTime) % TAU;
		}
		
		direction.set(1,0,0,0);
		direction.rotateY(angle.y).rotateX(angle.x);
	}

}
