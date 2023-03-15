#version 410

in vec4 a_position;	// vertex position as a homogeneous 3D point in model 

uniform mat4 u_mvpMatrix;			// MODEL -> NDC (= M_projection * M_view * M_model)

void main() {
    gl_Position = u_mvpMatrix * a_position;
}

