#define M_PI 3.1415926535897932384626433832795
uniform vec4 color;

void main() {
    gl_FragColor = vec4(color.x, color.y, color.z, color.w);
}
