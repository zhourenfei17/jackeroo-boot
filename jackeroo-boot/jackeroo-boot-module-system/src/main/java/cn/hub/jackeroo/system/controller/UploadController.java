package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.utils.DateUtils;
import cn.hub.jackeroo.utils.PathUtil;
import cn.hub.jackeroo.vo.FileVo;
import cn.hub.jackeroo.vo.Result;
import com.github.xiaoymin.swaggerbootstrapui.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequestMapping("/upload")
@Controller
public class UploadController extends BaseController {

	@Value("${jackeroo.path.upload}")
	private String basePath;

	/**
	 * 上传图片
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
	@ResponseBody
	public Result uploadImg(HttpServletRequest request) {
        String date = DateUtils.getDate("yyyyMMdd");
        String imagePath = "/images/" + date;
		String path = PathUtil.initUpload(basePath, imagePath);

		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

			List<FileVo> fileList = new ArrayList<>();// 用于存储保存的文件对象，当多图上传时，先检测所有图片后，再保存图片文件

			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {

				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					if (file.getSize() > 1024 * 1024 * 5) {
						return error("图片大小不得超过5Mb");
					} else {
						String oldFileName = file.getOriginalFilename();
						String extenstion = oldFileName.substring(oldFileName.lastIndexOf(".")).toLowerCase();// 获取后缀名
						if (!extenstion.equals(".jpg") && !extenstion.equals(".jpeg") && !extenstion.equals(".bmp") && !extenstion.equals(".png")) {
							return error("图片格式不正确");
						} else {
							String newFileName = UUID.randomUUID().toString();
							String newFilePath = path + File.separator + newFileName + extenstion;

							FileVo vo = new FileVo();
							vo.setFile(file);
							vo.setLocalPath(Paths.get(newFilePath));
							vo.setFileName(imagePath + File.separator + newFileName + extenstion);

							fileList.add(vo);
						}
					}
				}
			}

			if (CollectionUtils.isNotEmpty(fileList)) {
				List<String> src = new ArrayList<>();
				for (FileVo vo : fileList) {
					try {
						vo.getFile().transferTo(vo.getLocalPath());

						src.add(vo.getFileName());
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}

				return ok(src);
			}
		}
		return error("请上传图片");
	}

    /**
     * 上传
     * @param request
     */
    @PostMapping(value = "/upload")
    public Result upload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("avatar");// 获取上传文件对象
        String orgName = file.getOriginalFilename();// 获取文件名


        log.info("图片文件名：{}", orgName);
        System.out.println(String.format("图片文件名：%s", orgName));

        return ok();
    }
}
