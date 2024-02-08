package comp3170.demos.week4.bezier;

import org.joml.Vector3f;

public class Scene {
	
	private BezierCurve curve;

	public Scene() {
		
		// Set up the scene

		Vector3f points[] = new Vector3f[] {
			new Vector3f(-0.9f, -1f, 1f),
			new Vector3f(-0.9f,  1f, 1f),
			new Vector3f(0.9f,  0.8f, 1f),
			new Vector3f(0.9f,  1f, 1f),				
		};
		
		curve = new BezierCurve(points);
	}

	public void draw() {
		curve.draw();		
	}
	
}
