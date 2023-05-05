#version 410

in vec4 a_position;	// MODEL
in vec2 a_texcoord;	// UV 
in vec3 a_normal;	// MODEL

uniform mat4 u_mvpMatrix;			// MODEL -> NDC
uniform mat3 u_normalMatrix;		// MODEL -> WORLD (normals)

out vec2 v_texcoord;	// UV 
out vec3 v_normal;		// WORLD

void main() {
	v_texcoord = a_texcoord;
	v_normal = u_normalMatrix * a_normal;	
    gl_Position = u_mvpMatrix * a_position;
}

