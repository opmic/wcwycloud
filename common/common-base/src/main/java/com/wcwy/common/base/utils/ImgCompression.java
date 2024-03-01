package com.wcwy.common.base.utils;

/**
 * ClassName: ImgCompression
 * Description:
 * date: 2023/6/20 10:30
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImgCompression {

    public static byte[] getImageCom(MultipartFile file) throws IOException {
        //获取文件输入流
        InputStream inputStream = file.getInputStream();
        try {
            // 把图片读入到内存中
            BufferedImage bufImg = ImageIO.read(inputStream);
            // 压缩代码,存储图片文件byte数组
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //防止图片变红,这一步非常重要
            BufferedImage bufferedImage = new BufferedImage(bufImg.getWidth(), bufImg.getHeight(), BufferedImage.TYPE_INT_RGB);
            bufferedImage.createGraphics().drawImage(bufImg,0,0, Color.WHITE,null);
            //先转成jpg格式来压缩,然后在通过OSS来修改成源文件本来的后缀格式
            ImageIO.write(bufferedImage,"jpg",bos);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            inputStream.close();
        }
        return null;
    }
}
