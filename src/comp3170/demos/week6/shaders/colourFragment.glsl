#version 410

in vec3 v_hsb;			// colour as a 3D HSB vector

layout(location = 0) out vec4 o_colour;	// output to colour buffer (r,g,b,a)

// Stolen from: https://gist.github.com/983/e170a24ae8eba2cd174f

vec3 hsv2rgb(vec3 c)
{
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main() {
    o_colour = vec4(hsv2rgb(v_hsb),1);
}
