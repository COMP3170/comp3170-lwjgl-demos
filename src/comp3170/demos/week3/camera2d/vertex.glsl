#version 410

in vec2 a_position;	// vertex position as a 2D vector in MODEL

uniform mat4 u_modelMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_perspectiveMatrix;

void main() {
	vec4 position = vec4(a_position,0,1);
    gl_Position = u_perspectiveMatrix * u_viewMatrix * u_modelMatrix * position;
}

