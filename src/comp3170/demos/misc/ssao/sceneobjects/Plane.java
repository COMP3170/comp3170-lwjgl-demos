package comp3170.demos.misc.ssao.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.demos.common.cameras.Camera;
import comp3170.demos.misc.ssao.Scene;

public class Plane extends SceneObject {

	private static final String VERTEX_SHADER = "simpleVertex.glsl";
	private static final String FRAGMENT_SHADER = "simpleFragment.glsl";

	private Shader shader;
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector4f[] normals;
	private int normalBuffer;	
	private int[] indices;
	private int indexBuffer;

	private Vector4f colour;
	private Matrix4f normalMatrix = new Matrix4f();
	
	public Plane(Color c) {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		shader.setStrict(false);

		colour = new Vector4f(c.getComponents(null));
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
				
		vertexBuffer = GLBuffers.createBuffer(vertices);
		normalBuffer = GLBuffers.createBuffer(normals);
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
//		Light light = Scene.theScene.getLight();
//		shader.setUniform("u_lightDirection", light.getSourceVector(lightDirection));
//		shader.setUniform("u_intensity", light.getIntensity(lightIntensity));
//		shader.setUniform("u_ambientIntensity", light.getAmbient(ambientIntensity));

		// material
		shader.setUniform("u_colour", colour);
		
		// vertex attributes
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_normal", normalBuffer);
		
		// draw call
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);		
	
	}
	
	
}
