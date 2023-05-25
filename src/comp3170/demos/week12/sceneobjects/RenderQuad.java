package comp3170.demos.week12.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;

public class RenderQuad extends SceneObject {

	private Shader shader = null;

	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector2f[] uvs;
	private int uvBuffer;
	private int[] indices;
	private int indexBuffer;
	private int[] textures;
	
	public RenderQuad() {
		//  2----3
		//  |\   |
		//  | \  |
		//  |  \ |
		//  |   \|
		//  0----1

		vertices = new Vector4f[] {
			new Vector4f(-1, -1, 0, 1),
			new Vector4f( 1, -1, 0, 1),
			new Vector4f(-1,  1, 0, 1),
			new Vector4f( 1,  1, 0, 1),
		};
		
		vertexBuffer = GLBuffers.createBuffer(vertices);

		uvs = new Vector2f[] {
			new Vector2f(0,0),
			new Vector2f(1,0),
			new Vector2f(0,1),
			new Vector2f(1,1),
		};
			
		uvBuffer = GLBuffers.createBuffer(uvs);

		indices = new int[] {
			0,1,2,
			3,2,1,
		};
		indexBuffer = GLBuffers.createIndexBuffer(indices);
		
		textures = new int[2];
	}
	
	public void setShader(Shader shader) {
		this.shader = shader;
	}

	public void setTexture(int id, int texture)  {
		textures[id] = texture;
	}
	
	private int[] textureResolution = new int[] {100, 100};
	
	@Override
	protected void drawSelf(Matrix4f matrix) {
		shader.enable();
		
		// vertex attributes
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_texcoord", uvBuffer);
		
		// textures
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textures[0]);
		shader.setUniform("u_texture0", 0);

		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, textures[1]);
		shader.setUniform("u_texture1", 1);

		// toon shading
		shader.setUniform("u_buckets", 4f);
		shader.setUniform("u_textureResolution", textureResolution);
				
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);		
	}
	
}
