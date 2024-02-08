#version 410

uniform float u_near;
uniform float u_far;

layout(location = 0) out vec4 o_colour;

void main() {
	float d = gl_FragCoord.z;
	float n = u_near; 
	float f = u_far;

	// convert depth back into a linear value from 0 (near) to 1 (far)
	d = n * d / (f + (n-f) * d);
	
    o_colour = vec4(d,d,d,1);
}

