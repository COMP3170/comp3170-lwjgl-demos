package comp3170.demos.week6.livedemo;

import static comp3170.Math.TAU;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week6.shaders.ShaderLibrary;

public class Icosahedron extends SceneObject {
	
	private static final String VERTEX_SHADER = "colourVertex.glsl";
	private static final String FRAGMENT_SHADER = "colourFragment.glsl";
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector3f[] hsbColours;
	private int hsbBuffer;

	public Icosahedron() {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER , FRAGMENT_SHADER);
			
		createVertexBuffer();		
		createColourBuffer();		
		createIndexBuffer();
	}


	private void createVertexBuffer() {
		vertices = new Vector4f[12];
		
		vertices[0] = new Vector4f(0,1,0,1);
		vertices[11] = new Vector4f(0,-1,0,1);

		float angleZ = (float) Math.atan(0.5);  // ~26.57°
		
		Matrix4f rotate = new Matrix4f();
		
		for (int i = 1; i <= 10; i++) {
			float angleY = (i-1) * TAU / 10;			
			float sign = (i % 2 == 0 ? -1 : 1);
			rotate.identity().rotateY(angleY).rotateZ(sign * angleZ);			
			vertices[i] = new Vector4f(1,0,0,1).mul(rotate);
		}
				
		vertexBuffer = GLBuffers.createBuffer(vertices);
	}
	
	private void createColourBuffer() {
		hsbColours = new Vector3f[12];
		
		float hue = 1;
		float sat = 1;
		float bri = 1;
		
		for (int i = 0; i < hsbColours.length; i++) {
			hue = (float) i / (hsbColours.length -1 ); // hue from 0 to 1 in 12 steps
			hsbColours[i] = new Vector3f(hue, sat, bri);
		}
		
		hsbBuffer = GLBuffers.createBuffer(hsbColours);
	}

	private void createIndexBuffer() {
		indices = new int[] {
			// top
			0, 1, 3,
			0, 3, 5,
			0, 5, 7,
			0, 7, 9,
			0, 9, 1,
			
			// middle
			1, 2, 3,
			4, 3, 2,
			3, 4, 5,
			6, 5, 4,
			5, 6, 7, 
			8, 7, 6,
			7, 8, 9,
			10, 9, 8,
			1, 9, 10,
			2, 1, 10,
			
			// bottom
			11, 4, 2,
			11, 6, 4,
			11, 8, 6,
			11, 10, 8,
			11, 2, 10,
			
		};		
		
		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}

	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_hsb", hsbBuffer);

//		glPointSize(5);
//		glDrawArrays(GL_POINTS, 0, vertices.length);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}

	
	
}
