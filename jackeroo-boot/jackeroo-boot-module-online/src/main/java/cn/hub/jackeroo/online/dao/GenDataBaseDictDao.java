package cn.hub.jackeroo.online.dao;

import cn.hub.jackeroo.online.model.GenTable;
import cn.hub.jackeroo.online.model.GenTableColumn;
import cn.hub.jackeroo.persistence.CrudDao;

import java.util.List;

/**
 * 业务表字段DAO接口
 * @author
 * @version 2013-10-15
 */
public interface GenDataBaseDictDao extends CrudDao<GenTableColumn, Integer> {

	/**
	 * 查询表列表
	 * @param genTable
	 * @return
	 */
	List<GenTable> findTableList(GenTable genTable);

	/**
	 * 获取数据表字段
	 * @param genTable
	 * @return
	 */
	List<GenTableColumn> findTableColumnList(GenTable genTable);
	
	/**
	 * 获取数据表主键
	 * @param genTable
	 * @return
	 */
	List<String> findTablePK(GenTable genTable);
	
}
