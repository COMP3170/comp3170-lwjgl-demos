#version 410

uniform sampler2D u_texture;
in vec2 v_texcoord;	// UV 

layout(location = 0) out vec4 o_colour;

void main() {
    o_colour = texture(u_texture, v_texcoord);
}

