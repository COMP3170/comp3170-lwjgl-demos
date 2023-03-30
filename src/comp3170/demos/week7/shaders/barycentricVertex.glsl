#version 410

in vec4 a_position;	// vertex position as a homogeneous 3D point in model space
in vec3 a_barycentric;	// barycentric coords for vertex

out vec3 v_barycentric;  // interpoliated barycentric coords

uniform mat4 u_mvpMatrix;	// MODEL -> NDC

void main() {
	v_barycentric = a_barycentric;
    gl_Position = u_mvpMatrix * a_position;
}

