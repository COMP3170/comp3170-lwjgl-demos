package comp3170.demos.week5.extrusion;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

import static comp3170.Math.TAU;

public class Extrusion extends SceneObject {

	private static final String VERTEX_SHADER = "simpleVertex.glsl";
	private static final String FRAGMENT_SHADER = "simpleFragment.glsl";
	private static final int NSEGMENTS = 20;
	private static final float RADIUS = 0.8f;
	private static final Vector3f UP = new Vector3f(0,0,1);

	
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector4f colour = new Vector4f(1,1,1,1); 

	private Vector3f[] curve;
	private Vector2f[] crossSection = new Vector2f[] {
		new Vector2f(-0.1f, -0.1f),
		new Vector2f(-0.1f,  0.1f),
		new Vector2f( 0.1f,  0.1f),
		new Vector2f( 0.1f, -0.1f),
	};

	public Extrusion() {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		createCurve();
		createVertexBuffer();
		createIndexBuffer();
	}
	
	private void createCurve() {
		// create a hemicircular curve
		curve = new Vector3f[NSEGMENTS];
		Matrix3f rotate = new Matrix3f();

		for (int i = 0; i < NSEGMENTS; i++) {
			float angle = i * TAU / 2 / (NSEGMENTS-1);			// 0 to TAU/2 (inclusive)
			rotate.rotationZ(angle);							// rotation = Rz(angle)
			curve[i] = new Vector3f(RADIUS, 0, 0).mul(rotate);	// curve[i] = Rz * (r, 0, 0, 1)
		}
		
	}

	private void createVertexBuffer() {
		// allocate vectors for the three coordinate axes 
		// we need both non-homogeneous and homogenous forms
		// because the cross product is only implemented on Vector3f (annoying)
		
		Vector3f iAxis = new Vector3f();
		Vector3f jAxis = new Vector3f();
		Vector3f kAxis = new Vector3f();

		Vector4f iAxis4 = new Vector4f();
		Vector4f jAxis4 = new Vector4f();
		Vector4f kAxis4 = new Vector4f();
		Vector4f curve4 = new Vector4f();
		
		Matrix4f matrix = new Matrix4f();
		
		vertices = new Vector4f[curve.length * crossSection.length];
		
		int k = 0;
		for (int i = 0; i < curve.length; i++) {
			// approximate the tangent by the vector between prev point and next point
			// this requires special cases for the first and last point
			
			Vector3f prev = (i > 0 ? curve[i-1] : curve[i]);
			Vector3f next = (i < curve.length - 1 ? curve[i+1] : curve[i]);
			next.sub(prev, kAxis);  	// vt = P_next - P_prev

			// normalise this to the get k-axis
			kAxis.normalize();
			
			UP.cross(kAxis, iAxis);		// i = up x k
			iAxis.normalize();
			
			kAxis.cross(iAxis, jAxis);	// j = k x i
						
			// convert to homogeneous form
			iAxis4.set(iAxis, 0);
			jAxis4.set(jAxis, 0);
			kAxis4.set(kAxis, 0);
			curve4.set(curve[i], 1);
			
			// create the coordinate frame matrix
			
			matrix.set(iAxis4, jAxis4, kAxis4, curve4);

			// shrink the curve from one end to the other
			
//			matrix.scale(1f - 1.0f * i / curve.length);
//			matrix.rotateZ(TAU/4  * i / curve.length);
			
			// using this matrix to transform the cross-section points into 3D
			
			for (int j = 0; j < crossSection.length; j++) {
				vertices[k] = new Vector4f(crossSection[j].x, crossSection[j].y, 0, 1);  // v = (x, y, 0, 1)
				vertices[k].mul(matrix);
				k++;				
			}
		}
		
	    vertexBuffer = GLBuffers.createBuffer(vertices);		
	}

	private void createIndexBuffer() {
		
		indices = new int[crossSection.length * (curve.length - 1) * 3 * 2];
		
		int k = 0;
		for (int i = 0; i < curve.length - 1; i++) {
			for (int j = 0; j < crossSection.length; j++) {
				int j1 = (j+1) % crossSection.length;
				
				indices[k++] = crossSection.length * i + j;
				indices[k++] = crossSection.length * i + j1; 
				indices[k++] = crossSection.length * (i+1) + j1; 

				indices[k++] = crossSection.length * i + j;
				indices[k++] = crossSection.length * (i+1) + j1; 
				indices[k++] = crossSection.length * (i+1) + j; 

			}
		}
		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}

	@Override
	protected void drawSelf(Matrix4f modelMatrix) {
		shader.enable();
		shader.setUniform("u_mvpMatrix", modelMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_colour", colour);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}

	private final float ROTATION_SPEED = TAU / 8;

	
	public void update(float deltaTime, InputManager input) {
		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			getMatrix().rotateY(ROTATION_SPEED * deltaTime);			
		}
		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			getMatrix().rotateY(-ROTATION_SPEED * deltaTime);			
		}
		if (input.isKeyDown(GLFW_KEY_UP)) {
			getMatrix().rotateX(ROTATION_SPEED * deltaTime);			
		}
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			getMatrix().rotateX(-ROTATION_SPEED * deltaTime);			
		}
			
	}

}
