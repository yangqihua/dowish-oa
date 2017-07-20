package net.dowish.common.utils;

import lombok.Data;
import net.dowish.common.security.xss.SQLFilter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数
 */
@Data
public class Query extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	//当前页码
	private int page;
	//每页条数
	private int limit;

	public Query(Map<String, Object> params) {

		this.putAll(params);
		try {
			//分页参数
			Object page = params.get("page");
			Object limit = params.get("limit");
			if (page != null || limit != null) {
				this.page = Integer.parseInt(page.toString());
				this.limit = Integer.parseInt(params.get("limit").toString());
				this.put("offset", (this.page - 1) * this.limit);
				this.put("page", this.page);
				this.put("limit", this.limit);
			}
			String sidx = (String) params.get("sidx");
			String order = (String) params.get("order");
			if (StringUtils.isNotBlank(sidx)) {
				this.put("sidx", SQLFilter.sqlInject(sidx));
			}
			if (StringUtils.isNotBlank(order)) {
				this.put("order", SQLFilter.sqlInject(order));
			}
		} catch (RuntimeException ex) {
			this.remove("page");
			this.remove("limit");
			this.remove("offset");
			ex.printStackTrace();
		}
	}

}
