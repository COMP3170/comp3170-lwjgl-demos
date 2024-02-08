#version 410

in vec4 a_position;	// vertex position as a homogeneous 3D point in model 
in vec4 a_normal;		// normal vector in 3D homogenous coordinates (MODEL)

uniform mat4 u_mvpMatrix;			// MODEL -> NDC
uniform mat4 u_normalMatrix;	// MODEL -> WORLD (without scaling)

out vec4 v_normal;	// WORLD

void main() {
	v_normal = u_normalMatrix * a_normal;
    gl_Position = u_mvpMatrix * a_position;
}

