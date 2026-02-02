package comp3170.demos.template;

import comp3170.demos.common.cameras.OrthographicOrbittingCamera;

/**
 * A simple wrapper to the more generic camera class, to encapsulate the particular settings we are using for this demo.
 * 
 * The OrthographicOrbittingCamera is an orthographic camera with standard movement controls to orbit around 
 * an object at the origin.
 */

public class DemoCamera extends OrthographicOrbittingCamera {

	
	
	private static final float DISTANCE = 5;
	private static final float WIDTH = 4f;
	private static final float HEIGHT = 4f;
	private static final float NEAR = 0.1f;
	private static final float FAR = 10f;
	
	public DemoCamera()
	{
		super(DISTANCE, WIDTH, HEIGHT, NEAR, FAR);
	}
	
}
