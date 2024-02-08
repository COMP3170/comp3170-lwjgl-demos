package comp3170.demos.week11.livedemo;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week11.cameras.Camera;
import comp3170.demos.week11.shaders.ShaderLibrary;
import comp3170.demos.week11.textures.TextureLibrary;

public class TexturedCube extends SceneObject {
	
	private static final String VERTEX_SHADER = "lightingVertex.glsl";
	private static final String FRAGMENT_SHADER = "lightingFragment.glsl";
	private static final String TEXTURE_FILE = "cat.jpg";
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector2f[] uvs;
	private int uvBuffer;
	private Vector4f[] normals;
	private int normalBuffer;
	private int texture;

	public TexturedCube() {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		shader.setStrict(false);
		
		//          6-----7
		//         /|    /|
		//        / |   / |
		//       1-----0  |     y    RHS coords
		//       |  |  |  |     | 
		//       |  5--|--4     +--x
		//       | /   | /     /
		//       |/    |/     z
		//       2-----3
		
		createVertexBuffer();
		createUVBuffer();
		createNormalBuffer();
		
		try {
			texture = TextureLibrary.loadTexture(TEXTURE_FILE);
		} catch (IOException | OpenGLException e) {
			e.printStackTrace();
		}
	}

	private void createVertexBuffer() {
		vertices = new Vector4f[] {
			// front
			new Vector4f( 1, 1, 1, 1), // 0
			new Vector4f(-1, 1, 1, 1), // 1
			new Vector4f(-1,-1, 1, 1), // 2

			new Vector4f(-1,-1, 1, 1), // 2
			new Vector4f( 1,-1, 1, 1), // 3
			new Vector4f( 1, 1, 1, 1), // 0
			
			// back
			new Vector4f( 1,-1,-1, 1), // 4
			new Vector4f(-1,-1,-1, 1), // 5
			new Vector4f(-1, 1,-1, 1), // 6

			new Vector4f(-1, 1,-1, 1), // 6
			new Vector4f( 1, 1,-1, 1), // 7
			new Vector4f( 1,-1,-1, 1), // 4
			
			// top
			new Vector4f( 1, 1, 1, 1), // 0
			new Vector4f( 1, 1,-1, 1), // 7
			new Vector4f(-1, 1,-1, 1), // 6

			new Vector4f(-1, 1,-1, 1), // 6
			new Vector4f(-1, 1, 1, 1), // 1
			new Vector4f( 1, 1, 1, 1), // 0
			
			// bottom 
			new Vector4f(-1,-1, 1, 1), // 2
			new Vector4f(-1,-1,-1, 1), // 5
			new Vector4f( 1,-1,-1, 1), // 4

			new Vector4f( 1,-1,-1, 1), // 4
			new Vector4f( 1,-1, 1, 1), // 3
			new Vector4f(-1,-1, 1, 1), // 2
			
			// left
			new Vector4f(-1, 1, 1, 1), // 1
			new Vector4f(-1, 1,-1, 1), // 6
			new Vector4f(-1,-1,-1, 1), // 5

			new Vector4f(-1,-1,-1, 1), // 5
			new Vector4f(-1,-1, 1, 1), // 2
			new Vector4f(-1, 1, 1, 1), // 1
			
			// right
			new Vector4f( 1, 1,-1, 1), // 7
			new Vector4f( 1, 1, 1, 1), // 0
			new Vector4f( 1,-1, 1, 1), // 3

			new Vector4f( 1,-1, 1, 1), // 3
			new Vector4f( 1,-1,-1, 1), // 4
			new Vector4f( 1, 1,-1, 1), // 7
		};
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
	}
	
	private void createUVBuffer() {
		uvs = new Vector2f[] {
			// front
			new Vector2f(1, 1), 
			new Vector2f(0, 1), 
			new Vector2f(0, 0), 
	
			new Vector2f(0, 0), 
			new Vector2f(1, 0), 
			new Vector2f(1, 1), 
	
			// back
			new Vector2f(1, 1), 
			new Vector2f(0, 1), 
			new Vector2f(0, 0), 
	
			new Vector2f(0, 0), 
			new Vector2f(1, 0), 
			new Vector2f(1, 1), 
	
			// top
			new Vector2f(1, 1), 
			new Vector2f(0, 1), 
			new Vector2f(0, 0), 
	
			new Vector2f(0, 0), 
			new Vector2f(1, 0), 
			new Vector2f(1, 1), 
	
			// bottom
			new Vector2f(1, 1), 
			new Vector2f(0, 1), 
			new Vector2f(0, 0), 
	
			new Vector2f(0, 0), 
			new Vector2f(1, 0), 
			new Vector2f(1, 1), 
	
			// left
			new Vector2f(1, 1), 
			new Vector2f(0, 1), 
			new Vector2f(0, 0), 
	
			new Vector2f(0, 0), 
			new Vector2f(1, 0), 
			new Vector2f(1, 1), 
	
			// right
			new Vector2f(1, 1), 
			new Vector2f(0, 1), 
			new Vector2f(0, 0), 
	
			new Vector2f(0, 0), 
			new Vector2f(1, 0), 
			new Vector2f(1, 1), 
	
		};
		uvBuffer = GLBuffers.createBuffer(uvs);
	}

