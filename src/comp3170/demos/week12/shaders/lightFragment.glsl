#version 410

uniform vec3 u_intensity;        // source intensity (RGB)
uniform vec3 u_ambientIntensity; // ambient intensity (RGB)

uniform vec4 u_lightDirection;   // source vector (WORLD)
uniform vec4 u_viewPosition;  // camera position(WORLD)

uniform vec3 u_specularMaterial;	// RGB - intensity
uniform float u_specularity; 	// specularity constant


in vec4 v_normal;                // interpolated surface normal (WORLD)
in vec4 v_position;              // interpolated fragment position (WORLD)
in vec3 v_colour;				// RGB - intensity

layout(location = 0) out vec4 o_colour;

const vec3 gamma = vec3(2.2);

void main() {
    // normalise the vectors
    vec4 n = normalize(v_normal);
    vec4 s = normalize(u_lightDirection);
    vec4 r = vec4(0);
    vec4 v = normalize(u_viewPosition - v_position);

    vec3 ambient = u_ambientIntensity * v_colour;
    vec3 diffuse = u_intensity * v_colour * max(0, dot(s,n));
	vec3 specular = vec3(0);
	
	if (dot(s,n) > 0) {
	    r = -reflect(s, n);
		specular = u_intensity * u_specularMaterial * pow(max(0,dot(r,v)), u_specularity);
	}

	vec3 intensity = ambient + diffuse + specular;
	vec3 brightness = pow(intensity, 1./gamma);
    o_colour = vec4(brightness, 1);
}
