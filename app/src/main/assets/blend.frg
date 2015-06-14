precision highp float;

uniform vec4 AmbientGlobal;
uniform vec3 LightPosition;
uniform vec4 ObjectColor;

varying vec3 normal, halfVector, ecPos;

void main()
{
    vec4 color = AmbientGlobal;

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
        color += att * (ObjectColor * NdotL);

        vec3 halfV = normalize(halfVector);
        float NdotHV = max(dot(n,halfV),0.0);
        color += att * 0.4 * pow(NdotHV, 100.0);
        color.a = ObjectColor.a;
    }

    gl_FragColor = color;
}