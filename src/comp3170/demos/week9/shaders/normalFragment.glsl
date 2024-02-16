#version 410

layout(location = 0) out vec4 o_colour;

in vec4 v_normal; // WORLD

void main() {
	vec4 n = normalize(v_normal);	
    o_colour = vec4(n.xyz,1);
}

