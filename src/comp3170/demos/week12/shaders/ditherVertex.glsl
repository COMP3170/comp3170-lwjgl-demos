#version 410

in vec4 a_position;	// NDC
in vec2 a_texcoord;	// UV 

out vec2 v_texcoord;	// UV 

void main() {
	v_texcoord = a_texcoord;
    gl_Position = a_position;
}

