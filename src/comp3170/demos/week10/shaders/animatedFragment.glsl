#version 410

uniform sampler2D u_texture;
uniform float u_time;

in vec2 v_texcoord;	// UV 

layout(location = 0) out vec4 o_colour;

const float SPEED = 0.1;

void main() {
	vec2 uv = v_texcoord + vec2(SPEED * u_time,0);
	o_colour = texture(u_texture, uv);
}

