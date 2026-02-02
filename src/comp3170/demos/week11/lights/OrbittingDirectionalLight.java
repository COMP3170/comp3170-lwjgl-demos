package comp3170.demos.week11.lights;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_8;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_4;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_6;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_7;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_9;

import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.lights.DirectionalLight;
import comp3170.demos.common.lights.ILight;
import comp3170.demos.common.sceneobjects.OrbitingArmature;

/**
 * Implement a directional light that rotates around the parent object
 */

public class OrbittingDirectionalLight extends SceneObject implements ILight {

	// note this distance doesn't really matter for a directional light.
	private float distance = 10;
	private OrbitingArmature armature;
	private DirectionalLight light;
	
	// change default key-bindings to avoid overlap with Camera
	private int keyUp = GLFW_KEY_KP_8;
	private int keyDown = GLFW_KEY_KP_2;
	private int keyLeft = GLFW_KEY_KP_4;
	private int keyRight = GLFW_KEY_KP_6;
	private int keyOut = GLFW_KEY_KP_7;
	private int keyIn = GLFW_KEY_KP_9;
	
	public OrbittingDirectionalLight() {
		armature = new OrbitingArmature(distance);
		armature.setParent(this);
		armature.rebindKeys(keyUp, keyDown, keyLeft, keyRight, keyIn, keyOut);
		armature.setDrawArm(true);
		
		light = new DirectionalLight();
		light.setParent(armature);
	}

	@Override
	public Vector4f getSourceVector(Vector4f dest) {
		return light.getSourceVector(dest);
	}

	@Override
	public Vector3f getIntensity(Vector3f dest) {
		return light.getIntensity(dest);
	}

	@Override
	public Vector3f getAmbient(Vector3f dest) {
		return light.getAmbient(dest);
	}

	public void update(float deltaTime, InputManager input) {
		armature.update(deltaTime, input);	
	}

}
