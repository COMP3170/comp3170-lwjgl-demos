#version 410

in vec4 a_position;		// vertex in 3D homogenous coordinates (MODEL)
in vec4 a_normal;		// normal vector in 3D homogenous coordinates (MODEL)

uniform mat4 u_mvpMatrix;	// MODEL -> NDC
uniform mat4 u_modelMatrix;	// MODEL -> WORLD
uniform mat4 u_normalMatrix;	// MODEL -> WORLD (without scaling)

out vec4 v_normal;	// WORLD
out vec4 v_position;	// WORLD

void main() {
	v_normal = u_normalMatrix * a_normal;
	v_position = u_modelMatrix * a_position;
    gl_Position = u_mvpMatrix * a_position;
}

