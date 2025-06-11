package foxiwhitee.hellmod.client.render;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;

public class AnimatedHeightTexture extends AbstractTexture {
    protected final ResourceLocation textureLocation;

    private final int index;

    public AnimatedHeightTexture(ResourceLocation textureLocation, int index) {
        this.textureLocation = textureLocation;
        this.index = index;
    }

    public void loadTexture(IResourceManager p_110551_1_) throws IOException {
        deleteGlTexture();
        InputStream inputstream = null;
        try {
            IResource iresource = p_110551_1_.getResource(this.textureLocation);
            inputstream = iresource.getInputStream();
            BufferedImage bufferedimage = ImageIO.read(inputstream);
            bufferedimage = bufferedimage.getSubimage(0, this.index * 16, 16, 16);
            boolean flag = false;
            boolean flag1 = false;
            if (iresource.hasMetadata())
                try {
                    TextureMetadataSection texturemetadatasection = (TextureMetadataSection)iresource.getMetadata("texture");
                    if (texturemetadatasection != null) {
                        flag = texturemetadatasection.getTextureBlur();
                        flag1 = texturemetadatasection.getTextureClamp();
                    }
                } catch (RuntimeException runtimeException) {}
            TextureUtil.uploadTextureImageAllocate(getGlTextureId(), bufferedimage, flag, flag1);
        } finally {
            if (inputstream != null)
                inputstream.close();
        }
    }
}
