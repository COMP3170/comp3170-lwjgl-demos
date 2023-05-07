package comp3170.demos.week12.sceneobjects;

import static comp3170.Math.TAU;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week10.sceneobjects.Scene;
import comp3170.demos.week12.cameras.MirrorCamera;
import comp3170.demos.week12.shaders.ShaderLibrary;
import comp3170.demos.week12.textures.TextureLibrary;

public class Mirror extends SceneObject {

	static final private String VERTEX_SHADER = "textureVertex.glsl";
	static final private String FRAGMENT_SHADER = "textureFragment.glsl";
	private Shader shader;

	static final private int TEXTURE_WIDTH = 1024;
	static final private int TEXTURE_HEIGHT = 1024;
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector2f[] uvs;
	private int uvBuffer;
	private int[] indices;
	private int indexBuffer;
	private int renderTexture;
	private MirrorCamera camera;

	public Mirror() {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		createQuad();
		createFrame();
		
		camera = new MirrorCamera(this, Scene.theScene.getCamera());
		
		renderTexture = TextureLibrary.createRenderTexture(TEXTURE_WIDTH, TEXTURE_HEIGHT, GL_RGB);
	}

	private void createQuad() {
		vertices = new Vector4f[] {
			new Vector4f(-1, -1, 0, 1), 
			new Vector4f(-1, 1, 0, 1), 
			new Vector4f(1, -1, 0, 1),
			new Vector4f(1, 1, 0, 1), 
		};

		vertexBuffer = GLBuffers.createBuffer(vertices);

		uvs = new Vector2f[] { 
			new Vector2f(0, 0), 
			new Vector2f(0, 1), 
			new Vector2f(1, 0), 
			new Vector2f(1, 1), 
		};

		uvBuffer = GLBuffers.createBuffer(uvs);

		indices = new int[] { 
			0, 1, 2, 
			3, 2, 1, 
		};
		indexBuffer = GLBuffers.createIndexBuffer(indices);

	}

	private void createFrame() {
		for (int i = 0; i < 4; i++) {
			Frame frame = new Frame();
			frame.setParent(this);
			frame.getMatrix().rotationZ(TAU * i / 4);
		}		
	}
	
	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_texcoord", uvBuffer);
		
		// textures
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, renderTexture);
		shader.setUniform("u_texture", 0);

		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

	}

	public void update(InputManager input, float deltaTime) {
		// TODO Auto-generated method stub
		
	}
}
