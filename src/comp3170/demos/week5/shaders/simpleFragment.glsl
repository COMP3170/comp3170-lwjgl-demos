#version 410

uniform vec4 u_colour;	// RGBA

layout(location = 0) out vec4 o_colour;	// RGBA

void main() {
    o_colour = u_colour;
}

