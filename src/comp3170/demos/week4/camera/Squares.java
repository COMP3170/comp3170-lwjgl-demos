package comp3170.demos.week4.camera;

import static com.jogamp.opengl.GL.GL_TRIANGLES;

import java.awt.Color;

import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;

public class Square {
	private float[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;

	private Vector2f position; 
	private float angle;
	private Vector2f scale;
	
	private Matrix3f modelMatrix;
	private Matrix3f translationMatrix;
	private Matrix3f rotationMatrix;
	private Matrix3f scaleMatrix;
	private Vector3f colour;
	
	public Square(Shader shader) {
		
		// verices for a 1x1 square with origin in the centre
		// 
		//  (-0.5,0.5)   (0.5,0.5)
		//       2-----------3
		//       | \         |
		//       |   \       |
		//       |     \     |
		//       |       \   |
		//       |         \ |
		//       0-----------1
		//  (-0.5,-0.5)  (0.5,-0.5)		
		
		this.vertices = new float[] {
			-0.5f, -0.5f,
			 0.5f, -0.5f,
			-0.5f,  0.5f,
			 0.5f,  0.5f,
		};
		
		// copy the data into a Vertex Buffer Object in graphics memory		
	    this.vertexBuffer = GLBuffers.createBuffer(vertices, GL4.GL_FLOAT_VEC2);
	    
	    this.indices = new int[] {
	    	0, 1, 2,
	    	3, 2, 1,
	    };
	    
	    this.indexBuffer = GLBuffers.createIndexBuffer(indices);

	    // set up transform
	    
	    this.position = new Vector2f(0f, 0f);
	    this.angle = 0f;
	    this.scale = new Vector2f(1f, 1f);
	    this.modelMatrix = new Matrix3f();	    
	    
	    this.translationMatrix = new Matrix3f();    
	    this.rotationMatrix = new Matrix3f();
	    this.scaleMatrix = new Matrix3f();
	    
	    // colour
	    
	    this.colour = new Vector3f(1f, 1f, 1f);	// default is white
	}
	
	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}

	public void translate(Vector2f movement) {
		position.x += movement.x;
		position.y += movement.y;
	}
	
	public float getAngle() {
		return angle;
	}
		
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	public void rotate(float radians) {
		angle += radians;
	}
	
	public Vector2f getScale() {
		return scale;
	}

	public void setScale(float sx, float sy) {
		this.scale.x = sx;
		this.scale.y = sy;
	}

	public void scale(float factor) {
		scale.mul(factor);		
	}	
	
	public Vector3f getColour() {
		return colour;
	}
	
	public void setColour(Color color) {		
		colour.x = color.getRed() / 255f;
		colour.y = color.getBlue() / 255f;
		colour.z = color.getGreen() / 255f;
	}
	
	public void draw(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// set the model matrix
		
		calculateModelMatrix();
		shader.setUniform("u_modelMatrix", modelMatrix);
		
        // connect the vertex buffer to the a_position attribute		   
	    shader.setAttribute("a_position", vertexBuffer);

	    // write the colour value into the u_colour uniform 
	    shader.setUniform("u_colour", colour);	    
	    
	    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
	    gl.glDrawElements(GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);		
	}

	private void calculateModelMatrix() {
		//      [ 1  0  Tx ]
		// MT = [ 0  1  Ty ]
		//      [ 0  0  1  ]
		
		translationMatrix.m20(position.x);
		translationMatrix.m21(position.y);
		
		//      [ cos(a)  -sin(a)  0 ]
		// MR = [ sin(a)   cos(a)  0 ]
		//      [ 0        0       1 ]
		
		float s = (float) Math.sin(angle);
		float c = (float) Math.cos(angle);
		rotationMatrix.m00(c);
		rotationMatrix.m01(s);
		rotationMatrix.m10(-s);
		rotationMatrix.m11(c);

		//      [ sx  0   0 ]
		// MS = [ 0   sy  0 ]
		//      [ 0   0   1 ]

		scaleMatrix.m00(scale.x);
		scaleMatrix.m11(scale.y);

		// M = MT * MR * MS
		
		modelMatrix.identity();
		modelMatrix.mul(translationMatrix);
		modelMatrix.mul(rotationMatrix);
		modelMatrix.mul(scaleMatrix);
	}
	
}
