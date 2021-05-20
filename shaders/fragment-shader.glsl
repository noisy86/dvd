#version 330 core

out vec4 FragColor;

in vec3 outColor;
in vec2 outTexture;

uniform sampler2D ourTexture;

void main()
{
    if(outTexture != vec2(0,0)) {
        vec4 textureWithAlpha = texture(ourTexture, outTexture);
        vec4 coloredAlpha = vec4(outColor,textureWithAlpha.a);

        if(coloredAlpha.a < 0.1) discard;

        FragColor = coloredAlpha;
    } else {
        FragColor = vec4(outColor, 1);
    }


}
