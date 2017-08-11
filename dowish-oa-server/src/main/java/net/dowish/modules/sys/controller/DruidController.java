package net.dowish.modules.sys.controller;

import net.dowish.common.base.controller.BaseController;
import net.dowish.common.utils.Apis;
import net.dowish.modules.sys.service.DictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 系统配置信息
 */
@RestController
@RequestMapping("/sys/druid")
public class DruidController extends BaseController {
	@Autowired
	private DictService dictService;

	/**
	 * 获取sql内容
	 */
	@RequestMapping("/info")
	@RequiresPermissions("sys:druid:info")
	public Apis info(@RequestParam Map<String, Object> params, HttpServletRequest request) {
		String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + "druid/index.html";
		return Apis.ok().put("path", path);
	}

}
