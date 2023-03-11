#version 410

in vec3 a_position;	// vertex position as a homogeneous 2D point in NDC 

void main() {
	// pad to a homogeneous 3D point
    gl_Position = vec4(a_position.xy,0,1);
}

