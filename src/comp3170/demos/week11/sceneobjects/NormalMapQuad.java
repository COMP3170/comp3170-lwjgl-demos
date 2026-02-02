package comp3170.demos.week11.sceneobjects;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_N;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.io.IOException;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.TextureLibrary;
import comp3170.demos.common.lights.ILight;
import comp3170.demos.week11.demos.normalMap.NormalMapScene;

public class NormalMapQuad extends SceneObject {

	static final private String NORMAL_MAP_VERTEX_SHADER = "normalMapVertex.glsl";
	static final private String NORMAL_MAP_FRAGMENT_SHADER = "normalMapFragment.glsl";
	static final private String DIFFUSE_VERTEX_SHADER = "diffuseVertex.glsl";
	static final private String DIFFUSE_FRAGMENT_SHADER = "diffuseFragment.glsl";

	private Shader normalMapShader;
	private Shader diffuseShader;

	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector3f[] normals;
	private int normalBuffer;
	private Vector3f[] tangents;
	private int tangentBuffer;
	private Vector3f[] bitangents;
	private int bitangentBuffer;
	private Vector2f[] uvs;
	private int uvBuffer;

	static final private String DIFFUSE_TEXTURE = "brick-diffuse.png";
	static final private String NORMAL_TEXTURE = "brick-normals.png";
	private int diffuseTexture;
	private int normalTexture;

	private boolean isNormalMapEnabled = true;
	private boolean isNormalMapVisible = false;

	public NormalMapQuad() {
		normalMapShader = ShaderLibrary.instance.compileShader(NORMAL_MAP_VERTEX_SHADER, NORMAL_MAP_FRAGMENT_SHADER);
		diffuseShader = ShaderLibrary.instance.compileShader(DIFFUSE_VERTEX_SHADER, DIFFUSE_FRAGMENT_SHADER);
		diffuseShader.setStrict(false);
		createQuad();
		loadTextures();
		createTangentsBuffer();
	}

	private void createQuad() {

		// @formatter:off

		//  2---3
		//  |\  |    y
		//  | * |    |
		//  |  \|    +--x
		//  0---1   /
		//         z (out)

		vertices = new Vector4f[] {
			new Vector4f(-1, -1, 0, 1),	// 0
			new Vector4f( 1, -1, 0, 1), // 1
			new Vector4f(-1,  1, 0, 1),	// 2

			new Vector4f( 1,  1, 0, 1), // 3
			new Vector4f(-1,  1, 0, 1),	// 2
			new Vector4f( 1, -1, 0, 1), // 1
		};

		vertexBuffer = GLBuffers.createBuffer(vertices);

		normals = new Vector3f[] {
			new Vector3f(0, 0, 1),
			new Vector3f(0, 0, 1),
			new Vector3f(0, 0, 1),

			new Vector3f(0, 0, 1),
			new Vector3f(0, 0, 1),
			new Vector3f(0, 0, 1),
		};

		normalBuffer = GLBuffers.createBuffer(normals);

		uvs = new Vector2f[] {
			new Vector2f(0, 0),
			new Vector2f(1, 0),
			new Vector2f(0, 1),

			new Vector2f(1, 1),
			new Vector2f(0, 1),
			new Vector2f(1, 0),
		};

		uvBuffer = GLBuffers.createBuffer(uvs);

		// @formatter:on

	}

