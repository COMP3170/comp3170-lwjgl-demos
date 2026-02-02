package comp3170.demos.week9.sceneobjects;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_COMMA;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_N;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PERIOD;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_V;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
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
import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.demos.common.lights.ILight;
import comp3170.demos.week9.cameras.Camera;

public class Cylinder extends SceneObject {

	private static final int NSIDES = 12;
	private final static String NORMAL_VERTEX = "normalVertex.glsl";
	private final static String NORMAL_FRAGMENT = "normalFragment.glsl";
	private final static String LIGHT_VERTEX = "lightVertex.glsl";
	private final static String LIGHT_FRAGMENT = "lightFragment.glsl";

	private Shader normalShader;
	private Shader lightShader;
	private Shader shader;
	private boolean showNormals = false;

	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector4f[] normals;
	private int normalBuffer;
	private int[] indices;
	private int indexBuffer;

	private Matrix4f normalMatrix = new Matrix4f();
	private List<Integer> topIndices;
	private List<Integer> bottomIndices;
	private List<Integer> sideIndices;

	private Vector3f diffuseMaterial = new Vector3f(1,0,0);		// default to red
	private Vector3f specularMaterial = new Vector3f(1,1,1);	// default to white
	private float specularity = 10;

	public Cylinder() {
		normalShader = ShaderLibrary.instance.compileShader(NORMAL_VERTEX, NORMAL_FRAGMENT);
		// turn off checking for missing attributes / uniforms
		// when we are debugging
		normalShader.setStrict(showNormals);

		lightShader = ShaderLibrary.instance.compileShader(LIGHT_VERTEX, LIGHT_FRAGMENT);
		shader = lightShader;

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

		vertices = new Vector4f[NSIDES * 4 + 2];
		normals = new Vector4f[NSIDES * 4 + 2];

		int kv = 0;
		int kn = 0;

		Vector4f nUp = new Vector4f(0,1,0,0);
		Vector4f nDown = new Vector4f(0,-1,0,0);

		vertices[kv++] = new Vector4f(0,0,0,1);
		normals[kn++] = nDown;

		vertices[kv++] = new Vector4f(0,1,0,1);
		normals[kn++] = nUp;

		// form the bottom and top edges by rotating point P = (1,0,0) about the y axis
		Vector4f p = new Vector4f(1,0,0,1);
		Vector4f n = new Vector4f(1,0,0,0);

		Matrix4f rotate = new Matrix4f();
		Matrix4f translate = new Matrix4f().translation(0,1,0);

		topIndices = new ArrayList<>();
		bottomIndices = new ArrayList<>();
		sideIndices = new ArrayList<>();

		for (int i = 0; i < NSIDES; i++) {
			float angle = i * TAU / NSIDES;
			rotate.rotationY(angle);

			Vector4f vb = p.mul(rotate, new Vector4f());  // vb = R(p)
			Vector4f vt = p.mul(rotate, new Vector4f()).mul(translate);  // vt = T(R(p))
			Vector4f ni = n.mul(rotate, new Vector4f());   // ni = R(n)

			// bottom
			bottomIndices.add(kv);
			vertices[kv++] = vb;
			normals[kn++] = nDown;

			// top
			topIndices.add(kv);
			vertices[kv++] = vt;
			normals[kn++] = nUp;

			// side
			sideIndices.add(kv);
			vertices[kv++] = vb;
			normals[kn++] = ni;
			sideIndices.add(kv);
			vertices[kv++] = vt;
			normals[kn++] = ni;
		}

		vertexBuffer = GLBuffers.createBuffer(vertices);
		normalBuffer = GLBuffers.createBuffer(normals);
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

	private static final float ROTATION_SPEED = TAU/4;
	private static final float SCALE_SPEED = 1.1f;
	private static final float SPECULARITY_SPEED = 2f;

	public void update(InputManager input, float dt) {
		if (input.isKeyDown(GLFW_KEY_Z)) {
			getMatrix().rotateLocalY(-ROTATION_SPEED * dt);
		}
		if (input.isKeyDown(GLFW_KEY_X)) {
			getMatrix().rotateLocalY(ROTATION_SPEED * dt);
		}
		float s = (float) Math.pow(SCALE_SPEED, dt);
		if (input.isKeyDown(GLFW_KEY_C)) {
			getMatrix().scale(s,1,1/s);
		}
		if (input.isKeyDown(GLFW_KEY_V)) {
			getMatrix().scale(1/s,1,s);
		}
		if (input.isKeyDown(GLFW_KEY_COMMA)) {
			specularity *= Math.pow(SPECULARITY_SPEED, dt);
		}
		if (input.isKeyDown(GLFW_KEY_PERIOD)) {
			specularity /= Math.pow(SPECULARITY_SPEED, dt);
		}


		if (input.wasKeyPressed(GLFW_KEY_N)) {
			showNormals = !showNormals;
			if (showNormals) {
				shader = normalShader;
			}
			else {
				shader = lightShader;
			}
		}
	}

	private Matrix4f modelMatrix = new Matrix4f();

	private Vector4f lightDirection = new Vector4f();
	private Vector3f lightIntensity = new Vector3f();
	private Vector3f ambientIntensity = new Vector3f();
	private Vector4f viewDirection = new Vector4f();

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
		shader.setUniform("u_viewDirection", camera.getViewVector(viewDirection));

		// light
		ILight light = Scene.theScene.getLight();
		shader.setUniform("u_lightDirection", light.getSourceVector(lightDirection));
		shader.setUniform("u_intensity", light.getIntensity(lightIntensity));
		shader.setUniform("u_ambientIntensity", light.getAmbient(ambientIntensity));

		// materials
		shader.setUniform("u_diffuseMaterial", diffuseMaterial);
		shader.setUniform("u_specularMaterial", specularMaterial);
		shader.setUniform("u_specularity", specularity);

		// vertex attributes
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_normal", normalBuffer);

		// draw call
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

	}


}
