package comp3170.demos.week3.animation;

import java.awt.Color;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import comp3170.Shader;
import comp3170.ShaderLibrary;
import static comp3170.Math.TAU;

public class Scene {

	final private String VERTEX_SHADER = "simpleVertex.glsl";
	final private String FRAGMENT_SHADER = "simpleFragment.glsl";

	final private int NSQUARES = 100;
	private ArrayList<Square> squares;
	private Shader shader;

	public Scene() {
		// Compile the shader
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

	    squares = new ArrayList<Square>();
	    
	    for (int i = 0; i < NSQUARES; i++) {
	    	
	    	Square square = new Square();			
			// access the model matrix for the square
	    	// initial M = I (the identity matrix)
			Matrix4f matrix = square.getMatrix();
			
			// translate to random (x,y)
			float x = (float) Math.random() * 2 - 1;
			float y = (float) Math.random() * 2 - 1;
			matrix.translate(x, y, 0);
			
			// rotate and scale
			matrix.rotateZ(0);
			matrix.scale(0.1f, 0.1f, 1);
			
			Color colour = Color.getHSBColor((float) Math.random(), 1, 1);
			square.setColour(colour);

			squares.add(square);
			
	    }

	}
	
	private static final Vector3f MOVEMENT_SPEED = new Vector3f(1.0f, 0, 0);
	private static final float ROTATION_SPEED = TAU / 6;
	private static final float SCALE_SPEED = 1.0f;

	private Vector3f movement = new Vector3f();

	public void update(float deltaTime) {
		for (Square sq : squares) {

			// access the model matrix for the square
			Matrix4f matrix = sq.getMatrix();

			// translate
			// M = M * T
			MOVEMENT_SPEED.mul(deltaTime, movement);  // movement = speed * dt;
			matrix.translate(movement);

			// rotate
			// M = M * R
			matrix.rotateZ(ROTATION_SPEED * deltaTime);			

			// scale
			// M = M * S
			float s = (float) Math.pow(SCALE_SPEED, deltaTime);
			matrix.scale(s,s,1); 
			
			// combined effect is in TRS order
			// M = M * (T * R * S)

		}	
	}

	public void draw() {
		// activate the shader
		shader.enable();

		for (Square square : squares) {
			square.draw(shader);
		}		
	}
}
