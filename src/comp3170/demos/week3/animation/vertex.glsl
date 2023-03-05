#version 410

in vec3 a_position;	// vertex position as a 2D homogeneous vector in MODEL

uniform mat3 u_modelMatrix; // model->world transformation matrix 

void main() {
	// apply the model->world transform
	vec3 p = u_modelMatrix * a_position;
	
	// pad to a homogeneous 3D point
    gl_Position = vec4(p.xy,0,1);
}

