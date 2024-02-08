#version 410

uniform ivec2 u_textureResolution;
uniform sampler2D u_texture0;
uniform sampler2D u_texture1;

in vec2 v_texcoord;	// UV 

layout(location = 0) out vec4 o_colour;

const float[] kernel = float[] (-1, -1, -1, -1, 8, -1, -1, -1, -1);
const vec3 gamma = vec3(2.2);

void main() {
    int x = int(floor(v_texcoord.x * u_textureResolution.x));
    int y = int(floor(v_texcoord.y * u_textureResolution.y));

    vec3 total = vec3(0);

    int k = 0;
    for (int dx = -1; dx <= 1; dx++) {
        for (int dy = -1; dy <= 1; dy++) {
        	vec2 uv = vec2(x+dx, y+dy) / u_textureResolution;
        	vec3 c = texture(u_texture0, uv).rgb;

        	total = total + kernel[k] * c;
        	k++;
        }
    }

    total = total / 8;
    total = abs(total);

    float edge = max(max(total.r, total.g), total.b);
	edge = smoothstep(0,0.2,edge);
    edge = step(0.1,edge);

	// blend outline into image
	vec3 c = texture(u_texture1, v_texcoord).rgb;
	c = pow(c, gamma);
	c = c * (1-edge);
	c = pow(c, 1./gamma);
	
    o_colour = vec4(c,1);
}

