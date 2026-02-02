package comp3170.demos.week11.cameras;

import static comp3170.Math.TAU;
import comp3170.demos.common.cameras.PerspectiveOrbittingCamera;

public class Camera extends PerspectiveOrbittingCamera {

	private final static float DISTANCE = 5;
	private final static float FOVY = TAU / 6;
	private final static float ASPECT = 1;
	private final static float NEAR = 0.1f;	
	private final static float FAR = 10f;	
	
	public Camera() {
		super(DISTANCE, FOVY, ASPECT, NEAR, FAR);
	}


	
}
