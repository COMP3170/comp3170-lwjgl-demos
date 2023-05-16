package comp3170.demos.week11.sceneobjects;

import java.awt.Color;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.sceneobjects.Axes3D;
import comp3170.demos.week10.textures.TextureLibrary;
import comp3170.demos.week11.cameras.Camera;
import comp3170.demos.week11.cameras.OrbitingCamera;

public class SceneOne extends AbstractScene {
	
	public SceneOne(int renderTexture) {
		super();
		
		TexturedCube cube = new TexturedCube(renderTexture);				
		cube.setParent(this);
	}

}
