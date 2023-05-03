package comp3170.demos.week10.sceneobjects;

import static comp3170.Math.random;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

import java.util.Arrays;
import java.util.Comparator;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week10.cameras.Camera;
import comp3170.demos.week10.demos.ParticleDemo;
import comp3170.demos.week10.shaders.ShaderLibrary;
import comp3170.demos.week10.textures.TextureLibrary;

public class Explosion extends SceneObject {
	private static final String VERTEX_SHADER = "explosionVertex.glsl";
	private static final String FRAGMENT_SHADER = "explosionFragment.glsl";
	private Shader shader;

	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector2f[] uvs;
	private int uvBuffer;
	private int[] indices;
	private int indexBuffer;

	// Source: https://www.pngegg.com/en/png-zesfv
	private static final String PARTICLE_TEXTURE = "flame-particle.png";
	private int texture;
	
	private static final int NPARTICLES = 1000;
	private static final float RADIUS = 10;
	private Vector4f[] instances;
	private int instanceBuffer;
	

	public Explosion() {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		createQuad();
		createInstances();

		try {
			texture = TextureLibrary.loadTexture(PARTICLE_TEXTURE);		
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void createQuad() {
		//    2-----3
		//    |\    |
		//    | \   |
		//    |  *  |   y
		//    |   \ |   ^
		//    |    \|   |
		//    0-----1   +->x
		
		vertices = new Vector4f[] {
			new Vector4f(-1,-1,0,1),
			new Vector4f( 1,-1,0,1),
			new Vector4f(-1,1,0,1),
			new Vector4f( 1,1,0,1),			
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
			0, 1, 2,
			3, 2, 1,
		};
		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}

	private void createInstances() {

		// create random points in a sphere

		instances = new Vector4f[NPARTICLES];
		
		for (int i = 0; i < instances.length; i++) {
			Vector4f p = new Vector4f();
			
			// create random points in a cube 
			// and regenerate any that aren't within the sphere
			do {
				p.x = random(-RADIUS, RADIUS);
				p.y = random(-RADIUS, RADIUS);
				p.z = random(-RADIUS, RADIUS);
			}
			while (p.length() > RADIUS);			
			
			instances[i] = p;
		}
		
		instanceBuffer = GLBuffers.createBuffer(instances);
	}

	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f cameraMatrix = new Matrix4f();

	public void drawSelf(Matrix4f mvpMatrix) {
		Camera camera = ParticleDemo.instance.getCamera();
		camera.getCameraMatrix(cameraMatrix);
		camera.getViewMatrix(viewMatrix);		
		sortParticles();
		
		shader.enable();

		// matrices
	    shader.setUniform("u_mvpMatrix", mvpMatrix);
		
		// per vertex attributes
	    shader.setAttribute("a_position", vertexBuffer);
	    shader.setAttribute("a_texcoord", uvBuffer);

		// camera
		shader.setUniform("u_cameraMatrix", cameraMatrix);

		// texture
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture);
		shader.setUniform("u_texture", 0);

		// per instance attributes
	    shader.setAttribute("a_instance", instanceBuffer);
		glVertexAttribDivisor(shader.getAttribute("a_instance"), 1);
	    
		// draw
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	    glDrawElementsInstanced(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0, instances.length);

	    // clean up
		glVertexAttribDivisor(shader.getAttribute("a_instance"), 0);
		
	}

	private Vector4f pv1 = new Vector4f();
	private Vector4f pv2 = new Vector4f();
	
	private void sortParticles() {
				
		Arrays.sort(instances, new Comparator<Vector4f>() {
			@Override
			public int compare(Vector4f p1, Vector4f p2) {
				// WORLD -> VIEW
				p1.mul(viewMatrix, pv1);
				p2.mul(viewMatrix, pv2);
				
				// furthest particles (most negative) are drawn first
				return (pv1.z == pv2.z ? 0 : (pv1.z < pv2.z ? -1 : 1));
			}
		});		
		
		GLBuffers.updateBuffer(instanceBuffer, instances);
	}
}
