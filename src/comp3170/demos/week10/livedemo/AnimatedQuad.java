package comp3170.demos.week10.livedemo;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTexParameterfv;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL12.GL_REPEAT;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL14.GL_LINEAR;
import static org.lwjgl.opengl.GL14.GL_NEAREST;
import static org.lwjgl.opengl.GL13.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL13.GL_LINEAR_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_BORDER_COLOR;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL40.glGenerateMipmap;
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
import comp3170.Shader;
import comp3170.SceneObject;
import comp3170.demos.week10.shaders.ShaderLibrary;
import comp3170.demos.week10.textures.TextureLibrary;

public class AnimatedQuad extends SceneObject {

	static final private String VERTEX_SHADER = "animatedVertex.glsl";
	static final private String FRAGMENT_SHADER = "animatedFragment.glsl";
	static final private String TEXTURE = "colours.png";
	
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector2f[] uvs;
	private int uvBuffer;
	private int[] indices;
	private int indexBuffer;
	private int textureID;
	
	private long startTime;

	public AnimatedQuad() {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		startTime = System.currentTimeMillis(); 
		
		//  1---3
		//  |\  |   y
		//  | * |   |
		//  |  \|   +--x
		//  0---2
		
		vertices = new Vector4f[] {
			new Vector4f(-1, -1, 0, 1),
			new Vector4f(-1,  1, 0, 1),
			new Vector4f( 1, -1, 0, 1),
			new Vector4f( 1,  1, 0, 1),
		};
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		
		uvs = new Vector2f[] {
			new Vector2f(0.1f, 0),
			new Vector2f(0.1f, 1),
			new Vector2f(1.1f, 0),
			new Vector2f(1.1f, 1),
		};
				
		uvBuffer = GLBuffers.createBuffer(uvs);
		
		indices = new int[] {
			0, 1, 3,
			3, 2, 0,
		};
		indexBuffer = GLBuffers.createIndexBuffer(indices);
		
		try {
			textureID = TextureLibrary.loadTexture(TEXTURE);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (OpenGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Texture Settings
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D,textureID);
		
		//Wrap modes
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); //S is U
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT); //T is V
		
		//Filtering
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		//MipMaps
		glGenerateMipmap(GL_TEXTURE_2D);
	}	
	
	@Override
	public void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();

		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_texcoord", uvBuffer);

		float time = (System.currentTimeMillis() - startTime) / 1000f; 
		shader.setUniform("u_time", time);
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D,textureID);
		shader.setUniform("u_texture", 0);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

	}

	private final static float SPEED = 0.1f;
	
	public void update(InputManager input, float deltaTime) {

//		for (int i = 0; i < uvs.length; i++) {
//			uvs[i].x += SPEED * deltaTime;
//		}
//					
//		GLBuffers.updateBuffer(uvBuffer, uvs);
	}

}
