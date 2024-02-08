#version 410

in vec4 a_position;		// vertex in 3D homogenous coordinates (MODEL)
in vec4 a_normal;		// normal vector in 3D homogenous coordinates (MODEL)
in vec3 a_colour;		// colour (RGB - brightness)

uniform mat4 u_mvpMatrix;	// MODEL -> NDC
uniform mat4 u_modelMatrix;	// MODEL -> WORLD
uniform mat4 u_normalMatrix;	// MODEL -> WORLD (without scaling)

out vec4 v_normal;	// WORLD
out vec4 v_position;	// WORLD
out vec3 v_colour;	// RGB - intensity

const vec3 gamma = vec3(2.2);

void main() {
	v_normal = u_normalMatrix * a_normal;
	v_position = u_modelMatrix * a_position;
	v_colour = pow(a_colour, gamma);
    gl_Position = u_mvpMatrix * a_position;
}

