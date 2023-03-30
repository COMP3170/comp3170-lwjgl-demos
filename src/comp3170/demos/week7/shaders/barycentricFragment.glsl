#version 410

in vec3 v_barycentric;  // interpoliated barycentric coords

layout(location = 0) out vec4 o_colour;	// RGBA

const float TAU = 6.283185307179586;
const int NLINES = 10;

void main() {
	float v = v_barycentric.z;

    o_colour = vec4(v,v,v, 1);
}

