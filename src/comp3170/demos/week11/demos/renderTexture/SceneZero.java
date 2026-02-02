package comp3170.demos.week11.demos.renderTexture;

import java.io.IOException;

import comp3170.OpenGLException;
import comp3170.TextureLibrary;
import comp3170.demos.week11.sceneobjects.TexturedCube;

public class SceneZero extends Scene {
	private static final String TEXTURE = "colours.png";

	public SceneZero() {
		super();

		try {
			int texture = TextureLibrary.instance.loadTexture(TEXTURE);
			TexturedCube cube = new TexturedCube(texture);
			cube.setParent(this);
			cube.getMatrix().scale(0.5f);
		} catch (IOException | OpenGLException e) {
			e.printStackTrace();
		}
	}
}
