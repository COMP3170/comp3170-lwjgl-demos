#version 410

in vec2 a_position;	// vertex position as a 2D vector in NDC 

uniform mat4 u_modelMatrix; 		// model->world transformation matrix 
uniform mat4 u_viewMatrix; 			// world->view transformation matrix 
uniform mat4 u_projectionMatrix; 	// view->NDC transformation matrix 

void main() {
	// convert the vertex to a homogeneous 3D point
	vec4 p = vec4(a_position, 0, 1);
	
	// apply the model->world transform
	p = u_modelMatrix * p;
	p = u_viewMatrix * p;
	p = u_projectionMatrix * p;
	
	// pad to a homogeneous 3D point
    gl_Position = vec4(p.xy,0,1);
}

