package comp3170.demos.week12.sceneobjects;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
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

import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.TextureLibrary;

public class Skybox extends SceneObject {
	
	static final private String VERTEX_SHADER = "skyboxVertex.glsl";
	static final private String FRAGMENT_SHADER = "skyboxFragment.glsl";
		
	private static final String[] TEXTURE_FILES = new String[] { 
		"X.png", "X.png", "Y.png", "Y.png", "Z.png", "Z.png", 
	};
	
	// https://opengameart.org/content/cloudy-skyboxes
	private static final String[] SKYBOX_TEXTURE_FILES = new String[] { 
		"bluecloud_lf.jpg",
		"bluecloud_rt.jpg",
		"bluecloud_up.jpg",
		"bluecloud_dn.jpg",
		"bluecloud_ft.jpg",
		"bluecloud_bk.jpg",
	};

	
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private int texture;
	private int skyboxTexture;
	private boolean isSkyboxEnabled = false;

	
	public Skybox() {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		createCube();
		loadTextures();
	}
	
	private void createCube() {
		//          6-----7
		//         /|    /|
		//        / |   / |
		//       1-----0  |     y    RHS coords
		//       |  |  |  |     | 
		//       |  5--|--4     +--x
		//       | /   | /     /
		//       |/    |/     z
		//       2-----3
		
		vertices = new Vector4f[] {
			new Vector4f( 1, 1, 1, 1),
			new Vector4f(-1, 1, 1, 1),
			new Vector4f(-1,-1, 1, 1),
			new Vector4f( 1,-1, 1, 1),
			new Vector4f( 1,-1,-1, 1),
			new Vector4f(-1,-1,-1, 1),
			new Vector4f(-1, 1,-1, 1),
			new Vector4f( 1, 1,-1, 1),
		};
		
		vertexBuffer = GLBuffers.createBuffer(vertices);

		// indices for the lines forming each face
		// faces are facing inwards, as we will be viewing the
		// cube from the inside

		indices = new int[] {
			// front
			2, 1, 0,
			0, 3, 2,
			
			// back
			6, 5, 4,
			4, 7, 6,
			
			// top
			6, 7, 0,
			0, 1, 6,
			
			// bottom 
			4, 5, 2,
			2, 3, 4,
			
			// left
			6, 1, 2,
			2, 5, 6,
			
			// right
			3, 0, 7,
			7, 4, 3,			
		};
		
		indexBuffer = GLBuffers.createIndexBuffer(indices);
		
	}

	private void loadTextures() {
		try {
			texture = TextureLibrary.instance.loadCubemap(TEXTURE_FILES);
			skyboxTexture = TextureLibrary.instance.loadCubemap(SKYBOX_TEXTURE_FILES);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (OpenGLException e) {
			e.printStackTrace();
			System.exit(1);
		}		
	}

	public int getCubemap() {
		return isSkyboxEnabled ? skyboxTexture : texture;
	}
	
	public void update(InputManager input, float deltaTime) {
		if (input.wasKeyPressed(GLFW_KEY_SPACE)) {
			isSkyboxEnabled = !isSkyboxEnabled;
		}

	}

	
	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, isSkyboxEnabled ? skyboxTexture : texture);
		shader.setUniform("u_cubemap", 0);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);		

	}

}
