package comp3170.demos.week3.animation;

import java.awt.Color;
import java.util.ArrayList;

import org.joml.Matrix4f;

import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Scene {

	final private String VERTEX_SHADER = "simpleVertex.glsl";
	final private String FRAGMENT_SHADER = "simpleFragment.glsl";

	final private int NSQUARES = 100;
	private ArrayList<Square> squares;
	private Shader shader;

	public Scene() {
		// Compile the shader
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

	    squares = new ArrayList<>();

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


	public void update(float deltaTime) {
		// update each square
		for (Square sq : squares) {
			sq.update(deltaTime);
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
