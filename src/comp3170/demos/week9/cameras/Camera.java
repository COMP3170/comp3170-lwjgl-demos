package comp3170.demos.week9.cameras;

import comp3170.demos.common.cameras.OrthographicOrbittingCamera;

public class Camera extends OrthographicOrbittingCamera {

	private static final float WIDTH = 3;
	private static final float HEIGHT = 3;
	private static final float NEAR = 0.1f;
	private static final float FAR = 10f;
	private static final float DISTANCE = 5f;
	
	public Camera() {
		super(DISTANCE, WIDTH, HEIGHT, NEAR, FAR);
	}

}
