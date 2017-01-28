#version 120

attribute vec4 a_position;

uniform mat4 u_camProj;
uniform mat4 u_camView;

varying vec3 v_texCoord;

void main()
{
    v_texCoord = a_position.xyz;
    vec4 tpos = u_camProj * u_camView * a_position;

    gl_Position = tpos.xyww;
}