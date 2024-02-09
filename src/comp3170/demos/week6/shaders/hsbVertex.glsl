#version 410

in vec4 a_position;	// vertex position as a homogeneous 3D point in model 
in vec3 a_hsb;			// colour as a 4D vector (r,g,b,a)

out vec3 v_hsb;			// colour as a 4D vector (r,g,b,a)

uniform mat4 u_mvpMatrix;			// MODEL -> NDC (= M_projection * M_view * M_model)

void main() {
	v_hsb = a_hsb;
    gl_Position = u_mvpMatrix * a_position;
}

