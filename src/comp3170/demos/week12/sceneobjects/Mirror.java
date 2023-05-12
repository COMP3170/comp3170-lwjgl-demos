package comp3170.demos.week12.sceneobjects;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week12.cameras.Camera;
import comp3170.demos.week12.cameras.MirrorCamera;
import comp3170.demos.week12.shaders.ShaderLibrary;
import comp3170.demos.week12.textures.TextureLibrary;

public class Mirror extends SceneObject {

	static final private String VERTEX_SHADER = "textureVertex.glsl";
	static final private String FRAGMENT_SHADER = "textureFragment.glsl";
	static final private String OUTLINE_VERTEX_SHADER = "simpleVertex.glsl";
	static final private String OUTLINE_FRAGMENT_SHADER = "simpleFragment.glsl";
	private Shader shader;
	private Shader outlineShader;

	static final private int TEXTURE_WIDTH = 1024;
	static final private int TEXTURE_HEIGHT = 1024;
	static final private String DEBUG_TEXTURE = "colours.png";
	private int renderTexture;
	private int debugTexture;
	private boolean isDebugTexture = false;
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector2f[] uvs;
	private int uvBuffer;
	private int[] indices;
	private int indexBuffer;

	private MirrorCamera camera;
	private int[] outline;
	private int outlineBuffer;
	private Vector4f outlineColour = new Vector4f(1,1,0,1); // yellow
	
	public Mirror(Camera mainCamera) {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		outlineShader = ShaderLibrary.compileShader(OUTLINE_VERTEX_SHADER, OUTLINE_FRAGMENT_SHADER);
		createQuad();
		
		camera = new MirrorCamera(this, mainCamera);
		camera.setParent(this);
		
		renderTexture = TextureLibrary.createRenderTexture(TEXTURE_WIDTH, TEXTURE_HEIGHT, GL_RGB);
		
		try {
			debugTexture = TextureLibrary.loadTexture(DEBUG_TEXTURE);
		} catch (IOException | OpenGLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public MirrorCamera getCamera() {
		return camera;
	}

	public Vector4f getVertex(int i, Vector4f dest) {
		return dest.set(vertices[i]);
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

		outline = new int[] { 
			0, 1, 
			1, 3, 
			3, 2, 
			2, 0, 
		};
		outlineBuffer = GLBuffers.createIndexBuffer(outline);
	}

	private static final float ROTATION_SPEED = TAU/4;
	
	private Matrix4f modelMatrix = new Matrix4f();
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Vector4f position = new Vector4f();
	
	public void update(InputManager input, float deltaTime) {

		if (input.isKeyDown(GLFW_KEY_A)) {
			getMatrix().rotateY(ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			getMatrix().rotateY(-ROTATION_SPEED * deltaTime);
		}
		
		if (input.wasKeyPressed(GLFW_KEY_SPACE)) {
			isDebugTexture = !isDebugTexture;
		}
		
		camera.update();		
		getModelToWorldMatrix(modelMatrix);
		camera.getViewMatrix(viewMatrix);
		camera.getProjectionMatrix(projectionMatrix);

		for (int i = 0; i < vertices.length; i++) {
			position.set(vertices[i]).mul(modelMatrix).mul(viewMatrix).mul(projectionMatrix);
			uvs[i].x = (position.x + 1) / 2;
			uvs[i].y = (position.y + 1) / 2;
		}	
		
		GLBuffers.updateBuffer(uvBuffer, uvs);
	}

	
	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_texcoord", uvBuffer);
		
		// textures
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, isDebugTexture ? debugTexture : renderTexture);
		shader.setUniform("u_texture", 0);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
		
		outlineShader.enable();
		outlineShader.setUniform("u_mvpMatrix", mvpMatrix);
		outlineShader.setAttribute("a_position", vertexBuffer);
		outlineShader.setUniform("u_colour", outlineColour);
				
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, outlineBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDrawElements(GL_LINES, outline.length, GL_UNSIGNED_INT, 0);

	}


}
