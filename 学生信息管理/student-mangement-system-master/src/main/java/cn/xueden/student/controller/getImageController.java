package cn.xueden.student.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


@RestController
public class getImageController {

    // 用户头像存放路径配置（从 application.yml 或 application.properties 读取）
    @Value("${user.icon}")
    private String userIconPath;

    /**
     * 获取图片资源接口
     */
    @GetMapping("/uploadFile/{imgUrl:.+}") // 使用 .+ 来匹配路径中的斜杠
    public ResponseEntity<InputStreamResource> getImage(@PathVariable("imgUrl") String imgUrl) {
        try {
            // 构建完整的文件路径
            File imgFile = new File(userIconPath, imgUrl);

            if (!imgFile.exists() || !imgFile.isFile()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // 创建输入流
            InputStreamResource resource = new InputStreamResource(new FileInputStream(imgFile));

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imgFile.getName() + "\"");
            headers.setContentType(MediaType.IMAGE_JPEG); // 根据实际情况调整MIME类型

            // 返回响应实体
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(imgFile.length())
                    .body(resource);

        } catch (FileNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
