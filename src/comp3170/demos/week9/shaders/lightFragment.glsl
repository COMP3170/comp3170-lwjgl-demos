#version 410

uniform vec3 u_intensity;        // source intensity (RGB)
uniform vec3 u_ambientIntensity; // ambient intensity (RGB)

uniform vec4 u_lightDirection;   // source vector (WORLD)
uniform vec4 u_viewDirection;  // view vector (WORLD)

uniform vec3 u_diffuseMaterial;  // diffuse material cofficients (RGB)
uniform vec3 u_specularMaterial;  // specular material cofficients (RGB)
uniform float u_specularity; 	// specularity constant

in vec4 v_normal;                // interpolated surface normal (WORLD)
in vec4 v_position;              // interpolated fragment position (WORLD)


layout(location = 0) out vec4 o_colour;

void main() {
    // normalise the vectors
    vec4 n = normalize(v_normal);
    vec4 s = normalize(u_lightDirection);	// assumes light is directional
    vec4 r = vec4(0);
    vec4 v = normalize(u_viewDirection);	// assumes camera is orthographic

    vec3 ambient = u_ambientIntensity * u_diffuseMaterial;
    vec3 diffuse = u_intensity * u_diffuseMaterial * max(0, dot(s,n));
	vec3 specular = vec3(0);
	
	if (dot(s,n) > 0) {
	    r = -reflect(s, n);
		specular = u_intensity * u_specularMaterial * pow(max(0,dot(r,v)), u_specularity); 
	}

	vec3 intensity = ambient + diffuse + specular;
    o_colour = vec4(intensity, 1);
}