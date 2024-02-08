package comp3170.demos.week3.camera2d;

import java.awt.Color;

import static org.lwjgl.opengl.GL41.*;

import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.Shader;

public class CameraView {
	private Vector4f[] vertices;
	private int vertexBuffer;
	
	private float[] colour = new float[] {1f, 1f, 1f};	// white

	public CameraView() {
		
		vertices = new Vector4f[] {
			new Vector4f(-1f, -1f, 0, 1),
			new Vector4f( 1f, -1f, 0, 1),
			new Vector4f( 1f,  1f, 0, 1),
			new Vector4f(-1f,  1f, 0, 1),
		};
		
	    vertexBuffer = GLBuffers.createBuffer(vertices);
	    
	}
	
	public void draw(Shader shader) {
		// connect the vertex buffer to the a_position attribute		   
	    shader.setAttribute("a_position", vertexBuffer);
	    shader.setUniform("u_colour", colour);	    
		glDrawArrays(GL_LINE_LOOP, 0, vertices.length);           		    
	}
	
}
