#version 410

// per vertex 
in vec4 a_position;	// vertex position as a 4D vector (x, y, 0, 1) in MODEL

// per instance
in vec2 a_worldPos;   // instance position in 2D world space  
in float a_rotation;  // instance rotation angle in radians
in float a_scale;	  // instance scale    
in vec3 a_colour;	  // instance colour (r,g,b)

out vec3 v_colour; 	  // vertex colour (r,g,b)

void main() {

	// calculate model matrix as TRS
	mat4 translation = mat4(1,0,0,0, 0,1,0,0, 0,0,1,0, a_worldPos.x,a_worldPos.y,0,1); 
	float s = sin(a_rotation);
	float c = cos(a_rotation);
	mat4 rotation = mat4(c,s,0,0, -s,c,0,0, 0,0,1,0, 0,0,0,1);
	mat4 scale = mat4(a_scale,0,0,0,  0,a_scale,0,0, 0,0,1,0, 0,0,0,1);

	mat4 modelMatrix = translation * rotation * scale;
	
	// pad to a homogeneous 3D point
    gl_Position = modelMatrix * a_position;
    
	v_colour = a_colour;
}

