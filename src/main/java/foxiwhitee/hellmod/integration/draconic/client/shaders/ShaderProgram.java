package foxiwhitee.hellmod.integration.draconic.client.shaders;

import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

import foxiwhitee.hellmod.HellCore;
import org.lwjgl.opengl.ARBShaderObjects;

public class ShaderProgram {
    protected int programID = ARBShaderObjects.glCreateProgramObjectARB();

    public ShaderProgram(String vertexName, String fragmentName) {
        if (vertexName != null) {
            String code = (new Scanner(Objects.<InputStream>requireNonNull(getClass().getClassLoader().getResourceAsStream("assets/" + HellCore.MODID + "/shader/" + vertexName + ".vsh")))).useDelimiter("\\A").next();
            int shaderID = ARBShaderObjects.glCreateShaderObjectARB(35633);
            ARBShaderObjects.glShaderSourceARB(shaderID, code);
            ARBShaderObjects.glCompileShaderARB(shaderID);
            if (ARBShaderObjects.glGetObjectParameteriARB(shaderID, 35713) == 0)
                throw new RuntimeException("Vertex shader " + vertexName + " compilation error!\n" +
                        ARBShaderObjects.glGetInfoLogARB(shaderID,
                                ARBShaderObjects.glGetObjectParameteriARB(shaderID, 35716)));
            ARBShaderObjects.glAttachObjectARB(this.programID, shaderID);
        }
        if (fragmentName != null) {
            String code = (new Scanner(Objects.<InputStream>requireNonNull(getClass().getClassLoader().getResourceAsStream("assets/" + HellCore.MODID + "/shader/" + fragmentName + ".fsh")))).useDelimiter("\\A").next();
            int shaderID = ARBShaderObjects.glCreateShaderObjectARB(35632);
            ARBShaderObjects.glShaderSourceARB(shaderID, code);
            ARBShaderObjects.glCompileShaderARB(shaderID);
            if (ARBShaderObjects.glGetObjectParameteriARB(shaderID, 35713) == 0)
                throw new RuntimeException("Fragment shader " + vertexName + " compilation error!\n" +
                        ARBShaderObjects.glGetInfoLogARB(shaderID,
                                ARBShaderObjects.glGetObjectParameteriARB(shaderID, 35716)));
            ARBShaderObjects.glAttachObjectARB(this.programID, shaderID);
        }
        ARBShaderObjects.glLinkProgramARB(this.programID);
        if (ARBShaderObjects.glGetObjectParameteriARB(this.programID, 35714) == 0)
            throw new RuntimeException("Fragment shader " + vertexName + " compilation error!\n" +
                    ARBShaderObjects.glGetInfoLogARB(this.programID,
                            ARBShaderObjects.glGetObjectParameteriARB(this.programID, 35716)));
        ARBShaderObjects.glValidateProgramARB(this.programID);
        if (ARBShaderObjects.glGetObjectParameteriARB(this.programID, 35715) == 0)
            throw new RuntimeException("Fragment shader " + vertexName + " compilation error!\n" +
                    ARBShaderObjects.glGetInfoLogARB(this.programID,
                            ARBShaderObjects.glGetObjectParameteriARB(this.programID, 35716)));
    }

    public void use() {
        ARBShaderObjects.glUseProgramObjectARB(this.programID);
    }

    public void stopUsing() {
        ARBShaderObjects.glUseProgramObjectARB(0);
    }
}
