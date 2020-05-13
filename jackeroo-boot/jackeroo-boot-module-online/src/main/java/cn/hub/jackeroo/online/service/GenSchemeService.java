package cn.hub.jackeroo.online.service;

import cn.hub.jackeroo.online.dao.GenSchemeDao;
import cn.hub.jackeroo.online.dao.GenTableColumnDao;
import cn.hub.jackeroo.online.dao.GenTableDao;
import cn.hub.jackeroo.online.model.GenConfig;
import cn.hub.jackeroo.online.model.GenScheme;
import cn.hub.jackeroo.online.model.GenTable;
import cn.hub.jackeroo.online.model.GenTableColumn;
import cn.hub.jackeroo.online.model.GenTemplate;
import cn.hub.jackeroo.online.utils.GenUtils;
import cn.hub.jackeroo.persistence.CrudService;
import cn.hub.jackeroo.utils.PageUtils;
import cn.hub.jackeroo.vo.Grid;
import cn.hub.jackeroo.vo.GridParam;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 生成方案Service
 * @author
 * @version 2013-10-15
 */
@Service
public class GenSchemeService extends CrudService<GenSchemeDao, GenScheme, Integer> {

	@Resource
	private GenSchemeDao genSchemeDao;
//	@Autowired
//	private GenTemplateDao genTemplateDao;
	@Resource
	private GenTableDao genTableDao;
	@Resource
	private GenTableColumnDao genTableColumnDao;
	
	public GenScheme get(Integer id) {
		return genSchemeDao.get(id);
	}
	
	public Grid find(GridParam gridParam, GenScheme genScheme) {
		GenUtils.getTemplatePath();

		Grid grid = new Grid();

		Page<GenScheme> page = PageUtils.offsetPage(gridParam);
		genSchemeDao.findList(genScheme);

		grid.setPage(page);
		return grid;
	}
	
	public String saveScheme(GenScheme genScheme) {
		if (genScheme.getId()==null){
			genScheme.preInsert();
			genSchemeDao.insert(genScheme);
		}else{
			genScheme.preUpdate();
			genSchemeDao.update(genScheme);
		}
		// 生成代码
		if ("1".equals(genScheme.getFlag())){
			return generateCode(genScheme);
		}
		return "";
	}
	
	public void delete(GenScheme genScheme) {
		genSchemeDao.delete(genScheme.getId());
	}
	
	private String generateCode(GenScheme genScheme){

		StringBuilder result = new StringBuilder();
		
		// 查询主表及字段列
		GenTable genTable = genTableDao.get(genScheme.getGenTable().getId());
		genTable.setColumnList(genTableColumnDao.findList(new GenTableColumn(new GenTable(genTable.getId()))));
		
		// 获取所有代码模板
		GenConfig config = GenUtils.getConfig();
		
		// 获取模板列表
		List<GenTemplate> templateList = GenUtils.getTemplateList(config, genScheme.getCategory(), false);
		List<GenTemplate> childTableTemplateList = GenUtils.getTemplateList(config, genScheme.getCategory(), true);
		
		// 如果有子表模板，则需要获取子表列表
		if (childTableTemplateList.size() > 0){
			GenTable parentTable = new GenTable();
			parentTable.setParentTable(genTable.getName());
			genTable.setChildList(genTableDao.findList(parentTable));
		}
		
		// 生成子表模板代码
		for (GenTable childTable : genTable.getChildList()){
			childTable.setParent(genTable);
			childTable.setColumnList(genTableColumnDao.findList(new GenTableColumn(new GenTable(childTable.getId()))));
			genScheme.setGenTable(childTable);
			Map<String, Object> childTableModel = GenUtils.getDataModel(genScheme);
			for (GenTemplate tpl : childTableTemplateList){
				result.append(GenUtils.generateToFile(tpl, childTableModel, genScheme.getReplaceFile()));
			}
		}
		
		// 生成主表模板代码
		genScheme.setGenTable(genTable);
		Map<String, Object> model = GenUtils.getDataModel(genScheme);
		for (GenTemplate tpl : templateList){
			result.append(GenUtils.generateToFile(tpl, model, genScheme.getReplaceFile()));
		}
		/*for (int i = 0; i < templateList.size(); i++){
			System.out.println("==================================************************开始生成" + templateList.get(i).getName() + "页面");
			if(templateList.get(i).getName().equals("mapper")){
				result.append(GenUtils.generateToFile(templateList.get(i), model, genScheme.getReplaceFile()));
			}
			System.out.println("==================================************************生成" + templateList.get(i).getName() + "页面完成");
		}*/
		return result.toString();
	}
}
