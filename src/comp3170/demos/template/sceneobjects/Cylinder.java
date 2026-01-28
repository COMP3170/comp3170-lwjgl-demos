package comp3170.demos.template.sceneobjects;

import static comp3170.Math.TAU;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.demos.week12.cameras.Camera;
import comp3170.demos.week12.demos.sphere.Scene;

/**
 * An example of how we construct and draw a mesh.
 * 
 * All meshes extend the SceneObject class, which is used to implement the scene graph
 */

public class Cylinder extends SceneObject {

	// Every mesh needs a vertex and fragement shader.
	// These files are found using the ShaderLibrary singleton
	private static final String VERTEX_SHADER = "colourVertex.glsl";
	private static final String FRAGMENT_SHADER = "colourFragment.glsl";
	private Shader shader;

	private static final int NSEGMENTS = 10;
	private static final Vector4f TOP_COLOUR= new Vector4f(1,1,0,1); // (r,g,b,a) colour
	private static final Vector4f BOTTOM_COLOUR= new Vector4f(1,0,1,1); // (r,g,b,a) colour

	// The buffers used to represent the mesh
	// The 'xxxBuffer' variables are handles to OpenGL buffers
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector4f[] colours;
	private int colourBuffer;
	private int[] indices;
	private int indexBuffer;

	public Cylinder() {
		// Create the shader
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		shader.setStrict(false);

		// create the buffers used to represent the mesh 
		createAttributeBuffers();
		createIndexBuffer();
	}


	private void createAttributeBuffers() {

		// Create the vertex attribute buffers, including position and colour
		// It is generally a good idea to draw a picture of this to be sure you
		// are doing it right
		
		// all points are represented as (x,y,z,w)
		int nVertices = NSEGMENTS * 2 + 2; 
		vertices = new Vector4f[nVertices];
		colours = new Vector4f[nVertices];
		
		// I use this pattern a lot to keep track of the current index into the vertex array
		int k = 0;
		
		// Centre points on the top and bottom of the cylinder
		// avoid magic numbers here unless they are very obvious
		
		vertices[k] = new Vector4f(0,0,0,1); 
		colours[k] = BOTTOM_COLOUR;
		k++;

		vertices[k] = new Vector4f(0,1,0,1); 
		colours[k] = TOP_COLOUR;		
		k++;

		// avoid reallocating matrices
		Matrix4f rotation = new Matrix4f(); 
		
		for (int i = 0; i < NSEGMENTS; i++) {
			// We do all our work in radians
			// I have defined a TAU constant which I prefer to use instead of PI
			// as it simplifies calculations like the one below 
			
			float angle = i * TAU / NSEGMENTS;
			
			// You should avoid doing any trig calculations directly.
			// This is almost always better encapsulated as a rotation tranformation
			
			// I typically write comments to show the code as matrix equations
			// Sometimes the order of operations can be confusing.
			// Note that there are JOML methods that *set* the value of the matrix, e.g. matrix.rotation() 
			// and methods that multiply the existing matrix by a transformation, e.g. matrix.rotate())
			rotation.rotationY(angle);	// R = Ry(angle)
						
			// take the points (1,0,0) and (1,1,0) and rotation them about the y axis
			// to generate points around the bottom and top rims of the cylinder 
			
			vertices[k] = new Vector4f(1,0,0,1).mul(rotation); 
			colours[k] = BOTTOM_COLOUR;
			k++;

			vertices[k] = new Vector4f(1,1,0,1).mul(rotation); 
			colours[k] = TOP_COLOUR;		
			k++;			
		}
		
		// Load this data into GL buffers that can be loaded onto the GPU
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		colourBuffer = GLBuffers.createBuffer(colours);
	}

	private void createIndexBuffer() {

		// Join the vertices together into triangles
		// It is generally a good idea to draw a picture of this to be sure you
		// are doing it right
		
		// N triangles on top and bottom + 2N triangles around the outside
		
		int nTriangles = 4 * NSEGMENTS; 
		indices = new int[nTriangles * 3]; // 3 vertices per triangle

		// I commonly use this pattern to track the index into the indices array
		int k = 0;
		
		for (int i = 0; i < NSEGMENTS; i++)
		{
			int j = (i+1) % NSEGMENTS; // next point on rim, wrapping around 
			
			// note: make sure these triangles are specified in anticlockwise winding order (draw a picture)

			// bottom 			
			indices[k++] = 0; // bottom centre
			indices[k++] = 2 * j + 2; // next point on bottom rim 
			indices[k++] = 2 * i + 2; // point on bottom rim

			// top
			indices[k++] = 1; // top centre
			indices[k++] = 2 * i + 3; // point on top rim
			indices[k++] = 2 * j + 3; // next point on top rim 

			// side
			indices[k++] = 2 * i + 3; // point on top rim
			indices[k++] = 2 * i + 2; // point on bottom rim
			indices[k++] = 2 * j + 3; // next point on top rim 

			indices[k++] = 2 * i + 2; // point on bottom rim
			indices[k++] = 2 * j + 2; // next point on bottom rim 
			indices[k++] = 2 * j + 3; // next point on top rim 

		}
		
		// Load this data into GL buffers that can be loaded onto the GPU
		
		indexBuffer = GLBuffers.createIndexBuffer(indices);

	}


	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();

		// Set any shader attributes or uniforms here
		
		// matrices
		shader.setUniform("u_mvpMatrix", mvpMatrix);

		// attributes
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_colour", colourBuffer);

		// draw
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL); // to debug, set this to GL_POINT or GL_LINE
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}

	private float ROTATION_SPEED = TAU / 10; // 1 revolution every 10 seconds
	
	public void update(float deltaTime, InputManager input) {
		// always scale transformations using deltaTime
		getMatrix().rotateY(deltaTime * ROTATION_SPEED);
	}

}
