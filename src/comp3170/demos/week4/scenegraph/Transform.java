package comp3170.demos.week4.scenegraph;

import org.joml.Matrix3f;

public class Transform {
	
	public static final float TAU = (float)Math.PI * 2; 

	/**
	 * Set the destination matrix to a translation matrix.
	 * Note the destination matrix must already be allocated. 
	 * 
	 * @param tx	Offset in the x direction
	 * @param ty	Offset in the y direction
	 * @param dest	Destination matrix to write into
	 * @return
	 */
	
	public static Matrix3f translationMatrix(float tx, float ty, Matrix3f dest) {
		// clear the matrix to the identity matrix
		dest.identity();
		
		//     [ 1  0 tx ]
		// T = [ 0  1 ty ]
		//     [ 0  0  1 ]
		
		dest.m20(tx);
		dest.m21(ty);
		
		return dest;
	}
	
	/**
	 * Set the destination matrix to a rotation matrix.
	 * Note the destination matrix must already be allocated. 
	 *
	 * @param angle	Angle of rotation (in radians)
	 * @param dest	Destination matrix to write into
	 * @return
	 */

	public static Matrix3f rotationMatrix(float angle, Matrix3f dest) {
		
		// clear the matrix to the identity matrix
		dest.identity();
		
		//     [ cos(a)  -sin(a)  0 ]
		// R = [ sin(a)   cos(a)  0 ]
		//     [   0        0     1 ]
		
		float s = (float) Math.sin(angle);
		float c = (float) Math.cos(angle);
		
		dest.m00(c);
		dest.m01(s);
		dest.m10(-s);
		dest.m11(c);
		
		return dest;
	}

	/**
 	 * Set the destination matrix to a scale matrix.
	 * Note the destination matrix must already be allocated. 
	 *
	 * @param sx	Scale factor in x direction
	 * @param sy	Scale factor in y direction
	 * @param dest	Destination matrix to write into
	 * @return
	 */
	
	public static Matrix3f scaleMatrix(float sx, float sy, Matrix3f dest) {
		
		// clear the matrix to the identity matrix
		dest.identity();
		
		//     [ sx  0  0 ]
		// S = [  0 sy  0 ]
		//     [  0  0  1 ]
		
		dest.m00(sx);
		dest.m11(sy);
		
		return dest;
	}

}
