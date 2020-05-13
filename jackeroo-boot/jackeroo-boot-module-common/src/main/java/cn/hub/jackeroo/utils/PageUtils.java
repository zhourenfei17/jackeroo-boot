package cn.hub.jackeroo.utils;

import cn.hub.jackeroo.vo.GridParam;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

public class PageUtils<E> {

	public static <E> Page<E> offsetPage(GridParam gridParam, boolean count) {
		if (gridParam != null) {
			return PageHelper.offsetPage(gridParam.getOffset(), gridParam.getLimit(), count);
		}
		return null;
	}

	public static <E> Page<E> offsetPage(GridParam gridParam) {
		return offsetPage(gridParam, true);
	}
}
