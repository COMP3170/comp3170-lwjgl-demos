#version 410

in vec2 a_position;	// vertex position as a 2D vector in NDC 

uniform mat3 u_modelMatrix; 		// model->world transformation matrix 
uniform mat3 u_viewMatrix; 			// world->view transformation matrix 
uniform mat3 u_projectionMatrix; 	// view->NDC transformation matrix 

void main() {
	// convert the vertex to a homogeneous 2D point
	vec3 p = vec3(a_position, 1);
	
	// apply the model->world transform
	p = u_modelMatrix * p;
	p = u_viewMatrix * p;
	p = u_projectionMatrix * p;
	
	// pad to a homogeneous 3D point
    gl_Position = vec4(p.xy,0,1);
}

