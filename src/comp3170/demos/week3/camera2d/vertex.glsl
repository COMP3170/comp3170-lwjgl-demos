#version 410

in vec4 a_position;	// vertex position as a 4D vector (x, y, 0, 1) in MODEL

uniform mat4 u_modelMatrix;		// MODEL -> WORLD
uniform mat4 u_viewMatrix;		// WORLD -> VIEW
uniform mat4 u_perspectiveMatrix;	// VIEW -> NDC

void main() {
	gl_Position = u_perspectiveMatrix * u_viewMatrix * u_modelMatrix * a_position;
		
}

