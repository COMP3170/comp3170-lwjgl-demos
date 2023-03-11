#version 410

in vec2 a_position;	// vertex position as a 2D vector in model space 

uniform mat3 u_modelMatrix; 		// model->world transformation matrix 
uniform mat3 u_viewMatrix; 			// world->view transformation matrix 
uniform mat3 u_projectionMatrix; 	// view->NDC transformation matrix 

void main() {
	// convert the vertex to a homogeneous 2D point
	vec3 p = vec3(a_position, 1);	// MODEL
	
	// apply the model->world transform

    	p = u_projectionMatrix * u_viewMatrix * u_modelMatrix * p;
    //
	
	// pad to a homogeneous 3D point NDC
    gl_Position = vec4(p.xy,0,1);
}

