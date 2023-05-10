#version 410

uniform vec4 u_colour;

layout(location = 0) out vec4 o_colour;

void main() {
    o_colour = u_colour;
}

