package com.wcwy.company.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.ImgCompression;
import com.wcwy.common.base.utils.PicUtils;
import com.wcwy.company.config.CosUtils;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static com.wcwy.common.base.utils.ImageRemarkUtil.markImageByText;

/**
 * ClassName: FileContorller
 * Description:文件上传
 * date: 2022/9/1 17:04
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "文件上传接口")
@RequestMapping("/file")
@RestController
public class FileContorller {
    private final static Integer FILE_SIZE = 10;//文件上传限制大小
    private final static String FILE_UNIT = "M";//文件上传限制单位（B,K,M,G）
    @Autowired
    private CosUtils cosUtils;
    private String htmlPath = "/";

    @PostMapping("/images")
    @ApiOperation(value = "文件传输")
    @Log(title = "images", businessType = BusinessType.OTHER)
    public R uploadFile(MultipartFile file) throws Exception {
        if (ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            return R.fail("上传文件大小为空");
        }
        byte[] bytes = PicUtils.compressPicForScale(file.getBytes(), 120);

        boolean flag = checkFileSize(file.getSize(), FILE_SIZE, FILE_UNIT);
        if (!flag) {
            throw new RuntimeException("上传文件大小超出10M限制");
        }

        if (file == null) {
            return R.fail("上传文件失败:文件为空.");
        }
        String s = cosUtils.uploadFile(file, "/images");
        return R.success("上传成功", s);
    }

    @PostMapping("/batchUploadFile")
    @ApiOperation(value = "批量上传")
    @Log(title = "batchUploadFile", businessType = BusinessType.OTHER)
    public R batchUploadFile(MultipartFile[] files) {
        if (ObjectUtils.isEmpty(files) || files[0].getSize() <= 0) {
            return R.fail("上传文件大小为空");
        }
        int length = files.length;
        if (length > 5) {
            return R.fail("文件数据量限制为5张!");
        }
        for (MultipartFile file : files) {
            boolean flag = checkFileSize(file.getSize(), FILE_SIZE, FILE_UNIT);
            if (!flag) {
                throw new RuntimeException("上传文件大小超出10M限制");
            }
        }
        List<String> s = cosUtils.batchUploadFile(files, "/images");
        return R.success("上传成功", s);
    }
    @PostMapping("/tribe")
    @ApiOperation(value = "发帖上传")
    @Log(title = "发帖上传", businessType = BusinessType.OTHER)
    public R tribe(MultipartFile[] files) {
        if (ObjectUtils.isEmpty(files) || files[0].getSize() <= 0) {
            return R.fail("上传文件大小为空");
        }
        int length = files.length;
        if (length > 5) {
            return R.fail("文件数据量限制为5张!");
        }
        for (MultipartFile file : files) {
            boolean flag = checkFileSize(file.getSize(), FILE_SIZE, FILE_UNIT);
            if (!flag) {
                throw new RuntimeException("上传文件大小超出10M限制");
            }
        }
        List<String> s = cosUtils.batchUploadFile(files, "/tribe");
        return R.success("上传成功", s);
    }

    /**
     * @param len  文件长度
     * @param size 限制大小
     * @param unit 限制单位（B,K,M,G）
     * @描述 判断文件大小
     */
    public static boolean checkFileSize(Long len, int size, String unit) {
        double fileSize = 0;
        if ("B".equalsIgnoreCase(unit)) {
            fileSize = (double) len;
        } else if ("K".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1024;
        } else if ("M".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1048576;
        } else if ("G".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1073741824;
        }
        return !(fileSize > size);
    }

    @GetMapping("/deleteImages")
    @ApiOperation("删除文件")
    @Log(title = "删除文件", businessType = BusinessType.OTHER)
    public R deleteImages(@RequestParam("path") String path) {
        cosUtils.DelSingleFile(path);
        return R.success();
    }


    @PostMapping("/batchUploadFilesOY")
    @ApiOperation(value = "批量上传oy")
    public R batchUploadFilesOY(MultipartFile[] files) {
        if (ObjectUtils.isEmpty(files) || files[0].getSize() <= 0) {
            return R.fail("上传文件大小为空");
        }
        int length = files.length;
        if (length > 5) {
            return R.fail("文件数据量限制为5张!");
        }
        for (MultipartFile file : files) {
            boolean flag = checkFileSize(file.getSize(), FILE_SIZE, FILE_UNIT);
            if (!flag) {
                throw new RuntimeException("上传文件大小超出10M限制");
            }
        }
        List<String> s = cosUtils.batchUploadFile(files, "/OY");
        return R.success("上传成功", s);
    }

    @GetMapping("/customLogo")
    @ApiOperation(value = "自定义logo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", required = true, value = "企业名称"),
            @ApiImplicitParam(name = "images", required = true, value = "第几张图片 1:蓝 2黄 3青 4:橙"),
            @ApiImplicitParam(name = "alpha", required = true, value = "透明度0~1"),
            @ApiImplicitParam(name = "color", required = true, value = "颜色 1:蓝 2黄 3青 4:橙")
    })
    @Log(title = "自定义logo", businessType = BusinessType.OTHER)
    public R customLogo(@RequestParam(required = true, value = "name") String name, @RequestParam(required = true, value = "images") Integer images, @RequestParam(required = true, value = "alpha") Integer alpha, @RequestParam(required = true, value = "color") Integer color) {
        if (StringUtils.isEmpty(name) || name.length() != 4) {
            return R.fail("企业简称不能为空长度不能超过4个字符!");
        }
        if (StringUtils.isEmpty(images) || StringUtils.isEmpty(alpha) || StringUtils.isEmpty(color)) {
            return R.fail("数据有为空!");
        }
        String os = System.getProperties().getProperty("os.name");
        String srcImgPath = null;
        String targerTextPath = null; //添加文字水印之后的图片存放路径
        //Windows操作系统
        if (os != null && os.toLowerCase().startsWith("windows")) {
            /*  srcImgPath = TCompanyController.class.getResource("/static/"+images+".png").getPath();*/
            srcImgPath = "D:/static/" + images + ".png";
            targerTextPath = "D:/" + UUID.randomUUID() + ".png";
        } else if (os != null && os.toLowerCase().startsWith("linux")) {//Linux操作系统
            srcImgPath = Paths.get("/wx/" + images + ".png").toString();
            targerTextPath = "/wx/" + UUID.randomUUID() + ".png";
        } else { //其它操作系统
            System.out.println(String.format("当前系统版本是:%s", os));
        }
        //原始图片路径
        //添加的文本
        String logoText = "承正教育";

      /*  System.out.println(srcImgPath);
        File file1=new File(TCompanyController.class.getResource("/static/"+images+".png").getPath());
        boolean exists = file1.exists();
        System.out.println(exists);*/
        /*String targerTextPath2 = "E:/cc/22.png";//添加文字 文字旋转-45 水印之后的图片存放路径*/
        System.out.println(targerTextPath);
        System.out.println("给图片添加水印文字开始...");
        // 给图片添加水印文字
        InputStream inputStream = markImageByText(name, srcImgPath, targerTextPath, null, alpha, color);
        String logo = cosUtils.customLogo(inputStream, "/customLogo", "承正教育");
        File file = new File(targerTextPath);
        file.delete();
        return R.success(logo);
    }

    @PostMapping(value = "/html")
    @ApiOperation("支持pdf,docx,doc转html")
    @Log(title = "支持pdf,docx,doc转html", businessType = BusinessType.OTHER)
    public void html(HttpServletResponse response, MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String[] filename = originalFilename.split("\\.");
        String wordName = filename[1];
        String absolutePath = "";
        if ("docx".equals(wordName)) {
            absolutePath = Word2007ToHtml(file);
        } else if ("doc".equals(wordName)) {
            absolutePath = Word2003ToHtml(file);
        } else if ("pdf".equals(wordName)) {
            try {
                StringBuffer buffer = new StringBuffer();
                PDDocument doc = PDDocument.load(file.getInputStream());
                //遍历处理pdf附件
                buffer.append("<!doctype html>\r\n");
                buffer.append("<head>\r\n");
                buffer.append("<meta charset=\"UTF-8\">\r\n");
                buffer.append("</head>\r\n");
                buffer.append("<body>\r\n");
                buffer.append("<style>\r\n");
                buffer.append("img {background-color:#fff; text-align:center; width:100%; max-width:100%;}\r\n");
                buffer.append("</style>\r\n");
                int size = doc.getNumberOfPages();
                PDFRenderer reader = new PDFRenderer(doc);
                for (int i = 0; i < size; i++) {
                    BufferedImage bufferedImage = reader.renderImage(i, 3f);
                    //文件临时存储目录
                    //   String location = System.getProperty("user.dir") + "/" + dirTmp;
                    String location = "";
                    //生成图片,保存位置
                    FileOutputStream out = new FileOutputStream(location + "/" + i + ".jpg");
                    ImageIO.write(bufferedImage, "jpg", out);
                    String encode = Base64.encode(new File(location + "/" + i + ".jpg"));
                    //将图片路径追加到网页文件里
                    buffer.append("<img src=\"data:image/jpg;base64," + encode + "\"/>\r\n");
                    out.close();
                    FileUtil.del(location + "/" + i + ".jpg");
                }
                doc.close();
                buffer.append("</body>\r\n");
                buffer.append("</html>");
                InputStream inputStream = IOUtils.toInputStream(buffer.toString(), Charset.defaultCharset());
                response.reset();
                // response.setContentType("");
                response.setHeader("Content-Disposition", "inline; filename=" + URLEncoder.encode(IdWorker.getIdStr(), "UTF-8"));
                byte[] buf = new byte[1024];
                int len;
                OutputStream outputStream = response.getOutputStream();
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                inputStream.close();
                outputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else {
            return;
        }
        if (!StringUtils.isEmpty(absolutePath)) {
            File file1 = new File(absolutePath);
            InputStream inputStream = new FileInputStream(file1);
            OutputStream outputStream = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }

            inputStream.close();
            outputStream.close();
            boolean delete = file1.delete();
        }
    }


    /**
     * 将word2003转换为html文件
     *
     * @throws IOException
     * @throws TransformerException
     * @throws ParserConfigurationException
     */
    public String Word2003ToHtml(MultipartFile file)
            throws IOException, TransformerException, ParserConfigurationException {
        String originalFilename = file.getOriginalFilename();
        String[] filename = originalFilename.split("\\.");
        String wordName = filename[0] + UUID.randomUUID();
        String htmlName = wordName + ".html";
        final String imagePath = htmlPath + "image" + File.separator;
        // 判断html文件是否存在
        File htmlFile = new File(htmlPath + htmlName);
        if (htmlFile.exists()) {
            return htmlFile.getAbsolutePath();
        }
        // 原word文档
        File file2 = transferToFile(file);
        InputStream input = new FileInputStream(file2);
        HWPFDocument wordDocument = new HWPFDocument(input);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        // 设置图片存放的位置
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            @Override
            public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches,
                                      float heightInches) {
                File imgPath = new File(imagePath);
                if (!imgPath.exists()) {// 图片目录不存在则创建
                    imgPath.mkdirs();
                }
                File file = new File(imagePath + suggestedName);
                try {
                    OutputStream os = new FileOutputStream(file);
                    os.write(content);
                    os.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 图片在html文件上的路径 相对路径
                return "image/" + suggestedName;
            }
        });
        // 解析word文档
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();
        // 生成html文件上级文件夹
        File folder = new File(htmlPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        OutputStream outStream = new FileOutputStream(htmlFile);
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer serializer = factory.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        outStream.close();
        input.close();
        return htmlFile.getAbsolutePath();
    }

    /**
     * 2007版本word转换成html
     *
     * @return
     * @throws IOException
     */
    public String Word2007ToHtml(MultipartFile file)
            throws IOException {
        String originalFilename = file.getOriginalFilename();
        String[] filename = originalFilename.split("\\.");
        String wordName = filename[0] + UUID.randomUUID();
        String htmlName = wordName + ".html";
        String imagePath = htmlPath + "image" + File.separator;
        // 判断html文件是否存在
        File htmlFile = new File(htmlPath + htmlName);
        if (htmlFile.exists()) {
            return htmlFile.getAbsolutePath();
        }
        // word文件
        File file2 = transferToFile(file);

        // 1) 加载word文档生成 XWPFDocument对象
        InputStream in = new FileInputStream(file2);
        XWPFDocument document = new XWPFDocument(in);
        // 2) 解析 XHTML配置 (这里设置IURIResolver来设置图片存放的目录)
        File imgFolder = new File(imagePath);
        XHTMLOptions options = XHTMLOptions.create();
        options.setExtractor(new FileImageExtractor(imgFolder));
        // html中图片的路径 相对路径
        options.URIResolver(new BasicURIResolver("image"));
        options.setIgnoreStylesIfUnused(false);
        options.setFragment(true);
        // 3) 将 XWPFDocument转换成XHTML
        // 生成html文件上级文件夹
        File folder = new File(htmlPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        OutputStream out = new FileOutputStream(htmlFile);
        XHTMLConverter.getInstance().convert(document, out, options);
        out.close();
        in.close();
        return htmlFile.getAbsolutePath();
    }

    public File transferToFile(MultipartFile multipartFile) {
//        选择用缓冲区来实现这个转换即使用java 创建的临时文件 使用 MultipartFile.transferto()方法 。
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filename = originalFilename.split("\\.");
            file = File.createTempFile(filename[0], "." + filename[1]);
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @PostMapping("/offer")
    @ApiOperation(value = "发送offer")
    @Log(title = "offer", businessType = BusinessType.OTHER)
    public R uploadOfferFile(MultipartFile file) {
        if (ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            return R.fail("上传文件大小为空");
        }
        boolean flag = checkFileSize(file.getSize(), FILE_SIZE, FILE_UNIT);
        if (!flag) {
            throw new RuntimeException("上传文件大小超出10M限制");
        }
        if (file == null) {
            return R.fail("上传文件失败:文件为空.");
        }
        String s = cosUtils.uploadFile(file, "/offer");
        return R.success("上传成功", s);
    }

    @PostMapping("/resume")
    @ApiOperation(value = "简历上传")
    @Log(title = "offer", businessType = BusinessType.OTHER)
    public R resume(MultipartFile file) {
        if (ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            return R.fail("上传文件大小为空");
        }

        boolean flag = checkFileSize(file.getSize(), FILE_SIZE, FILE_UNIT);
        if (!flag) {
            throw new RuntimeException("上传文件大小超出10M限制");
        }
        String originalFilename = file.getOriginalFilename();
        String[] filename = originalFilename.split("\\.");
        String wordName = filename[1];

        if (!"pdf".equals(wordName) &&  !"docx".equals(wordName) && !"doc".equals(wordName) && !"png".equals(wordName) && !"jpg".equals(wordName)) {
            return R.fail("请上传docx,doc,pdf 文件类型！");
        }

        String s = cosUtils.uploadFile(file, "/resume");
        return R.success("上传成功", s);
    }

    @RequestMapping(value = "/img", method = RequestMethod.POST)
    public String imgUploads(@RequestParam("file") MultipartFile file) throws IOException {
        //压缩图片到指定120K以内,不管你的图片有多少兆,都不会超过120kb,精度还算可以,不会模糊
        byte[] bytes = PicUtils.compressPicForScale(file.getBytes(), 120);
        //生成保存在服务器的图片名称，统一修改原后缀名为:jpg
        String newFileName = UUID.randomUUID() + ".jpg";
        InputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        String s = cosUtils.filesUpload(byteArrayInputStream, "/resume", newFileName);
        return s;
  /*      File fOut = new File("E:\\image\\copy\\" + newFileName);
      //  fOut.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(fOut);
        fileOutputStream.write(bytes);
        fileOutputStream.close();
        return newFileName;*/
    }

    @RequestMapping(value = "/img2", method = RequestMethod.POST)
    public String imgUploads2(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] bs = ImgCompression.getImageCom(file);
        String newFileName = UUID.randomUUID() + ".jpg";
        File fOut = new File("E:\\image\\copy\\" + newFileName);
        fOut.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(fOut);
        fileOutputStream.write(bs);
        fileOutputStream.close();
        return newFileName;
    }

    @PostMapping("/app")
    @ApiOperation(value = "小程序文件上传")
    @Log(title = "小程序文件上传", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "files", required = true, value = "文件集合 "),
            @ApiImplicitParam(name = "path", required = true, value = "自定义路径如(oy)")
    })
    public R app(MultipartFile[] files,String path) {
        if (ObjectUtils.isEmpty(files) || files[0].getSize() <= 0) {
            return R.fail("上传文件大小为空");
        }
        int length = files.length;
        if (length > 5) {
            return R.fail("文件数据量限制为5张!");
        }
        for (MultipartFile file : files) {
            boolean flag = checkFileSize(file.getSize(), FILE_SIZE, FILE_UNIT);
            if (!flag) {
                throw new RuntimeException("上传文件大小超出10M限制");
            }
        }
        List<String> s = cosUtils.batchUploadFileApp(files, "/"+path);
        return R.success("上传成功", s);
    }


}
