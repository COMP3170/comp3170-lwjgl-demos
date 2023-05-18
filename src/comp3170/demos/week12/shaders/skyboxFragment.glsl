#version 410

uniform samplerCube u_cubemap;

in vec4 v_position;	// WORLD

layout(location = 0) out vec4 o_colour;

void main() {
	gl_FragDepth = 1;
    o_colour = texture(u_cubemap, v_position.xyz);
}

