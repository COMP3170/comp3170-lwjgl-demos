package comp3170.demos.week1;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Scene {

	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private Vector4f[] vertices;
	private int vertexBuffer;
	private Shader shader;
	private int screenWidth;
	private int screenHeight;


	public Scene(int width, int height) {
		
		screenWidth = width;
		screenHeight = height;
		
		// create the shape

		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// vertices of a square as (x,y) pairs
		// @formatter:off

		vertices = new Vector4f[] {
			new Vector4f(1.0f, 1.0f, 0.0f, 1.0f),
			new Vector4f(-1.0f, 1.0f, 0.0f, 1.0f),
			new Vector4f(-1.0f, -1.0f, 0.0f, 1.0f),
			
			new Vector4f(-1.0f, -1.0f, 0.0f, 1.0f),
			new Vector4f(1.0f, -1.0f, 0.0f, 1.0f),
			new Vector4f(1.0f, 1.0f, 0.0f, 1.0f),
		};
		// @formatter:on

		// copy the data into a Vertex Buffer Object in graphics memory
		vertexBuffer = GLBuffers.createBuffer(vertices);

	}


	public void draw() {
		// activate the shader
		shader.enable();

		// connect the vertex buffer to the a_position attribute
		shader.setAttribute("a_position", vertexBuffer);

	    // write the colour value into the u_colour uniform 
	    Vector3f colour = new Vector3f(1.0f, 0.0f, 0.0f);	    
        shader.setUniform("u_colour", colour);
        
        Vector2f screenSize = new Vector2f(screenWidth, screenHeight);
        shader.setUniform("u_screenSize", screenSize);	

		// mode = GL_TRIANGLES
		// starting offset = 0
		// number of elements = 6
		glDrawArrays(GL_TRIANGLES, 0, vertices.length);
	}
	
}
