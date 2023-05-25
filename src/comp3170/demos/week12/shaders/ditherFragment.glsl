#version 410

uniform ivec2 u_textureResolution;
uniform sampler2D u_texture;
uniform float u_buckets;

in vec2 v_texcoord;	// UV 

layout(location = 0) out vec4 o_colour;

const vec3 gamma = vec3(2.2);

void main() {
    vec3 c = texture(u_texture, v_texcoord).rgb;
	c = pow(c,gamma);

    float b = u_buckets * 2;
    c = round(c * b);

    int x = int(floor(v_texcoord.x * u_textureResolution.x));
    int y = int(floor(v_texcoord.y * u_textureResolution.y));

    if ((x+y) % 2 == 0) {
    	c = floor(c / 2);
    }
    else {
    	c = ceil(c / 2);
    }

    c = c / u_buckets;
    c = pow(c, 1/gamma);
    o_colour = vec4(c,1);
}

