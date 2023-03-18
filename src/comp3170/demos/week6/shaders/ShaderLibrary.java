package comp3170.demos.week6.shaders;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import comp3170.OpenGLException;
import comp3170.Shader;

public class ShaderLibrary {

	final private static File DIRECTORY = new File("src/comp3170/demos/week6/shaders"); 	
	private static Map<Pair<String, String>, Shader> loadedShaders = new HashMap<Pair<String, String>, Shader>();
	
	/**
	 * Load, compile and link a shader. 
	 * 
	 * All shader files are expected to be in the folder given by the DIRECTORY constant above.  
	 * 
	 * If the vertex/fragment pair have already been compiled and linked, 
	 * a reference to the existing shader will be returned, rather than recompiling.  
	 * 
	 * @param vertex	The name of the vertex shader file
	 * @param fragment	The name of the fragment shader file
	 * @return
	 */
	
	public static Shader compileShader(String vertex, String fragment) {
		
		Pair<String, String> p = new Pair<String, String>(vertex, fragment);

		if (loadedShaders.containsKey(p)) {
			return loadedShaders.get(p);
		}		
		
		Shader shader = null;
		try {
			File vertexShader = new File(DIRECTORY, vertex);
			File fragmentShader = new File(DIRECTORY, fragment);
			shader = new Shader(vertexShader, fragmentShader);		
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (OpenGLException e) {
			e.printStackTrace();
			System.exit(1);
		}

		loadedShaders.put(p, shader);
		
		return shader;

	}

	/**
	 * Creates a pair of two objects
	 * 
	 * @param <T1> the type of object 1
	 * @param <T2> the type of object 2
	 */
	private static class Pair<T1, T2> {
	    
	    /**
	     * The first object
	     */
	    private final T1 obj1;
	    
	    /**
	     * The second object
	     */
	    private final T2 obj2;
	    
	    /**
	     * Creates a new pair of objects
	     * 
	     * @param first the first object
	     * @param second the second object
	     */
	    public Pair(T1 first, T2 second) {
	        this.obj1 = first;
	        this.obj2 = second;
	    }
	    
	    /**
	     * @return the first object
	     */
	    public T1 getFirst() {
	        return this.obj1;
	    }
	    
	    /**
	     * @return the second object
	     */
	    public T2 getSecond() {
	        return this.obj2;
	    }
	    
	    @Override
	    public boolean equals(Object other) {
	        if (!(other instanceof Pair<?,?>)) {
	            return false;
	        }
	        Pair<?,?> otherObj = (Pair<?,?>)other;
	        return otherObj.obj1.equals(obj1) && otherObj.obj2.equals(obj2);
	    }
	    
	    /**
	     * The number of bits per bytes
	     */
	    private static final int BITS_PER_BYTES = 8;
	    
	    /**
	     * The number of bytes in the hash
	     */
	    private static final int NUMBER_BITS = Integer.BYTES * BITS_PER_BYTES;
	    
	    /**
	     * The number of bytes in the hash
	     */
	    private static final int HALF_NUMBER_BITS = NUMBER_BITS / 2;
	    
	    /**
	     * 0's for the first half of the int, 1's for the second half of the int
	     */
	    private static final int EMPTY_FULL = (1 << (HALF_NUMBER_BITS + 1)) - 1;

	    /**
	     * 1's for the first half of the int, 0's for the second half of the int
	     */
	    private static final int FULL_EMPTY = EMPTY_FULL << HALF_NUMBER_BITS;
	    
	    @Override
	    public int hashCode() {
	        int firstHash = this.obj1.hashCode();
	        int first16Bits = (EMPTY_FULL & firstHash) ^ ((FULL_EMPTY & firstHash) >> HALF_NUMBER_BITS);
	        int secondHash = this.obj2.hashCode();
	        int second16Bits = (EMPTY_FULL & secondHash) ^ ((FULL_EMPTY & secondHash) >> HALF_NUMBER_BITS);
	        return (first16Bits << HALF_NUMBER_BITS) | second16Bits;
	    }
	    
	    @Override
	    public String toString() {
	        return String.format("Pair<%s, %s>", this.obj1.toString(),
	                this.obj2.toString());
	    }
	}

}
