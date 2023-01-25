
package comp3170.demos.week2;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.GL_FLOAT_VEC2;
import static org.lwjgl.opengl.GL20.GL_FLOAT_VEC3;

import java.io.File;
import java.io.IOException;

import comp3170.GLBuffers;
import comp3170.IWindowListener;
import comp3170.OpenGLException;
import comp3170.Shader;
import comp3170.Window;

public class Week2 implements IWindowListener {

	public double TAU = 2 * Math.PI;		// https://tauday.com/tau-manifesto
	
	private int width = 800;
	private int height = 800;

	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/demos/week2"); 
	final private String VERTEX_SHADER = "colour_vertex.glsl";
	final private String FRAGMENT_SHADER = "colour_fragment.glsl";

	private float[] vertices;
	private int vertexBuffer;
	private float[] colours;
	private int colourBuffer;

	private int[] indices;

	private int indexBuffer;

	public Week2() throws OpenGLException {
		Window window = new Window("Week 2 example", width, height, this);
		window.run();
	}
	

	@Override
	public void init() {
	
		shader = compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		// calculate the vertices of a hexagon as (x,y) pairs

		vertices = new float[7 * 2];
		
		// the centre

		int n = 0;
		vertices[n++] = 0;	// x
		vertices[n++] = 0;	// y
		
		// the outer ring
		
		float radius = 0.8f;
		
		for (int i = 1; i <= 6; i++) {
			double angle = i * TAU / 6;
			vertices[n++] = (float) (radius * Math.cos(angle));	// x 
			vertices[n++] = (float) (radius * Math.sin(angle)); // y
		}
				
		// copy the data into a Vertex Buffer Object in graphics memory		
	    vertexBuffer = GLBuffers.createBuffer(vertices, GL_FLOAT_VEC2);

	    // @formatter: off
		colours = new float[] {
			 1.0f, 1.0f, 1.0f,  // WHITE
			 1.0f, 0.0f, 0.0f,  // RED
			 1.0f, 1.0f, 0.0f,  // YELLOW
			 0.0f, 1.0f, 0.0f,  // GREEN
			 0.0f, 1.0f, 1.0f,  // CYAN
			 0.0f, 0.0f, 1.0f,  // BLUE
			 1.0f, 0.0f, 1.0f,  // MAGENTA
		};
	    // @formatter: on

		// copy the data into a Vertex Buffer Object in graphics memory		
	    colourBuffer = GLBuffers.createBuffer(colours, GL_FLOAT_VEC3);
	    
	    // @formatter: off
	    indices = new int[] {
	    	0, 1, 2,
	    	0, 2, 3,
	    	0, 3, 4,
	    	0, 4, 5,
	    	0, 5, 6,
	    	0, 6, 1,	    		
	    };
	    // @formatter: on
	    
	    indexBuffer = GLBuffers.createIndexBuffer(indices);
	    			    
	}

	private Shader compileShader(String vertexShaderFile, String fragmentShaderFile) {
		// Compile the shader
		try {
			File vertexShader = new File(DIRECTORY, vertexShaderFile);
			File fragementShader = new File(DIRECTORY, fragmentShaderFile);
			return new Shader(vertexShader, fragementShader);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (OpenGLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	@Override
	public void draw() {
		// set the background colour to white
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        // clear the colour buffer
		glClear(GL_COLOR_BUFFER_BIT);		

		// activate the shader
		shader.enable();
		
        // connect the vertex buffer to the a_position attribute		   
	    shader.setAttribute("a_position", vertexBuffer);
	    shader.setAttribute("a_colour", colourBuffer);

	    // write the colour value into the u_colour uniform 
//	    float[] colour = {1.0f, 0.0f, 1.0f};	    
//	    shader.setUniform("u_colour", colour);	    
	    
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
	    
	    // draw triangles as wireframe or filled
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	    glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;		
	}

	@Override
	public void close() {
		
	}


	
	public static void main(String[] args) throws OpenGLException { 
		new Week2();
	}


}