	private void loadTextures() {
		try {
			diffuseTexture = TextureLibrary.instance.loadTexture(DIFFUSE_TEXTURE);
			normalTexture = TextureLibrary.instance.loadTexture(NORMAL_TEXTURE);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (OpenGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		glBindTexture(GL_TEXTURE_2D, diffuseTexture);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glGenerateMipmap(GL_TEXTURE_2D);

		glBindTexture(GL_TEXTURE_2D, normalTexture);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glGenerateMipmap(GL_TEXTURE_2D);

	}

	private void createTangentsBuffer() {
		tangents = new Vector3f[6];
		bitangents = new Vector3f[6];

		int k = 0;
		Matrix3f matrix;
		Vector3f t, b;
		matrix = calculateTangentMatrix(0);
		t = matrix.getColumn(0, new Vector3f()).normalize();
		b = matrix.getColumn(1, new Vector3f()).normalize();
		tangents[0] = t;
		bitangents[0] = b;
		tangents[1] = t;
		bitangents[1] = b;
		tangents[2] = t;
		bitangents[2] = b;

		matrix = calculateTangentMatrix(1);
		t = matrix.getColumn(0, new Vector3f()).normalize();
		b = matrix.getColumn(1, new Vector3f()).normalize();
		tangents[3] = t;
		bitangents[3] = b;
		tangents[4] = t;
		bitangents[4] = b;
		tangents[5] = t;
		bitangents[5] = b;

		tangentBuffer = GLBuffers.createBuffer(tangents);
		bitangentBuffer = GLBuffers.createBuffer(bitangents);

	}

	Vector3f one = new Vector3f(0, 0, 1);
	Vector3f dP10 = new Vector3f();
	Vector3f dP20 = new Vector3f();
	Vector3f normal = new Vector3f();
	Vector3f dUV10 = new Vector3f();
	Vector3f dUV20 = new Vector3f();

	Matrix3f edgeMatrix = new Matrix3f();
	Matrix3f uvMatrix = new Matrix3f();

	private Matrix3f calculateTangentMatrix(int triangle) {

		Vector4f p0, p1, p2;
		Vector2f uv0, uv1, uv2;

		p0 = vertices[triangle * 3];
		p1 = vertices[triangle * 3 + 1];
		p2 = vertices[triangle * 3 + 2];

		uv0 = uvs[triangle * 3];
		uv1 = uvs[triangle * 3 + 1];
		uv2 = uvs[triangle * 3 + 2];

		// the JOML class Matrix3x2f doesn't suit our needs, so pad everything to 3x3
		dP10.set(p1.x - p0.x, p1.y - p0.y, p1.z - p0.z); // dP10 = P1 - P0
		dP20.set(p2.x - p0.x, p2.y - p0.y, p2.z - p0.z); // dP10 = P2 - P0

		// [ .. .. 0 ]
		// E = [ dP10 dP20 0 ]
		// [ .. .. 1 ]
		edgeMatrix.identity();
		edgeMatrix.setColumn(0, dP10);
		edgeMatrix.setColumn(1, dP20);

		dUV10.set(uv1.x - uv0.x, uv1.y - uv0.y, 0); // dUV10 = UV1 - UV0
		dUV20.set(uv2.x - uv0.x, uv2.y - uv0.y, 0); // dUV20 = UV1 - UV0
		// [ ... ... 0 ]
		// U = [ dUV10 dUV20 0 ]
		// [ ... ... 1 ]
		uvMatrix.identity();
		uvMatrix.setColumn(0, dUV10);
		uvMatrix.setColumn(1, dUV20);

		//
		// T = E U^-1
		//
		uvMatrix.invert();
		Matrix3f tangentMatrix = edgeMatrix.mul(uvMatrix, new Matrix3f());

		return tangentMatrix;
	}

	private static final float ROTATION_SPEED = TAU / 4;

	public void update(float dt, InputManager input) {
		if (input.isKeyDown(GLFW_KEY_Z)) {
			getMatrix().rotateY(-ROTATION_SPEED * dt);
		}
		if (input.isKeyDown(GLFW_KEY_X)) {
			getMatrix().rotateY(ROTATION_SPEED * dt);
		}

		if (input.wasKeyPressed(GLFW_KEY_SPACE)) {
			isNormalMapEnabled = !isNormalMapEnabled;
		}
		if (input.wasKeyPressed(GLFW_KEY_N)) {
			isNormalMapVisible = !isNormalMapVisible;
		}
	}

	private Matrix4f modelMatrix = new Matrix4f();
	private Matrix3f normalMatrix = new Matrix3f();

	private Vector4f lightDirection = new Vector4f();
	private Vector3f lightIntensity = new Vector3f();
	private Vector3f ambientIntensity = new Vector3f();

	@Override
	public void drawSelf(Matrix4f mvpMatrix) {
		Shader shader = isNormalMapEnabled ? normalMapShader : diffuseShader;
		shader.enable();

		// matrices
		getModelToWorldMatrix(modelMatrix);
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_normalMatrix", modelMatrix.normal(normalMatrix));

		// vertex attributes
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_normal", normalBuffer);
		shader.setAttribute("a_tangent", tangentBuffer);
		shader.setAttribute("a_bitangent", bitangentBuffer);
		shader.setAttribute("a_texcoord", uvBuffer);

		// light
		ILight light = NormalMapScene.theScene.getLight();
		shader.setUniform("u_lightDirection", light.getSourceVector(lightDirection));
		shader.setUniform("u_intensity", light.getIntensity(lightIntensity));
		shader.setUniform("u_ambientIntensity", light.getAmbient(ambientIntensity));

		// textures
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, isNormalMapVisible ? normalTexture : diffuseTexture);
		shader.setUniform("u_diffuseTexture", 0);
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, normalTexture);
		shader.setUniform("u_normalTexture", 1);

		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDrawArrays(GL_TRIANGLES, 0, vertices.length);
	}

}