	private void createNormalBuffer() {
		normals = new Vector4f[] {
			// front
			new Vector4f( 0, 0, 1, 0), // 0
			new Vector4f( 0, 0, 1, 0), // 0
			new Vector4f( 0, 0, 1, 0), // 0

			new Vector4f( 0, 0, 1, 0), // 0
			new Vector4f( 0, 0, 1, 0), // 0
			new Vector4f( 0, 0, 1, 0), // 0
			
			// back
			new Vector4f( 0, 0, -1, 0), // 0
			new Vector4f( 0, 0, -1, 0), // 0
			new Vector4f( 0, 0, -1, 0), // 0

			new Vector4f( 0, 0, -1, 0), // 0
			new Vector4f( 0, 0, -1, 0), // 0
			new Vector4f( 0, 0, -1, 0), // 0
			
			// top
			new Vector4f( 0, 1, 0, 0), // 0
			new Vector4f( 0, 1, 0, 0), // 0
			new Vector4f( 0, 1, 0, 0), // 0

			new Vector4f( 0, 1, 0, 0), // 0
			new Vector4f( 0, 1, 0, 0), // 0
			new Vector4f( 0, 1, 0, 0), // 0
			
			// bottom 
			new Vector4f( 0, -1, 0, 0), // 0
			new Vector4f( 0, -1, 0, 0), // 0
			new Vector4f( 0, -1, 0, 0), // 0

			new Vector4f( 0, -1, 0, 0), // 0
			new Vector4f( 0, -1, 0, 0), // 0
			new Vector4f( 0, -1, 0, 0), // 0
			
			// left
			new Vector4f(-1, 0, 0, 0), // 1
			new Vector4f(-1, 0, 0, 0), // 1
			new Vector4f(-1, 0, 0, 0), // 1

			new Vector4f(-1, 0, 0, 0), // 1
			new Vector4f(-1, 0, 0, 0), // 1
			new Vector4f(-1, 0, 0, 0), // 1
			
			// right
			new Vector4f( 1, 0, 0, 0), // 1
			new Vector4f( 1, 0, 0, 0), // 1
			new Vector4f( 1, 0, 0, 0), // 1

			new Vector4f( 1, 0, 0, 0), // 1
			new Vector4f( 1, 0, 0, 0), // 1
			new Vector4f( 1, 0, 0, 0), // 1
		};
		
		normalBuffer = GLBuffers.createBuffer(normals);
	}

	private final static float ROTATION_SPEED = TAU / 6;
	
	public void update(InputManager input, float deltaTime) {
		if (input.isKeyDown(GLFW_KEY_A)) {
			getMatrix().rotateY(ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			getMatrix().rotateY(-ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_W)) {
			getMatrix().rotateX(ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_S)) {
			getMatrix().rotateX(-ROTATION_SPEED * deltaTime);
		}

	}
		
	private static final float GAMMA = 2.2f;
	private static final float SPECULARITY = 100;
	
	private Vector3f ambientIntensity = new Vector3f();
	private Vector3f lightIntensity = new Vector3f();
	private Vector4f lightDirection = new Vector4f();
	private Vector4f viewDirection = new Vector4f();
	
	private Matrix4f modelMatrix = new Matrix4f();
	private Matrix4f normalMatrix = new Matrix4f();
	
	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		
		// matrices
		getModelToWorldMatrix(modelMatrix);
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_normalMatrix", modelMatrix.normal(normalMatrix));
		
		// vertex attributes
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_texcoord", uvBuffer);
		shader.setAttribute("a_normal", normalBuffer);

		// lighting
		DirectionalLight light = Scene.theScene.getLight();
		shader.setUniform("u_gamma", GAMMA);
		shader.setUniform("u_ambient", light.getAmbient(ambientIntensity));
		shader.setUniform("u_intensity", light.getIntensity(lightIntensity));
		shader.setUniform("u_lightDirection", light.getDirection(lightDirection));
		
		// camera
		Camera camera = Scene.theScene.getCamera();
		shader.setUniform("u_viewDirection", camera.getViewVector(viewDirection));
		shader.setUniform("u_specularity", SPECULARITY);
		
		// textures
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture);
		shader.setUniform("u_texture", 0);

		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDrawArrays(GL_TRIANGLES, 0, vertices.length);
	}
}
