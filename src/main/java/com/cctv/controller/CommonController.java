package com.cctv.controller;

import com.cctv.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        log.info(file.toString());
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.indexOf("."));
        log.info("文件名后缀:{}",suffix);
        String fileName = UUID.randomUUID() + suffix;
        File dir = new File(basePath + fileName);
        if(!dir.exists()){
            dir.mkdirs();
        }
        try {
            //将临时文件转储到指定位置
            file.transferTo(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * we
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        InputStream in = null;
        ServletOutputStream outputStream = null;
        try {
            in = new FileInputStream(basePath + name);
            outputStream = response.getOutputStream();
            response.setContentType("type/jpeg");
            byte[] bytes = new byte[1024];
            while ((in.read(bytes)) != -1){
                outputStream.write(bytes);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
