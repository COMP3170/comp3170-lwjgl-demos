package comp3170.demos.week3.animation;

import static comp3170.Math.TAU;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import java.awt.Color;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.Shader;

public class Square {
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;

	private Matrix4f modelMatrix;
	private Vector3f colour;
	
	public Square() {
		
		// verices for a 1x1 square with origin in the centre
		// 
		//  (-0.5,0.5)   (0.5,0.5)
		//       2-----------3
		//       | \         |
		//       |   \       |
		//       |     *     |
		//       |       \   |
		//       |         \ |
		//       0-----------1
		//  (-0.5,-0.5)  (0.5,-0.5)		
		
		vertices = new Vector4f[] {
			new Vector4f(-0.5f, -0.5f, 0, 1),
			new Vector4f( 0.5f, -0.5f, 0, 1),
			new Vector4f(-0.5f,  0.5f, 0, 1),
			new Vector4f( 0.5f,  0.5f, 0, 1),
		};
		
		// copy the data into a Vertex Buffer Object in graphics memory		
	    vertexBuffer = GLBuffers.createBuffer(vertices);
	    
	    indices = new int[] {
	    	0, 1, 2,
	    	3, 2, 1,
	    };
	    
	    indexBuffer = GLBuffers.createIndexBuffer(indices);

	    // set up transform
	    
	    modelMatrix = new Matrix4f();	    
	      
	    // colour
	    
	    colour = new Vector3f(1f, 1f, 1f);	// default is white
	}
	
	public Matrix4f getMatrix() {
		return modelMatrix;
	}
	
	public Vector3f getColour() {
		return colour;
	}
	
	public void setColour(Color color) {		
		colour.x = color.getRed() / 255f;
		colour.y = color.getGreen() / 255f;
		colour.z = color.getBlue() / 255f;
	}
	
	public void draw(Shader shader) {
		
		// set the model matrix		
		shader.setUniform("u_modelMatrix", modelMatrix);
		
        // connect the vertex buffer to the a_position attribute		   
	    shader.setAttribute("a_position", vertexBuffer);

	    // write the colour value into the u_colour uniform 
	    shader.setUniform("u_colour", colour);	    
	    
	    // draw using an index buffer
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
	    glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);		
	}

	private static final Vector3f MOVEMENT_SPEED = new Vector3f(1.0f, 0, 0);
	private static final float ROTATION_SPEED = TAU / 6;
	private static final float SCALE_SPEED = 1.0f;

	private Vector3f movement = new Vector3f();

	public void update(float deltaTime) {
		
		// FIXME: Is this the behaviour we want?
		
		// translate
		// M = M * T
		MOVEMENT_SPEED.mul(deltaTime, movement);  // movement = speed * dt;
		modelMatrix.translate(movement);

		// rotate
		// M = M * R
		modelMatrix.rotateZ(ROTATION_SPEED * deltaTime);			

		// scale
		// M = M * S
		float s = (float) Math.pow(SCALE_SPEED, deltaTime);
		modelMatrix.scale(s,s,1); 
		
		// combined effect is in TRS order
		// M = M * (T * R * S)
		
	}

	
}
