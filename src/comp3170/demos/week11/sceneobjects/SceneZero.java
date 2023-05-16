package comp3170.demos.week11.sceneobjects;

import java.awt.Color;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.sceneobjects.Axes3D;
import comp3170.demos.week11.cameras.Camera;
import comp3170.demos.week11.cameras.OrbitingCamera;

public class SceneZero extends AbstractScene {
	public SceneZero() {
		super();
		Cube cube = new Cube(Color.RED);
		cube.setParent(this);
		cube.getMatrix().scale(0.5f);
		
	}
}
