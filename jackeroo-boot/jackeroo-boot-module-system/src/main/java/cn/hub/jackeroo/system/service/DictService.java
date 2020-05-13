package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.system.entity.Dict;
import cn.hub.jackeroo.persistence.CrudService;
import cn.hub.jackeroo.system.dao.DictDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 字典信息Service
 * @author
 * @version 2018-11-14
 */
@Service
public class DictService extends CrudService<DictDao, Dict, Integer> {
	@Resource
	private DictDao dao;

}