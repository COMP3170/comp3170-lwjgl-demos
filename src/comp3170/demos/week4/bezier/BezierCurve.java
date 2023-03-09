package comp3170.demos.week4.bezier;

import org.joml.Vector3f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;

public class BezierCurve {

	private Shader shader;    
	private Vector3f[] vertices;
	private int vertexBuffer;
	
	private final int NPOINTS = 20;
	
	public BezierCurve(Shader shader, Vector3f[] points) {
		this.shader = shader;
				
		this.vertices = new Vector3f[NPOINTS];

		Vector3f q = new Vector3f();

		for (int n = 0; n < NPOINTS; n++)  {
			float t = 1f * n / (NPOINTS-1);
			
			Vector3f p = new Vector3f();

			for (int i = 0; i < 4; i++) {
				points[i].mul(b(i,t), q);	// q = b(i,t) * points[i] 
				p.add(q);					// p += b(i,t) * points[i]
			}
			
			vertices[n] = p;
		}
			
		// copy the data into a Vertex Buffer Object in graphics memory		
	    this.vertexBuffer = GLBuffers.createBuffer(vertices);	    
    }
	
	private float b(int i, float t) {
		
		switch(i) {
		case 0:
			return (1-t) * (1-t) * (1-t);
		case 1:
			return 3 * (1-t) * (1-t) * t;
		case 2:
			return 3 * (1-t) * t * t;
		case 3:
			return t * t * t;
		}
		
		throw new IllegalArgumentException(String.format("Invalid bezier coefficient index: %d", i));
	}
	
	public void draw() {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
        // connect the vertex buffer to the a_position attribute		   
	    shader.setAttribute("a_position", vertexBuffer);

	    // draw lines in blue
	    shader.setUniform("u_colour", new float[] {0,0,1});	    	    
	    gl.glDrawArrays(GL.GL_LINE_STRIP, 0, vertices.length);		

	    // draw points in yellow 
	    shader.setUniform("u_colour", new float[] {1,1,0});	    
	    gl.glDrawArrays(GL.GL_POINTS, 0, vertices.length);		

	}

	
}
