#version 120

uniform samplerCube u_cubemap;

varying vec3 v_texCoord;

void main()
{
    gl_FragColor = textureCube(u_cubemap, v_texCoord);
}