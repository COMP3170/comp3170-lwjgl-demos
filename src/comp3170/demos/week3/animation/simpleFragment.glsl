#version 410

uniform vec3 u_colour;

layout(location = 0) out vec4 o_colour;

void main() {
	// pad with alpha component = 1
    o_colour = vec4(u_colour,1);
}

