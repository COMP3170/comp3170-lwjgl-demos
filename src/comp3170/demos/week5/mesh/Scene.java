package comp3170.demos.week5.mesh;

import comp3170.SceneObject;
import comp3170.demos.week5.mesh.sceneobjects.NormalisedCube;
import comp3170.demos.week5.mesh.sceneobjects.SimpleCube;
import comp3170.demos.week5.mesh.sceneobjects.UVSphere;
import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import comp3170.InputManager;

public class Scene {

	private SceneObject[] meshes;
	private int currentMesh;

	public Scene() {
		meshes = new SceneObject[] { 
			new SimpleCube(), 
			new UVSphere(), 
			new NormalisedCube(false),
			new NormalisedCube(true), 
		};

		currentMesh = 0;

		for (int i = 0; i < meshes.length; i++) {
			meshes[i].getMatrix().rotateX(TAU / 8).rotateZ(TAU / 8);
		}
	}

	private final float ROTATION_SPEED = TAU / 8;

	public void update(float deltaTime, InputManager input) {
		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			meshes[currentMesh].getMatrix().rotateY(ROTATION_SPEED * deltaTime);			
		}
		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			meshes[currentMesh].getMatrix().rotateY(-ROTATION_SPEED * deltaTime);			
		}
		if (input.isKeyDown(GLFW_KEY_UP)) {
			meshes[currentMesh].getMatrix().rotateX(ROTATION_SPEED * deltaTime);			
		}
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			meshes[currentMesh].getMatrix().rotateX(-ROTATION_SPEED * deltaTime);			
		}
		
		if (input.wasKeyPressed(GLFW_KEY_SPACE)) {
			currentMesh = (currentMesh + 1) % meshes.length;
		}		
	}

	public void draw() {
		meshes[currentMesh].draw();		
	}
	
	
}
