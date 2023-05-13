package comp3170.demos.week12.sceneobjects;

import java.awt.Color;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.demos.common.sceneobjects.Axes3D;
import comp3170.demos.week12.cameras.Camera;
import comp3170.demos.week12.cameras.OrbitingCamera;
import comp3170.demos.week12.cameras.OverheadCamera;

public class ReflectionScene extends SceneObject {

	public static ReflectionScene theScene = null;
	private OverheadCamera overheadCamera;
	private OrbitingCamera mainCamera;
	private Mirror mirror;
	private ViewVolume mainView;
	private ViewVolume mirrorView;
	
	public ReflectionScene() {
		theScene = this;
		
		overheadCamera = new OverheadCamera();
		
		mainCamera = new OrbitingCamera();
		mainView = new ViewVolume(mainCamera);
		mainView.setParent(this);
		Axes3D mainAxes = new Axes3D();
		mainAxes.setParent(mainView);
				
		mirror = new Mirror(mainCamera);
		mirror.setParent(this);
		mirrorView = new ViewVolume(mirror.getCamera());
		mirrorView.setParent(this);
		Axes3D mirrorAxes = new Axes3D();
		mirrorAxes.setParent(mirrorView);
		
		Cube redCube = new Cube(Color.RED);
		redCube.setParent(this);
		redCube.getMatrix().translation(2, 1, 1).scale(0.5f);		

		Cube blueCube = new Cube(Color.BLUE);
		blueCube.setParent(this);
		blueCube.getMatrix().translation(-2, -1, 2).scale(0.5f);		
	}
	
	public Camera getMainCamera() {
		return mainCamera;
	}

	public Camera getOverheadCamera() {
		return overheadCamera;
	}

	public Camera getMirrorCamera() {
		return mirror.getCamera();
	}

	public Mirror getMirror() {
		return mirror;
	}
	
	public void setDrawVolumes(boolean draw) {
		if (draw) {
			mainView.setParent(this);
			mirrorView.setParent(this);
		}
		else {
			mainView.setParent(null);
			mirrorView.setParent(null);			
		}
	}

	public void update(InputManager input, float deltaTime) {
		overheadCamera.update(input, deltaTime);
		mainCamera.update(input, deltaTime);
		mainView.update(input, deltaTime);
		mirror.update(input, deltaTime);
		mirrorView.update(input, deltaTime);
	}


}
