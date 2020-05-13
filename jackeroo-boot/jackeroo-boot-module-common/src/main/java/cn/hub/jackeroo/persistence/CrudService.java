package cn.hub.jackeroo.persistence;

import cn.hub.jackeroo.vo.Grid;
import cn.hub.jackeroo.vo.GridParam;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * Service基类
 * 
 * @author
 * @version 2014-05-16
 */
public abstract class CrudService<D extends CrudDao<T, Pk>, T extends DataEntity<T, Pk>, Pk extends Serializable> {

	/**
	 * 持久层对象
	 */
	@Resource
	protected D dao;

	/**
	 * 获取单条数据
	 * 
	 * @param id
	 * @return
	 */
	public T get(Pk id) {
		return dao.get(id);
	}

	/**
	 * 获取单条数据
	 * 
	 * @param entity
	 * @return
	 */
	public T get(T entity) {
		return dao.get(entity);
	}

	/**
	 * 查询列表数据
	 * 
	 * @param entity
	 * @return
	 */
	public List<T> findList(T entity) {
		return dao.findList(entity);
	}

	/**
	 * @Description: 查询所有列表数据
	 * @param entity
	 * @return
	 * @author
	 * @date 2016年10月15日
	 */
	public List<T> findAllList(T entity) {
		return dao.findAllList(entity);
	}

	/**
	 * 保存数据（插入或更新）
	 * 
	 * @param entity
	 */
	public void save(T entity) {
		if (entity.getIsNewRecord()) {
			entity.preInsert();
			dao.insert(entity);
		} else {
			entity.preUpdate();
			dao.update(entity);
		}
	}

	/**
	 * 删除数据
	 * 
	 * @param entity
	 */
	public void delete(T entity) {
		dao.delete(entity);
	}

	public void delete(Pk id) {
		dao.delete(id);
	}

	/**
	 * @description 查询并返回列表数据
	 * @param
	 * @author
	 * @date 2018/1/2
	 * @return
	 */
	public Grid findPage(T entity, GridParam param) {
		Grid grid = new Grid();

		Page page = PageHelper.offsetPage(param.getOffset(), param.getLimit(), true);
		dao.findList(entity);

		grid.setRows(page.getResult());
		grid.setTotal(page.getTotal());
		return grid;
	}
}
