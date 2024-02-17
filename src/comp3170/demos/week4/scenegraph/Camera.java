package comp3170.demos.week4.scenegraph;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.GL_FLOAT_VEC2;

import org.joml.AxisAngle4f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;


public class Camera extends SceneObject {

    
    private Vector3f position;
	private AxisAngle4f angle;
	private float zoom;
	private float aspect;
	private Vector3f scale;
	
	private Shader shader;

	private float[] vertices;
	private int vertexBuffer;
		
	public Camera(Shader shader) {
		
		this.shader = shader;
		
		position = new Vector3f(0,0,0);
		angle = new AxisAngle4f(0,0,0,0);
		scale = new Vector3f(0,0,0);
		
		// @formatter:off

		// vertices for a 2x2 square with origin in the centre
		// 
		//  (-1,1)         (1,1)
		//       2-----------3
		//       | \         |
		//       |   \       |
		//       |     *     |
		//       |       \   |
		//       |         \ |
		//       0-----------1
		//  (-1,-1)        (1,-1)		
		
		vertices = new float[] {
			-1f, -1f,
			 1f, -1f,
			 1f,  1f,
			-1f,  1f,
		};
		
		// @formatter:on

		// copy the data into a Vertex Buffer Object in graphics memory		
	    vertexBuffer = GLBuffers.createBuffer(vertices, GL_FLOAT_VEC2);

        zoom = 40; // pixels per world unit 
        aspect = 1;
     	
    }
	
	public void setZoom (float zoom)
	{
		this.zoom = zoom;
	}
	

	
	public Matrix4f getViewMatrix(Matrix4f dest, Matrix4f matrix) {
		
		matrix.getTranslation(position);
		
		matrix.getRotation(angle);
		
		dest.identity();
		
		dest.translate(position).rotate(angle);
		// view matrix is the inverse of the camera's model matrix
		return dest.invert();	
	}
	
	public Matrix4f getProjectionMatrix(Matrix4f dest, Matrix4f matrix, float width, float height) {
		
		matrix.getScale(scale);
		
		dest.identity();
		dest.scale(2/scale.x, 2/scale.y, 1);

		return dest;
		
	}
	
	@Override
	protected void drawSelf(Matrix4f matrix) {
					
		// set the model matrix		
		shader.setUniform("u_modelMatrix", matrix);
		
        // connect the vertex buffer to the a_position attribute		   
	    shader.setAttribute("a_position", vertexBuffer);

	    // write the colour value into the u_colour uniform 
	    shader.setUniform("u_colour", new float[] {1,1,1});	    
	    
	    // draw a rectangle as a line loop
		glDrawArrays(GL_LINE_LOOP, 0, vertices.length / 2);   
	}

	
}
