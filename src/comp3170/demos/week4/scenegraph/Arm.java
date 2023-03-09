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
	
	private Vector3f position; 
	private float angle;
	private Vector3f scale;
	
	protected Matrix4f modelMatrix;
	private Matrix4f translationMatrix;
	private Matrix4f rotationMatrix;
	private Matrix4f scaleMatrix;
	
	private Vector3f colour;
	
	private Shader shader;
	
	public Arm(Shader shader, float width, float height) {
		
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
		
		this.vertices = new float[] {
			-width/2, 0f,
			 width/2, 0f,
			-width/2, height,
			 width/2, height,
		};
		
		// copy the data into a Vertex Buffer Object in graphics memory		
	    vertexBuffer = GLBuffers.createBuffer(vertices, GL_FLOAT_VEC2);
	    
	    this.indices = new int[] {
	    	0, 1, 2,
	    	3, 2, 1,
	    };
	    
	    this.indexBuffer = GLBuffers.createIndexBuffer(indices);
	    this.colour = new Vector3f(1f, 1f, 1f);	// default is white
	    shader = shader;
	    
	    position = new Vector3f(0,0,0);
	    
	    scale = new Vector3f(1,1,0);
	    
		modelMatrix = new Matrix4f();
		translationMatrix = new Matrix4f();
		rotationMatrix = new Matrix4f();
		scaleMatrix = new Matrix4f();
	}
	
	public Vector3f getColour() {
		return colour;
	}
	
	public void setColour(Color color) {		
		colour.x = color.getRed() / 255f;
		colour.y = color.getBlue() / 255f;
		colour.z = color.getGreen() / 255f;
	}
	
	public Vector3f getPosition(Vector3f armPosition) {
		return armPosition.get(position);
	}

	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}

	public float getAngle() {
		return angle;
	}
		
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	public Vector3f getScale(Vector3f dest) {
		return dest.set(scale);
	}

	public void setScale(float sx, float sy) {
		this.scale.x = sx;
		this.scale.y = sy;
	}	
		
	public void draw(Matrix4f parentMatrix, Shader shader) {
		
		// set the model matrix
		
		calculateModelMatrix(parentMatrix);
		
		// draw self
		
		drawSelf(parentMatrix);
		

	}

	private Matrix4f calculateModelMatrix(Matrix4f parentMatrix) {
		translationMatrix.translate(position);
		//Transform.translationMatrix(position.x, position.y, translationMatrix);
		rotationMatrix.rotateZ(angle);
		//Transform.rotationMatrix(angle, rotationMatrix);
		scaleMatrix.scale(scale.x,scale.y,1);

		// M = MP * T * R * S
		
		modelMatrix.set(parentMatrix);
		modelMatrix.mul(translationMatrix);
		modelMatrix.mul(rotationMatrix);
		modelMatrix.mul(scaleMatrix);
		
		return modelMatrix;
	}
	
	@Override
	protected void drawSelf(Matrix4f matrix) {
				
		// set the model matrix		
		shader.setUniform("u_modelMatrix", modelMatrix);
		
        // connect the vertex buffer to the a_position attribute		   
	    shader.setAttribute("a_position", vertexBuffer);

	    // write the colour value into the u_colour uniform 
	    shader.setUniform("u_colour", colour);	    
	    
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
	    glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);	
	}

	
}
