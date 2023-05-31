#version 410

layout(location = 0) out vec4 o_colour;

uniform float u_time;
in vec2 v_texcoord;

float random(vec2 uv) {
    return fract(sin(dot(uv, vec2(12.9898, 78.233))) * 43758.5453);
}

float noise(vec2 p, int freq) {
	p = p * freq;
	vec2 q = floor(p);
	p = p - q;

	float r00 = random(q);
	float r01 = random(q + vec2(0,1));
	float r10 = random(q + vec2(1,0));
	float r11 = random(q + vec2(1,1));

	float r0 = mix(r00, r01, p.y);
	float r1 = mix(r10, r11, p.y);
	float r = mix(r0, r1, p.x);

	return r;
}

float octaveNoise(vec2 p, int nOctaves) {

	float decay = 0.5;
	float amp = 1;
	int octave = 1;
	float c = amp * noise(p,octave);

	for (int i = 1; i < nOctaves; i++) {
		octave *= 2;
		amp *= decay;
		c = c + amp * noise(p,octave);
	}

	// scale back to [0..1]
	c = c * (1-decay);
	return c;
}

const float speed = 4;

void main() {
	vec2 p = v_texcoord + vec2(u_time / speed);
	float c = octaveNoise(p,10);
	o_colour = vec4(c,c,c,1);
}

