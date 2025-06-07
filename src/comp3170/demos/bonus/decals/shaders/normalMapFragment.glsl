#version 410

uniform sampler2D u_diffuseTexture;
uniform sampler2D u_normalTexture;	// normals in UV space

uniform mat3 u_normalMatrix;		// MODEL -> WORLD (normals)

in vec4 v_position;		// WORLD
in vec2 v_texcoord;		// UV 
in vec3 v_normal;		// MODEL
in vec3 v_tangent;		// MODEL
in vec3 v_bitangent;	// MODEL

layout(location = 0) out vec4 o_colour;
layout(location = 1) out vec4 o_position;
layout(location = 2) out vec4 o_normal;

void main() {
	vec3 t = normalize(v_tangent);
	vec3 b = normalize(v_bitangent);
	vec3 n = normalize(v_normal);
	mat3 tbnMatrix = mat3(t, b, n);		// UV -> MODEL

	// get normal from normal map texture
	// and convert to world space
	n = texture(u_normalTexture, v_texcoord).xyz;
	n = 2. * n - 1.;
	n = u_normalMatrix * tbnMatrix * n;

    o_colour = texture(u_diffuseTexture, v_texcoord);
    o_position = v_position;
	o_normal = vec4(n,0);
}

