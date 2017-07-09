package net.dowish.modules.sys.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import net.dowish.common.utils.Apis;
import net.dowish.common.utils.ShiroUtils;
import net.dowish.modules.sys.entity.SysUserEntity;
import net.dowish.modules.sys.service.SysUserService;
import net.dowish.modules.sys.service.SysUserTokenService;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 *
 * 登录相关
 *
 */
@RestController
@Slf4j
public class SysLoginController {
	@Autowired
	private Producer producer;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;

	@Value("${spring.profiles.active}")
	private String env;


	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response)throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		//生成文字验证码
		String text = producer.createText();
		//生成图片验证码
		BufferedImage image = producer.createImage(text);
		//保存到shiro session
		ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}

	/**
	 * 登录
	 */
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public Map<String, Object> login(@RequestBody Map<String,String> map)throws IOException {
//		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
//		if(!"dev".equals(env)) {
//			if (!captcha.equalsIgnoreCase(kaptcha)) {
//				return Apis.error("验证码不正确");
//			}
//		}
		String username = map.get("username");
		String password = map.get("password");
		log.info("username : {}",map.get("username"));

		//用户信息
		SysUserEntity user = sysUserService.queryByUserName(username);

		//账号不存在、密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex())) {
			return Apis.error("账号或密码不正确");
		}

		//账号锁定
		if(user.getStatus() == 0){
			return Apis.error("账号已被锁定,请联系管理员");
		}

		//生成token，并保存到数据库
		Apis apis = sysUserTokenService.createToken(user);
		return apis;
	}
	
}
