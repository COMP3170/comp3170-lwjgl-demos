#version 410

in vec4 a_position;	// MODEL
in vec2 a_texcoord;	// UV 

in vec3 a_normal;	// MODEL
in vec3 a_tangent;	// MODEL
in vec3 a_bitangent;	// MODEL

uniform mat4 u_mvpMatrix;			// MODEL -> NDC

out vec2 v_texcoord;	// UV 
out vec3 v_normal;		// MODEL
out vec3 v_tangent;		// MODEL
out vec3 v_bitangent;	// MODEL

void main() {
	v_texcoord = a_texcoord;
	v_normal = a_normal;
	v_tangent = a_tangent;
	v_bitangent = a_bitangent;
	
    gl_Position = u_mvpMatrix * a_position;
}

