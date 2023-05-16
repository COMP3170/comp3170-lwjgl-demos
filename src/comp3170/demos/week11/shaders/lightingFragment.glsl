#version 410

uniform float u_gamma;
uniform sampler2D u_texture;
uniform vec3 u_ambient;		// RGB - Intensity
uniform vec3 u_intensity;	// RGB - Intensity
uniform vec4 u_lightDirection;		// WORLD
uniform vec4 u_viewDirection;		// WORLD
uniform float u_specularity;

in vec2 v_texcoord;	// UV 
in vec4 v_normal;	// WORLD

layout(location = 0) out vec4 o_colour;

void main() {
	vec4 s = normalize(u_lightDirection);
	vec4 v = normalize(u_viewDirection);
	vec4 n = normalize(v_normal);

	vec3 gamma = vec3(u_gamma);
	vec3 material = texture(u_texture, v_texcoord).rgb;	// RGB - Brightness

	material = pow(material, gamma);  // I = B^g
	vec3 ambient = material * u_ambient; // Intensity
	vec3 diffuse = material * u_intensity * max(0, dot(n,s));  // Intensity
	vec3 specular = vec3(0);

	if (dot(s,n) > 0) {
	    vec4 r = -reflect(s, n);
		specular = u_intensity * pow(max(0,dot(r,v)), u_specularity);
	}

	vec3 intensity = ambient + diffuse + specular;
	vec3 brightness = pow(intensity, 1/gamma); // B = I^(1/g)

    o_colour = vec4(brightness, 1);	// RGBA - Brightness
}

