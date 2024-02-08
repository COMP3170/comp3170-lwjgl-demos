#version 410

in vec4 a_position;	// vertex positon (MODEL)
in vec2 a_texcoord;	// vertex UV 

in vec4 a_instance;	// instance offset (WORLD)

uniform mat4 u_mvpMatrix;	// VIEW->NDC
uniform mat4 u_cameraMatrix;	// camera MODEL -> WORLD

out vec2 v_texcoord;	// interpolated UVs

void main() {
	v_texcoord = a_texcoord;
	
	vec3 up = vec3(0,1,0);
	vec4 k = normalize(u_cameraMatrix[2]);
	vec4 i = normalize(vec4(cross(up, k.xyz), 0));
	vec4 j = normalize(vec4(cross(k.xyz, i.xyz), 0));

	mat4 modelMatrix = mat4(i, j, k, a_instance);
    gl_Position = u_mvpMatrix * modelMatrix * a_position;
}

