#version 410

in vec4 a_position;			// MODEL
in vec4 a_normal;			// MODEL
uniform mat4 u_mvpMatrix;	// MODEL -> NDC
uniform mat4 u_modelMatrix;	// MODEL -> WORLD
uniform mat4 u_normalMatrix;	// MODEL -> WORLD

out vec4 v_position;		// WORLD
out vec4 v_normal;			// WORLD

void main() {
	v_position = u_modelMatrix * a_position;
	v_normal = u_normalMatrix * a_normal;
	
    gl_Position = u_mvpMatrix * a_position;
}

