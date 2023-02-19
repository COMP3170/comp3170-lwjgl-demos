#version 410

in vec3 a_position;	// vertex position as a 2D vector in MODEL
in mat3 a_modelMatrix; // per instance model->world transformation matrix 

void main() {
	// apply the model->world transform
	vec3 p = a_modelMatrix * a_position;
	
	// pad to a homogeneous 3D point
    gl_Position = vec4(p.xy,0,1);
}

