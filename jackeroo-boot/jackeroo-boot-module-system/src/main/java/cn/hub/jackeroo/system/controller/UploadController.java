package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.utils.DateUtils;
import cn.hub.jackeroo.vo.FileVo;
import cn.hub.jackeroo.utils.PathUtil;
import cn.hub.jackeroo.vo.Json;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@RequestMapping("/upload")
@Controller
public class UploadController {

	/*@Value("${ueditor.imageUrlPrefix}")
	private String imageUrlPrefix;

	*//**
	 * 上传图片
	 * 
	 * @param request
	 * @return
	 *//*
	@RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
	@ResponseBody
	public Json uploadImg(HttpServletRequest request) {
		Json json = new Json();
		json.setSuccess(false);

		String source = "/uploadimages/";
		String date = DateUtils.getDate("yyyyMMdd");
		String path = PathUtil.initDirUpload(source, date);

		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

			List<FileVo> fileList = new ArrayList<>();// 用于存储保存的文件对象，当多图上传时，先检测所有图片后，再保存图片文件

			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {

				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					if (file.getSize() > 1024 * 1024 * 5) {
						json.setMsg("图片大小不得超过5Mb");
						return json;
					} else {
						String oldFileName = file.getOriginalFilename();
						String extenstion = oldFileName.substring(oldFileName.lastIndexOf(".")).toLowerCase();// 获取后缀名
						if (!extenstion.equals(".jpg") && !extenstion.equals(".jpeg") && !extenstion.equals(".bmp") && !extenstion.equals(".png")) {
							json.setMsg("图片格式不正确");
							return json;
						} else {
							String newFileName = UUID.randomUUID().toString();
							String newFilePath = path + "/" + newFileName + extenstion;
							File localFile = new File(newFilePath);

							FileVo vo = new FileVo();
							vo.setFile(file);
							vo.setLocalFile(localFile);
							vo.setFileName(source + "/" + date + "/" + newFileName + extenstion);

							fileList.add(vo);
						}
					}
				}
			}

			if (CollectionUtils.isNotEmpty(fileList)) {
				List<String> src = new ArrayList<>();
				for (FileVo vo : fileList) {
					try {
						vo.getFile().transferTo(vo.getLocalFile());

						src.add(vo.getFileName());
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}

				json.setData(src);
				json.setSuccess(true);
				return json;
			}
		}
		json.setMsg("请上传图片");
		return json;
	}*/
}
