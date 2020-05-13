package cn.hub.jackeroo.online.dao;

import cn.hub.jackeroo.online.model.GenTableColumn;
import cn.hub.jackeroo.persistence.CrudDao;

/**
 * 业务表字段DAO接口
 * @author
 * @version 2013-10-15
 */
public interface GenTableColumnDao extends CrudDao<GenTableColumn, Integer> {
	
	public void deleteByGenTableId(Integer genTableId);
}
