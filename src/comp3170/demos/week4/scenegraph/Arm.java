package comp3170.demos.week4.scenegraph;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.GL_FLOAT_VEC2;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;

public class Arm extends SceneObject {
	private float[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
		
	private Vector3f colour;
	
	private Shader shader;
	
	public Arm(Shader shader, float width, float height) {
		
		this.shader = shader;
		
		// vertices for a wxh square with origin at the end
		// 
		//  (-w/2, h)     (w/2, h)		
		//       2-----------3
		//       | \         |
		//       |   \       |
		//       |     \     |
		//       |       \   |
		//       |         \ |
		//       0-----*-----1
		//  (-w/2, 0)     (w/2, 0)		
		
		vertices = new float[] {
			-width/2, 0f,
			 width/2, 0f,
			-width/2, height,
			 width/2, height,
		};
		
		// copy the data into a Vertex Buffer Object in graphics memory		
	    vertexBuffer = GLBuffers.createBuffer(vertices, GL_FLOAT_VEC2);
	    
	    indices = new int[] {
	    	0, 1, 2,
	    	3, 2, 1,
	    };
	    
	    indexBuffer = GLBuffers.createIndexBuffer(indices);
	    colour = new Vector3f(1f, 1f, 1f);	// default is white
	}
	
	public Vector3f getColour() {
		return colour;
	}
	
	public void setColour(Color color) {		
		colour.x = color.getRed() / 255f;
		colour.y = color.getBlue() / 255f;
		colour.z = color.getGreen() / 255f;
	}


	@Override
	protected void drawSelf(Matrix4f matrix) {
					
		// set the model matrix		
		shader.setUniform("u_modelMatrix", matrix);
		
        // connect the vertex buffer to the a_position attribute		   
	    shader.setAttribute("a_position", vertexBuffer);

	    // write the colour value into the u_colour uniform 
	    shader.setUniform("u_colour", colour);	    
	    
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
	    glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);	
	}

	
}
