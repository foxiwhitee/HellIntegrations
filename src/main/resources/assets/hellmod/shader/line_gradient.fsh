#version 120

uniform sampler2D sampler;

uniform float uTime;

uniform vec2 uViewportSize;

uniform float uGradientWidth;

uniform vec3 uColors[8];
uniform float uPositions[8];
uniform int uGradientSize;
uniform int uColorSpace;

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

vec3 xyz2rgb(vec3 c) {
    vec3 v =  c / 100.0 * mat3(
    3.2406, -1.5372, -0.4986,
    -0.9689, 1.8758, 0.0415,
    0.0557, -0.2040, 1.0570
    );
    vec3 r;
    r.x = (v.r > 0.0031308) ? ((1.055 * pow(v.r, (1.0 / 2.4))) - 0.055) : 12.92 * v.r;
    r.y = (v.g > 0.0031308) ? ((1.055 * pow(v.g, (1.0 / 2.4))) - 0.055) : 12.92 * v.g;
    r.z = (v.b > 0.0031308) ? ((1.055 * pow(v.b, (1.0 / 2.4))) - 0.055) : 12.92 * v.b;
    return r;
}

vec3 lab2xyz(vec3 c) {
    float fy = (c.x + 16.0) / 116.0;
    float fx = c.y / 500.0 + fy;
    float fz = fy - c.z / 200.0;
    return vec3(
    95.047 * ((fx > 0.206897) ? fx * fx * fx : (fx - 16.0 / 116.0) / 7.787),
    100.000 * ((fy > 0.206897) ? fy * fy * fy : (fy - 16.0 / 116.0) / 7.787),
    108.883 * ((fz > 0.206897) ? fz * fz * fz : (fz - 16.0 / 116.0) / 7.787)
    );
}

float linstep(float edge0, float edge1, float x) {
    return clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
}

vec3 lab2rgb(vec3 c) {
    return xyz2rgb(lab2xyz(vec3(100.0 * c.x, 2.0 * 127.0 * (c.y - 0.5), 2.0 * 127.0 * (c.z - 0.5))));
}

void main() {
    vec4 ndcPos;
    ndcPos.xy = (2.0 * gl_FragCoord.xy) / (uViewportSize.xy) - 1.0;
    ndcPos.z = 2.0 * gl_FragCoord.z - 1.0;
    ndcPos.w = 1.0;

    vec4 fragPos = gl_ModelViewProjectionMatrixInverse * ndcPos;

    vec4 texSample = texture2D(sampler, gl_TexCoord[0].xy);

    float mixVal = mod(fragPos.x / uGradientWidth + uTime, 1.0);

    vec3 color = uColors[0];
    for (int i = 1; i < uGradientSize; ++i) {
        color = mix(color, uColors[i].rgb, linstep(uPositions[i - 1], uPositions[i], mixVal));
    }

    if (uColorSpace == 1) {
        color = hsv2rgb(color);
    } else if (uColorSpace == 2) {
        color = lab2rgb(color);
    }

    gl_FragColor = vec4(gl_Color.rgb * color, texSample.a);
}
