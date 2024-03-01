package com.wcwy.common.base.utils;

/**
 * ClassName: ImageRemarkUtil
 * Description:
 * date: 2023/3/1 11:43
 *
 * @author tangzhuo
 * @since JDK 1.8
 */


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * @author tqf
 * @version 创建时间：2020-4-3 上午10:49:02
 * 类说明:图片添加水印   文字&图片水印
 */
public class ImageRemarkUtil {

    // 水印透明度
    private static float alpha = 1f;
    // 水印横向位置
    private static int positionWidth = 150;
    // 水印纵向位置
    private static int positionHeight = 300;
    // 水印文字字体
    private static Font font = new Font("宋体", Font.BOLD, 35);
    // 水印文字颜色
    private static Color color = Color.red;

    /**
     * @param alpha          水印透明度
     * @param positionWidth  水印横向位置
     * @param positionHeight 水印纵向位置
     * @param font           水印文字字体
     * @param color          水印文字颜色
     */
    public static void setImageMarkOptions(float alpha, int positionWidth, int positionHeight, Font font, Color color) {
        if(alpha != 0.0f){
            ImageRemarkUtil.alpha = alpha;
        }

        if(positionWidth != 0){
            ImageRemarkUtil.positionWidth = positionWidth;
        }

        if(positionHeight != 0){
            ImageRemarkUtil.positionHeight = positionHeight;
        }

        if(font != null){
            ImageRemarkUtil.font = font;
        }

        if (color != null){
            ImageRemarkUtil.color = color;
        }

    }


    /**
     * 给图片添加水印文字、可设置水印文字的旋转角度
     *
     * @param logoText
     * @param srcImgPath
     * @param targerPath
     * @param degree
     */
    public static InputStream  markImageByText(String logoText, String srcImgPath,
                                       String targerPath, Integer degree,Integer alpha1 ,Integer coloIid) {
       alpha= alpha1;
        if(coloIid ==1){
             color = new Color( 120, 148, 206);
        }else if(coloIid ==2){
             color = new Color( 254, 168, 10);
        }else  if(coloIid ==3){

             color = new Color( 41, 226, 204);
        }else if(coloIid ==4){
             color = new Color(252, 76, 12);
        }
        InputStream is = null;
        OutputStream os = null;
        try {
            // 1、源图片
            Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
           /* int width1 = buffImg.getWidth();//获得宽度
            int height1 = buffImg.getHeight();//获得高度*/
      /*      System.out.println(width1);
            System.out.println(height1);
            BufferedImage buffImg = buffImg1.getSubimage(1, 1, width1-1, height1-1);*/
            // 2、得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 3、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
            // 4、设置水印旋转
            if (null != degree) {
                g.rotate(Math.toRadians(degree),
                        (double) buffImg.getWidth() / 2,
                        (double) buffImg.getHeight() / 2);
            }
            // 5、设置水印文字颜色
            g.setColor(color);
            // 6、设置水印文字Font

            g.setFont(new Font("微软雅黑", Font.BOLD, 20));

            // 7、设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
            int width = srcImg.getWidth(null) - 50;
            int heigth = srcImg.getHeight(null) - 35;

            String substring = logoText.substring(0, 2);
            System.out.println("11111111"+ substring);
            g.drawString(substring, width, heigth);
            // 9、释放资源
            g.dispose();
            // 10、生成图片
            os = new FileOutputStream(targerPath);

            ImageIO.write(buffImg, "png", os);

            System.out.println("图片完成添加水印文字");
            System.out.println(logoText);
            String substring1 = logoText.substring(2, 4);
            System.out.println("22222"+substring1);
            return   markImageByText1(substring1,targerPath,targerPath,degree);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is){
                    is.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != os){
                    os.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 给图片添加水印文字、可设置水印文字的旋转角度
     *
     * @param logoText
     * @param srcImgPath
     * @param targerPath
     * @param degree
     */
    public static InputStream markImageByText1(String logoText,  String srcImgPath,
                                        String targerPath, Integer degree) {

        InputStream is = null;
        OutputStream os = null;

        try {
            // 1、源图片

            Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
            // 2、得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 3、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
            // 4、设置水印旋转
            if (null != degree) {
                g.rotate(Math.toRadians(degree),
                        (double) buffImg.getWidth() / 2,
                        (double) buffImg.getHeight() / 2);
            }
            // 5、设置水印文字颜色
            g.setColor(color);
            // 6、设置水印文字Font
            g.setFont(new Font("微软雅黑", Font.BOLD, 20));
            // 7、设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
            int width = srcImg.getWidth(null) - 50;
            int heigth = srcImg.getHeight(null) - 13;
            g.drawString(logoText, width, heigth);
            // 9、释放资源
            g.dispose();
            // 10、生成图片
         /*   os = new FileOutputStream(targerPath);
            ImageIO.write(buffImg, "png", os);*/
            System.out.println("图片完成添加水印文字");

            return    bufferedImageToInputStream(buffImg);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is){
                    is.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != os){
                    os.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

/*    public static void main(String[] args) {
        //原始图片路径
        String srcImgPath = "E:/cc/2.png";  //只需要这一张图片 然后生成4张图片
        //添加的文本
        String logoText = "承正";

        String targerTextPath = "E:/cc/21.png";  //添加文字水印之后的图片存放路径
        *//*String targerTextPath2 = "E:/cc/22.png";//添加文字 文字旋转-45 水印之后的图片存放路径*//*

        System.out.println("给图片添加水印文字开始...");
        // 给图片添加水印文字
        InputStream inputStream = markImageByText(logoText, srcImgPath, targerTextPath, null);

     *//*   // 给图片添加水印文字,水印文字旋转-45
        markImageByText(logoText, srcImgPath, targerTextPath2, 45);
        System.out.println("给图片添加水印文字结束...");*//*


    }*/

    /**
     * 将BufferedImage转换为InputStream
     * @param image
     * @return
     */
    public static InputStream bufferedImageToInputStream(BufferedImage image){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
            InputStream input = new ByteArrayInputStream(os.toByteArray());
            return input;
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }


}

