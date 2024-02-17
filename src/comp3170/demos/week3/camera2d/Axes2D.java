package comp3170.demos.week3.camera2d;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.Shader;

public class Axes2D {

	private Vector4f[] xAxis;
	private Vector4f[] yAxis;
	private int xVertexBuffer;
	private int yVertexBuffer;

	private float[] xColour = new float[] { 1, 0, 0 }; // RED
	private float[] yColour = new float[] { 0, 1, 0 }; // GREEN

	public Axes2D() {
		// @formatter:off

		xAxis = new Vector4f[] {
			new Vector4f(0, 0, 0, 1),
			new Vector4f(1, 0, 0, 1),
		};

		yAxis = new Vector4f[] {
			new Vector4f(0, 0, 0, 1),
			new Vector4f(0, 1, 0, 1),
		};

		// @formatter:on

		xVertexBuffer = GLBuffers.createBuffer(xAxis);
		yVertexBuffer = GLBuffers.createBuffer(yAxis);
	}

	public void draw(Shader shader) {

		// X axis

		shader.setAttribute("a_position", xVertexBuffer);
		shader.setUniform("u_colour", xColour);
		glDrawArrays(GL_LINES, 0, xAxis.length);

		// Y axis

		shader.setAttribute("a_position", yVertexBuffer);
		shader.setUniform("u_colour", yColour);
		glDrawArrays(GL_LINES, 0, yAxis.length);

	}
}
