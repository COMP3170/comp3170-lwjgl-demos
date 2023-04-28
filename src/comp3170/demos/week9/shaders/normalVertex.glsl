#version 410

in vec4 a_position;		// vertex in 3D homogenous coordinates (MODEL)
in vec4 a_normal;		// normal vector in 3D homogenous coordinates (MODEL)

uniform mat4 u_mvpMatrix;	// MODEL -> NDC
uniform mat4 u_modelMatrix;	// MODEL -> WORLD 
uniform mat4 u_normalMatrix;	// MODEL -> WORLD (for normals)

out vec4 v_normal;	// WORLD

void main() {
	// show normals in model space:
//	v_normal = a_normal;
	
	// use model matrix to transform normals (incorrect)
//	v_normal = u_modelMatrix * a_normal;

	// user normal matrix to transform normals (correct)
	v_normal = u_normalMatrix * a_normal;
    gl_Position = u_mvpMatrix * a_position;
}

