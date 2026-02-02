package comp3170.demos.week11.demos.screenEffect;

import java.io.IOException;

import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.SceneObject;
import comp3170.TextureLibrary;
import comp3170.demos.common.cameras.ICamera;
import comp3170.demos.week11.cameras.Camera;
import comp3170.demos.week11.sceneobjects.TexturedCube;

public class Scene extends SceneObject {
	private static final String TEXTURE = "colours.png";

	private Camera camera;

	public Scene() {
		camera = new Camera();

		try {
			int texture = TextureLibrary.instance.loadTexture(TEXTURE);
			TexturedCube cube = new TexturedCube(texture);
			cube.setParent(this);
			cube.getMatrix().scale(0.5f);
		} catch (IOException | OpenGLException e) {
			e.printStackTrace();
		}
	}

	public ICamera getCamera() {
		return camera;
	}

	public void update(float deltaTime, InputManager input) {
		camera.update(deltaTime, input);
	}

}
