#version 410

in vec4 v_normal;	// WORLD

layout(location = 0) out vec4 o_colour;

void main() {
	vec3 n = normalize(v_normal.xyz);
	// convert all values to range (0..1)
	n = (n+1) / 2;
	
    o_colour = vec4(n,1);
}

