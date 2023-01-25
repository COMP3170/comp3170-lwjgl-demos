#version 410

in vec2 a_position;	/* vertex position as a 2D vector in NDC */

void main() {
	// pad the vertex to a homogeneous 3D point
    gl_Position = vec4(a_position,0,1);

}

