package comp3170.demos.week12.sceneobjects;

import static comp3170.Math.TAU;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week12.cameras.Camera;
import comp3170.demos.week12.lights.Light;
import comp3170.demos.week12.livedemo.Scene;
import comp3170.demos.week12.shaders.ShaderLibrary;

public class Cylinder extends SceneObject {

	private static final int NSIDES = 24;
	private final static String LIGHT_VERTEX = "lightVertex.glsl";
	private final static String LIGHT_FRAGMENT = "lightFragment.glsl";
	private final static String DEPTH_VERTEX = "depthVertex.glsl";
	private final static String DEPTH_FRAGMENT = "depthFragment.glsl";

	private Shader[] shaders;
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector4f[] normals;
	private int normalBuffer;	
	private Vector3f[] colours;
	private int colourBuffer;	
	private int[] indices;
	private int indexBuffer;
	
	private Matrix4f normalMatrix = new Matrix4f();
	private List<Integer> topIndices;
	private List<Integer> bottomIndices;
	private List<Integer> sideIndices;
	
	private Vector3f specularMaterial = new Vector3f(1,1,1);
	private float specularity = 10;

	public Cylinder() {
		shaders = new Shader[] {
			ShaderLibrary.compileShader(LIGHT_VERTEX, LIGHT_FRAGMENT),
			ShaderLibrary.compileShader(DEPTH_VERTEX, DEPTH_FRAGMENT),
		};

		for (int i = 0; i < shaders.length; i++) {
			shaders[i].setStrict(false);			
		}
		
		createVertexBuffer();
		createIndexBuffer();
	}

	private void createVertexBuffer() {
		// Cylinder with pivot at the centre of the base, height 1 and radius 1.

		// vertices[0] = centre of base
		// vertices[1] = centre of top
		// vertices[2*i] = points around bottom edge
		// vertices[2*i+1] = points around top edge
		
		// TODO: This code contains an error. 
		// The normals for the top and bottom faces are not computed correctly.
		
		int nv = NSIDES * 4 + 2;
		vertices = new Vector4f[nv];
		normals = new Vector4f[nv];
		colours = new Vector3f[nv];
		
		int k = 0;			

		// Top and bottom
		
		Vector4f nUp = new Vector4f(0,1,0,0);
		Vector4f nDown = new Vector4f(0,-1,0,0);
		Vector3f cTop = new Vector3f(1,0,0);
		Vector3f cBottom = new Vector3f(1,0,0);

		vertices[k] = new Vector4f(0,0,0,1);
		normals[k] = nDown;
		colours[k] = cTop;
		k++;
		
		vertices[k] = new Vector4f(0,1,0,1);
		normals[k] = nUp;
		colours[k] = cTop;
		k++;
		
		// form the bottom and top edges by rotating point P = (1,0,0) about the y axis
		Vector4f p = new Vector4f(1,0,0,1);
		Vector4f n = new Vector4f(1,0,0,0);

		Matrix4f rotate = new Matrix4f();
		Matrix4f translate = new Matrix4f().translation(0,1,0);
		
		topIndices = new ArrayList<Integer>();
		bottomIndices = new ArrayList<Integer>();
		sideIndices = new ArrayList<Integer>();
		
		for (int i = 0; i < NSIDES; i++) {
			float angle = i * TAU / NSIDES; 
			rotate.rotationY(angle);

			Vector4f vb = p.mul(rotate, new Vector4f());  // vb = R(p)
			Vector4f vt = p.mul(rotate, new Vector4f()).mul(translate);  // vt = T(R(p))
			Vector4f ni = n.mul(rotate, new Vector4f());   // ni = R(n)
			
			// bottom
			bottomIndices.add(k);
			vertices[k] = vb;
			normals[k] = nDown;
			colours[k] = cBottom;
			k++;

			// top
			topIndices.add(k);
			vertices[k] = vt;
			normals[k] = nUp;
			colours[k] = cTop;
			k++;
			
			// side
			sideIndices.add(k);
			vertices[k] = vb; 			
			normals[k] = ni;
			colours[k] = cBottom;
			k++;
			
			sideIndices.add(k);
			vertices[k] = vt; 			
			normals[k] = ni;
			colours[k] = cTop;
			k++;
		}
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		normalBuffer = GLBuffers.createBuffer(normals);
		colourBuffer = GLBuffers.createBuffer(colours);
	}
	
	private void createIndexBuffer() {
		indices = new int[NSIDES * 3 * 4];
		
		int k = 0;
		for (int i = 0; i < NSIDES; i++) {
			int j = (i+1) % NSIDES;
			
			// bottom 
			indices[k++] = 0;
			indices[k++] = bottomIndices.get(j);
			indices[k++] = bottomIndices.get(i); 

			// top 
			indices[k++] = 1;
			indices[k++] = topIndices.get(i);
			indices[k++] = topIndices.get(j);

			// side
			indices[k++] = sideIndices.get(2 * i);
			indices[k++] = sideIndices.get(2 * j);
			indices[k++] = sideIndices.get(2 * i + 1);

			// side
			indices[k++] = sideIndices.get(2 * i + 1);
			indices[k++] = sideIndices.get(2 * j);
			indices[k++] = sideIndices.get(2 * j + 1);
		}
		
		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}


	private Matrix4f modelMatrix = new Matrix4f();
	
	private Vector4f viewPosition = new Vector4f();
	private Vector4f lightDirection = new Vector4f();
	private Vector3f lightIntensity = new Vector3f();
	private Vector3f ambientIntensity = new Vector3f();

	
	@Override
	public void drawSelf(Matrix4f mvpMatrix, int pass) {
		shaders[pass].enable();
		
		// matrices
		getModelToWorldMatrix(modelMatrix);
		shaders[pass].setUniform("u_mvpMatrix", mvpMatrix);
		shaders[pass].setUniform("u_modelMatrix", modelMatrix);
		shaders[pass].setUniform("u_normalMatrix", modelMatrix.normal(normalMatrix));
		
		// camera
		Camera camera = Scene.theScene.getCamera();
		shaders[pass].setUniform("u_viewPosition", camera.getViewVector(viewPosition));
		
		// light
		Light light = Scene.theScene.getLight();
		shaders[pass].setUniform("u_lightDirection", light.getSourceVector(lightDirection));
		shaders[pass].setUniform("u_intensity", light.getIntensity(lightIntensity));
		shaders[pass].setUniform("u_ambientIntensity", light.getAmbient(ambientIntensity));

		// materials
		shaders[pass].setUniform("u_specularMaterial", specularMaterial);
		shaders[pass].setUniform("u_specularity", specularity);
		
		// vertex attributes
		shaders[pass].setAttribute("a_position", vertexBuffer);
		shaders[pass].setAttribute("a_normal", normalBuffer);
		shaders[pass].setAttribute("a_colour", colourBuffer);
		
		// draw call
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);		
	
	}
	
	
}
