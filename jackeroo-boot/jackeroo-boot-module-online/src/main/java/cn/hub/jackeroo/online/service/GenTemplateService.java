package cn.hub.jackeroo.online.service;

import cn.hub.jackeroo.online.dao.GenTemplateDao;
import cn.hub.jackeroo.online.entity.GenTemplate;
import cn.hub.jackeroo.vo.Grid;
import cn.hub.jackeroo.vo.GridParam;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 代码模板Service
 * @author
 * @version 2013-10-15
 */
@Service
public class GenTemplateService {

	@Resource
	private GenTemplateDao genTemplateDao;
	
	public GenTemplate get(Integer id) {
		return genTemplateDao.get(id);
	}
	
	public Grid find(GridParam gridParam, GenTemplate genTemplate) {
		/*genTemplate.setPage(page);
		page.setList(genTemplateDao.findList(genTemplate));
		return page;*/

		Grid grid = new Grid();

		//Page page = PageUtils.offsetPage(gridParam);
		genTemplateDao.findList(genTemplate);

		//grid.setPage(page);
		return grid;
	}
	
	public void save(GenTemplate genTemplate) {
		if (genTemplate.getContent()!=null){
			genTemplate.setContent(StringEscapeUtils.unescapeHtml4(genTemplate.getContent()));
		}
		if (genTemplate.getId()==null){
			//genTemplate.preInsert();
			genTemplateDao.insert(genTemplate);
		}else{
			//genTemplate.preUpdate();
			genTemplateDao.update(genTemplate);
		}
	}
	
	public void delete(GenTemplate genTemplate) {
		genTemplateDao.delete(genTemplate.getId());
	}
	
}