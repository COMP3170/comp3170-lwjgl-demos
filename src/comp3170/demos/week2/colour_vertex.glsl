#version 410

in vec4 a_position;	// vertex position as a 4D vector (x, y, 0, 1) in NDC
in vec3 a_colour; // RGB

out vec3 v_colour; // RGB

void main() {
	// pad the vertex to a homogeneous 3D point
    gl_Position = a_position;

    // pass the colour to the fragment shader
    v_colour = a_colour;
}

