#version 410

uniform samplerCube u_cubemap;
uniform vec4 u_cameraPosition;

in vec4 v_position;	// WORLD
in vec4 v_normal;	// WORLD

layout(location = 0) out vec4 o_colour;

void main() {
	// calculate the reflection vector
	vec4 v = normalize(u_cameraPosition - v_position);
	vec4 n = normalize(v_normal);	
	vec4 r = -reflect(v, n);

	// look up the reflection vector in the environment map
    o_colour = texture(u_cubemap, r.xyz);
 }

