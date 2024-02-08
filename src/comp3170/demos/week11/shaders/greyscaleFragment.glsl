#version 410

uniform sampler2D u_texture;

in vec2 v_texcoord;	// UV 

const float GAMMA = 2.2;

layout(location = 0) out vec4 o_colour;

void main() {
    vec3 c = texture(u_texture, v_texcoord).rgb;    
    c = pow(c, vec3(GAMMA));
    
    float i = (c.r + c.g + c.b) / 3;
    float b = pow(i, 1./GAMMA); 
    
    o_colour = vec4(b,b,b,1);
}

