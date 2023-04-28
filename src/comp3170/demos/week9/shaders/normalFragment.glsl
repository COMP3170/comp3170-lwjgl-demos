#version 410

layout(location = 0) out vec4 o_colour;

in vec4 v_normal; // WORLD

void main() {
	vec3 n = v_normal.xyz;
	n = normalize(n);
    o_colour = vec4(n,1);
}

