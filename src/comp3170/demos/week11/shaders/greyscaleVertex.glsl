#version 410

in vec4 a_position;	// MODEL = NDC
in vec2 a_texcoord;	// UV 

out vec2 v_texcoord;	// UV 

void main() {
	v_texcoord = a_texcoord;
	// no MVP matrix, as the quad is drawn directly in NDC space
    gl_Position = a_position;
}

