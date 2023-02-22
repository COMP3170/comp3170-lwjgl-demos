#version 410

// per vertex 
in vec2 a_position;	// vertex position as a 2D vector in MODEL

// per instance
in vec2 a_worldPos;   // instance position in 2D world space  
in float a_rotation;  // instance rotation angle in radians
in float a_scale;	  // instance scale    

void main() {
	mat3 translation = mat3(1,0,0, 0,1,0, a_worldPos, 1); 
	float s = sin(a_rotation);
	float c = cos(a_rotation);
	mat3 rotation = mat3(c,s,0, -s,c,0, 0,0,1);
	mat3 scale = mat3(a_scale, 0, 0,  0, a_scale, 0, 0, 0, 1);

	mat3 modelMatrix = translation * rotation * scale;
	vec3 p = modelMatrix * vec3(aPosition, 1);
	
	// pad to a homogeneous 3D point
    gl_Position = vec4(p.xy,0,1);
}

