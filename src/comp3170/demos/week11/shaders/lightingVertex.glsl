#version 410

in vec4 a_position;	// MODEL
in vec2 a_texcoord;	// UV 
in vec4 a_normal; // MODEL

uniform mat4 u_mvpMatrix;			// MODEL -> NDC
uniform mat4 u_normalMatrix;			// MODEL -> NDC

out vec2 v_texcoord;	// UV
out vec4 v_normal;		// WORLD

void main() {
	v_texcoord = a_texcoord;
	v_normal = u_normalMatrix * a_normal;
    gl_Position = u_mvpMatrix * a_position;
}

