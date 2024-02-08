#version 410

in vec4 a_position;	// vertex position as a homogeneous 4D point (x,y,0,1) in NDC 

void main() {
    gl_Position = a_position;
}

