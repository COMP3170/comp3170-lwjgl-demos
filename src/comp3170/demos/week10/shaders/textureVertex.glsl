#version 410

in vec4 a_position;	// MODEL
in vec2 a_texcoord;	// UV 

uniform mat4 u_mvpMatrix;			// MODEL -> NDC

out vec2 v_texcoord;	// UV 

void main() {
	v_texcoord = a_texcoord;
    gl_Position = u_mvpMatrix * a_position;
}

