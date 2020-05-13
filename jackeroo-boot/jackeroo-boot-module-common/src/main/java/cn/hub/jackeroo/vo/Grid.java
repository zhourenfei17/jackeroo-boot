package cn.hub.jackeroo.vo;

import com.github.pagehelper.Page;

import java.io.Serializable;
import java.util.List;

public class Grid implements Serializable {

	private long total;

	private List rows;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	public void setPage(Page page) {
		if (page != null) {
			this.total = page.getTotal();
			this.rows = page.getResult();
		}
	}
}
