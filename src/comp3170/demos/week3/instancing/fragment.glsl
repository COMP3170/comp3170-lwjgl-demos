#version 410

in vec3 v_colour;			// colour as a 3D vector (r,g,b)

layout(location = 0) out vec4 o_colour;	// output to colour buffer (r,g,b,a)

void main() {
    o_colour = vec4(v_colour, 1);
}
