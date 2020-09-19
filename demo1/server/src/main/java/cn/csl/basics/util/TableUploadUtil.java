package cn.csl.basics.util;
/**
 * 分页客户端传给服务器的参数
 * @author Administrator
 *
 */
public class TableUploadUtil {
	/**
	 * 开始记录数
	 */
	private int start=0;
	/**
	 * 前台传过来的数据，原封推给后台
	 */
	private int draw=0;
	/**
	 * 每页记录数
	 */
	private int length=10;
	/**
	 * 搜索条件
	 */
	private String searchdetail;
	/**
	 * 搜索条件属性
	 */
	private String searchdeAttribute;
	/**
	 * 附加搜索sql
	 */
	private String sql;

	public String getSearchdetail() {
		return searchdetail;
	}
	public void setSearchdetail(String searchdetail) {
		this.searchdetail = searchdetail;
	}
	public int getStart() {
		//start=(draw-1)*length;
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}

	public String getSearchdeAttribute() {
		return searchdeAttribute;
	}

	public void setSearchdeAttribute(String searchdeAttribute) {
		this.searchdeAttribute = searchdeAttribute;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
}
