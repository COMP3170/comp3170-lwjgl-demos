package comp3170.demos.week5.livedemo;

import org.joml.Vector4f;

import comp3170.SceneObject;

public class Scene extends SceneObject {

	public static final float TAU = (float) (2 * Math.PI);
	
	public Scene() {
		
		//  Camera
		//   |
		//   | Mview = Mw->c = Mc->w^-1
		//   v
		//  World (this) 
		//   |         \
		//   | M_t1->w  \ Mc->w 
		//   v           v
		//  Triangle 1   Camera
		//   |
		//   | M_t2->t1
		//   v
        //  Triangle 2
		
	    // MVP = Mproj * Mview * M_t1->w * M_t2->t1 
		
		// MVP * v = Mproj * (Mview * (M_t1->w * (M_t2->t1 * v)))
		
		Triangle triangle1 = new Triangle(new Vector4f(1,0,0,1));
		triangle1.setParent(this);

		Triangle triangle2 = new Triangle(new Vector4f(0,0,1,1));
		triangle2.setParent(triangle1);

		triangle1.getMatrix().rotateZ(TAU/8);	
		triangle2.getMatrix().rotateZ(TAU/2).scale(0.5f);
		
		
	}
}
