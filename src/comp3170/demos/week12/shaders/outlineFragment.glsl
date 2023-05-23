#version 410

uniform ivec2 u_textureResolution;
uniform sampler2D u_texture;

in vec2 v_texcoord;	// UV 

layout(location = 0) out vec4 o_colour;

const float[] kernel = float[] (-1, -1, -1, -1, 8, -1, -1, -1, -1);

void main() {
    int x = int(floor(v_texcoord.x * u_textureResolution.x));
    int y = int(floor(v_texcoord.y * u_textureResolution.y));

    vec3 total = vec3(0);

    int k = 0;
    for (int dx = -1; dx <= 1; dx++) {
        for (int dy = -1; dy <= 1; dy++) {
        	vec2 uv = vec2(x+dx, y+dy) / u_textureResolution;
        	vec3 c = texture(u_texture, uv).rgb;

        	total = total + kernel[k] * c;
        	k++;
        }
    }

    total = total / 8;
    total = abs(total);

    float edge = max(max(total.r, total.g), total.b);

    o_colour = vec4(edge, edge, edge,1);
}

