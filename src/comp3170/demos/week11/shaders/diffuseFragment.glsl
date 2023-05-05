#version 410

uniform sampler2D u_diffuseTexture;

uniform vec4 u_lightDirection;		// WORLD
uniform vec3 u_intensity;			// RGB - intensity
uniform vec3 u_ambientIntensity;	// RGB - intensity

in vec2 v_texcoord;		// UV 
in vec3 v_normal;		// WORLD

const vec3 GAMMA = vec3(2.2f);

layout(location = 0) out vec4 o_colour;

void main() {
	vec3 n = normalize(v_normal);

	vec3 material = texture(u_diffuseTexture, v_texcoord).rgb; 
	material = pow(material, GAMMA);
	
	vec3 s = normalize(u_lightDirection.xyz);
	vec3 ambient = u_ambientIntensity * material;
	vec3 diffuse = u_intensity * material * max(0, dot(n, s));
	
	vec3 intensity = ambient + diffuse;
	vec3 brightness = pow(intensity, 1. / GAMMA);

    o_colour = vec4(brightness, 1);
}

