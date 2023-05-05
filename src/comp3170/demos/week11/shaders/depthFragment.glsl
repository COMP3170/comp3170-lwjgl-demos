#version 410

uniform vec4 u_colour;

layout(location = 0) out vec4 o_colour;

void main() {
	float dist = gl_FragCoord.z;
    o_colour = vec4(u_colour.rgb, dist);
}

