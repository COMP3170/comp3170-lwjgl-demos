package comp3170.demos.week12.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week12.cameras.Camera;
import comp3170.demos.week12.lights.Light;
import comp3170.demos.week12.livedemo.pixelart.Scene;
import comp3170.demos.week12.shaders.ShaderLibrary;

public class Plane extends SceneObject {

	private final static String LIGHT_VERTEX = "lightVertex.glsl";
	private final static String LIGHT_FRAGMENT = "lightFragment.glsl";

	private Shader shader;
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector4f[] normals;
	private int normalBuffer;	
	private Vector3f[] colours;
	private int colourBuffer;	
	private int[] indices;
	private int indexBuffer;

	private Vector3f colour = new Vector3f(1,1,1);
	private Matrix4f normalMatrix = new Matrix4f();
	
	private Vector3f specularMaterial = new Vector3f(0,0,0);
	private float specularity = 10;

	public Plane() {
		shader = ShaderLibrary.compileShader(LIGHT_VERTEX, LIGHT_FRAGMENT);
		shader.setStrict(false);
		
		createVertexBuffer();
		createIndexBuffer();
	}

	private void createVertexBuffer() {
		vertices = new Vector4f[] {
			new Vector4f(-1,0,-1,1),
			new Vector4f( 1,0,-1,1),
			new Vector4f(-1,0, 1,1),
			new Vector4f( 1,0, 1,1),
		};
		normals = new Vector4f[] {
			new Vector4f(0,1,0,0),
			new Vector4f(0,1,0,0),
			new Vector4f(0,1,0,0),
			new Vector4f(0,1,0,0),				
		};
		colours = new Vector3f[] {
			colour,
			colour,
			colour,
			colour,
		};
				
		vertexBuffer = GLBuffers.createBuffer(vertices);
		normalBuffer = GLBuffers.createBuffer(normals);
		colourBuffer = GLBuffers.createBuffer(colours);
	}
	
	private void createIndexBuffer() {
		indices = new int[] {
			0,1,2,
			3,2,1,
		};		
		indexBuffer = GLBuffers.createIndexBuffer(indices);

	}


	private Matrix4f modelMatrix = new Matrix4f();
	
	private Vector4f lightDirection = new Vector4f();
	private Vector3f lightIntensity = new Vector3f();
	private Vector3f ambientIntensity = new Vector3f();
	private Vector4f viewPosition = new Vector4f();

	@Override
	public void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		
		// matrices
		getModelToWorldMatrix(modelMatrix);
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_modelMatrix", modelMatrix);
		shader.setUniform("u_normalMatrix", modelMatrix.normal(normalMatrix));
		
		// camera
		Camera camera = Scene.theScene.getCamera();
		shader.setUniform("u_viewPosition", camera.getViewVector(viewPosition));
		
		// light
		Light light = Scene.theScene.getLight();
		shader.setUniform("u_lightDirection", light.getSourceVector(lightDirection));
		shader.setUniform("u_intensity", light.getIntensity(lightIntensity));
		shader.setUniform("u_ambientIntensity", light.getAmbient(ambientIntensity));

		// materials
		shader.setUniform("u_specularMaterial", specularMaterial);
		shader.setUniform("u_specularity", specularity);
		
		// vertex attributes
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_normal", normalBuffer);
		shader.setAttribute("a_colour", colourBuffer);
		
		// draw call
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);		
	
	}
	
	
}
