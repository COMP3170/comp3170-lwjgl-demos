#version 410

uniform vec4 u_colour;			// colour as a 4D vector (r,g,b,a)

layout(location = 0) out vec4 o_colour;	// output to colour buffer (r,g,b,a)

void main() {
    o_colour = vec4(u_colour);
}
