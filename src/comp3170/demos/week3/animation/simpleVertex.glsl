#version 410

in vec4 a_position;	// vertex position as a homogeneous 3D point in model 

uniform mat4 u_modelMatrix;			// MODEL -> NDC

void main() {
    gl_Position = u_modelMatrix * a_position;
}

