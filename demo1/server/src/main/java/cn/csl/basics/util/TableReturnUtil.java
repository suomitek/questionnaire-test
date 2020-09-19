package cn.csl.basics.util;


/**
 * 分页 服务器 传给 客户端的参数
 * @author Administrator
 *
 */
public class TableReturnUtil {
	private long draw=0;
	//每页显示页数
	private long pageLength=10;
	private long length=10;
	private long pageNum;
	private Object data;
	private long recordsTotal;//符合条件的总条数
	private long recordsFiltered;

	public long getPageNum() {
		return pageNum;
	}

	public void setPageNum(long pageNum) {
		this.pageNum = pageNum;
	}

	public long getDraw() {
		return draw;
	}
	public void setDraw(long draw) {
		this.draw = draw;
	}
	public long getPageLength() {
		return pageLength;
	}
	public void setPageLength(long pageLength) {
		this.pageLength = pageLength;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public long getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public long getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
}
