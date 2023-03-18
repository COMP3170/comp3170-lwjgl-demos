package comp3170.demos.week5.mesh;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.SceneObject;
import comp3170.Window;
import comp3170.demos.week5.mesh.sceneobjects.NormalisedCube;
import comp3170.demos.week5.mesh.sceneobjects.SimpleCube;
import comp3170.demos.week5.mesh.sceneobjects.UVSphere;

public class MeshDemo implements IWindowListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto

	private Window window;
	private int screenWidth = 800;
	private int screenHeight = 800;
	private InputManager input;
	private long oldTime;
	private SceneObject[] meshes;
	private int currentMesh;

	public MeshDemo() throws OpenGLException {
		window = new Window("Week 5 mesh demo", screenWidth, screenHeight, this);
		window.run();
	}
	
	@Override
	public void init() {
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		meshes = new SceneObject[] {
			new SimpleCube(),
			new UVSphere(),
			new NormalisedCube(false),
			new NormalisedCube(true),
		};
		currentMesh = 0;
		
		for (int i = 0; i < meshes.length; i++) {
			meshes[i].getMatrix().rotateX(TAU/8).rotateZ(TAU/8);			
		}
		
	    // initialise oldTime
		input = new InputManager(window);
	    oldTime = System.currentTimeMillis();

	}

	private final float ROTATION_SPEED = TAU / 8;

	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;

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

		input.clear();
	}

	@Override
	public void draw() {
		update();

		glClear(GL_COLOR_BUFFER_BIT);		
		meshes[currentMesh].draw();
	}

	@Override
	public void resize(int width, int height) {
		screenWidth = width;
		screenHeight = height;
		glViewport(0, 0, width, height);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws OpenGLException {
		new MeshDemo();
	}
}
