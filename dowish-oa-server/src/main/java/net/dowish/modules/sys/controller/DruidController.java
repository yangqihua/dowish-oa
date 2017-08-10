package net.dowish.modules.sys.controller;

import net.dowish.common.annotation.SysLog;
import net.dowish.common.base.controller.BaseController;
import net.dowish.common.security.validator.ValidatorUtils;
import net.dowish.common.utils.Apis;
import net.dowish.common.utils.Page;
import net.dowish.common.utils.Query;
import net.dowish.modules.sys.entity.Dict;
import net.dowish.modules.sys.service.ConfigService;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * 系统配置信息
 */
@RestController
@RequestMapping("/sys/druid")
public class DruidController extends BaseController {
	@Autowired
	private ConfigService configService;

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
