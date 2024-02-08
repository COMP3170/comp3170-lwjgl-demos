#version 410

in vec4 a_position;	/* vertex position as a 4D vector (x, y, 0, 1) in NDC padded width */

void main() {
    gl_Position = a_position;
}

