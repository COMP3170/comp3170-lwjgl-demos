package comp3170.demos.week12.textures;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.STBI_rgb_alpha;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;

import comp3170.OpenGLException;

/**
 * This class provides a wrapper to the OpenGL and STBI library calls
 * necessary to load an image file for use as an OpenGL texture.
 * 
 * @author malcolmryan
 *
 */
public class TextureLibrary {

	final private static File DIRECTORY = new File("src/comp3170/demos/week12/textures");

	public final static Map<String, Integer> loadedTextures = new HashMap<String, Integer>();

	/**
	 * Load a texture from an image file.
	 * 
	 * @param filename The image file
	 * @return The GL texture handle
	 * @throws IOException
	 * @throws OpenGLException
	 */
	public static int loadTexture(String filename) throws IOException, OpenGLException {
		if (loadedTextures.containsKey(filename)) {
			return loadedTextures.get(filename);
		}

		// Load the image file using STBI library
		File textureFile = new File(DIRECTORY, filename);
		if (!textureFile.exists()) {
			throw new FileNotFoundException(filename);
		}
		
		IntBuffer x = BufferUtils.createIntBuffer(1);
		IntBuffer y = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		
		stbi_set_flip_vertically_on_load(true);
		ByteBuffer image = stbi_load(textureFile.getAbsolutePath(), x, y, channels, STBI_rgb_alpha);

		if (image == null) {
			throw new IOException(stbi_failure_reason());
		}
				
		// Create a new texture
		
		int textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);
		
		// Set the texture data to the loaded image data
		
		// stbi_load always returns 8 bits per channel
		// See: https://javadoc.lwjgl.org/org/lwjgl/stb/STBImage.html#stbi_load(java.lang.CharSequence,java.nio.IntBuffer,java.nio.IntBuffer,java.nio.IntBuffer,int)
		// regardless of external format, we'll store the texture internally as RGBA
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, x.get(), y.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
		OpenGLException.checkError();

		// Free the image file
		stbi_image_free(image);

		// Set default texture parameters
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		loadedTextures.put(filename, textureID);
		return textureID;
	}


	/**
	 * Create a render texture with the specified dimensions.
	 * 
	 * @param width	Texture width in pixels
	 * @param height Texture height in pixels
	 * @param format Texutre format, one of GL_RED, GL_RG, GL_RGB, GL_BGR, GL_RGBA, GL_BGRA, GL_DEPTH_COMPONENT, GL_DEPTH_STENCIL
	 * @return	The OpenGL handle to the render texture.
	 */

	public static int createRenderTexture(int width, int height, int format) {
		int renderTexture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, renderTexture);
		glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, (ByteBuffer)null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		
		return renderTexture;
	}

	// Each of the sides of the cubemap
	private static final int[] cubemapTargets = { GL_TEXTURE_CUBE_MAP_POSITIVE_X, GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
			GL_TEXTURE_CUBE_MAP_POSITIVE_Y, GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
			GL_TEXTURE_CUBE_MAP_NEGATIVE_Z };

	/**
	 * Create a cube map from six image files.
	 * 
	 * @param filename	An arrange containing six filenames in the order [+X, -X, +Y, -Y, +Z, -Z]
	 * @return The OpenGL handle to the cubemap
	 * @throws IOException
	 * @throws OpenGLException
	 */
	public static int loadCubemap(String[] filename) throws IOException, OpenGLException {
		// create a cubemap texture
		int textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_CUBE_MAP, textureID);

		// load the images for the six sides of the cube

		stbi_set_flip_vertically_on_load(true);

		for (int i = 0; i < filename.length; i++) {
			File file = new File(DIRECTORY, filename[i]);
			if (!file.exists()) {
				throw new FileNotFoundException(filename[i]);
			}

			IntBuffer x = BufferUtils.createIntBuffer(1);
			IntBuffer y = BufferUtils.createIntBuffer(1);
			IntBuffer channels = BufferUtils.createIntBuffer(1);

			ByteBuffer image = stbi_load(file.getAbsolutePath(), x, y, channels, STBI_rgb_alpha);
			glTexImage2D(cubemapTargets[i], 0, GL_RGBA, x.get(), y.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
			OpenGLException.checkError();
			
			// Free the image file
			stbi_image_free(image);
		}

		// configure the texture parameters

		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

		return textureID;
	}

}
