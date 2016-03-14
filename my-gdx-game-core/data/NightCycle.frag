#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

uniform sampler2D u_texture;

uniform vec4 ambientColor;

varying vec4 vColor;
varying vec2 vTexCoord;

void main() {
  vec4 texColor = texture2D(u_texture, vTexCoord);
  gl_FragColor = vec4(texColor.r * ambientColor.r, texColor.g * ambientColor.g, texColor.b * ambientColor.b, texColor.a);//  gray.r, gray.g, gray.b, texColor.a * ambientColor.r);
}