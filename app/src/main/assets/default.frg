precision highp float;

uniform vec4 AmbientGlobal;
uniform vec3 LightPosition;
uniform vec4 ObjectColor;

varying vec3 normal, halfVector, ecPos;
varying vec4 diffuse;

uniform sampler2D texture0;
varying vec2 texCoord;

void main()
{
    vec4 color = AmbientGlobal;

    vec4 textureColor = texture2D(texture0, texCoord);
    vec4 diffuseTex = mix( ObjectColor, textureColor, 0.5);

    /* a fragment shader can't write a verying variable, hence we need
    a new variable to store the normalized interpolated normal */
    vec3 n = normalize(normal);

    // Compute the ligt direction
    vec3 lightVec = LightPosition - ecPos;

    // compute the distance to the light source to a varying variable
    float dist = length(lightVec);

    /* compute the dot product between normal and ldir */
    float NdotL = max(dot(n,normalize(lightVec)),0.0);

    if (NdotL > 0.0)
    {
        float constAtt = 0.0;
        float linAtt = 0.1;
        float quadAtt = 0.1;

        float att = 1.0 / (constAtt +
        linAtt * dist +
        quadAtt * dist * dist);

        color += att * (diffuseTex * NdotL);

        vec3 halfV = normalize(halfVector);
        float NdotHV = max(dot(n,halfV),0.0);
        color += att * 0.5 * 0.8 * pow(NdotHV, 100.0);
        color.a = diffuse.a;
    }

    gl_FragColor = color;
}