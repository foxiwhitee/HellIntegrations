package foxiwhitee.hellmod.integration.draconic.client.shaders;

import org.lwjgl.opengl.ARBShaderObjects;

public class AlphaShader extends ShaderProgram {
    protected int uColor;

    public AlphaShader() {
        super(null, "alpha");
        this.uColor = ARBShaderObjects.glGetUniformLocationARB(this.programID, "color");
    }

    public void setColor(float r, float g, float b, float a) {
        ARBShaderObjects.glUniform4fARB(this.uColor, r, g, b, a);
    }
}

