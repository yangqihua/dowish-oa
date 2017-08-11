package net.dowish.modules.sys.service.impl;

import com.google.gson.Gson;
import net.dowish.modules.sys.dao.ConfigDao;
import net.dowish.modules.sys.entity.Dict;
import net.dowish.modules.sys.service.DictService;
import net.dowish.common.exception.ResultException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("sysConfigService")
public class DictServiceImpl implements DictService {
	@Autowired
	private ConfigDao configDao;
	
	@Override
	@Transactional
	public void save(Dict config) {
		configDao.save(config);
	}

	@Override
	public void update(Dict config) {
		configDao.update(config);
	}

	@Override
	public void updateValueByKey(String key, String value) {
		configDao.updateValueByKey(key, value);
	}

	@Override
	public void deleteBatch(Long[] ids) {
		configDao.deleteBatch(ids);
	}

	@Override
	public List<Dict> queryList(Map<String, Object> map) {
		return configDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return configDao.queryTotal(map);
	}

	@Override
	public Dict queryObject(Long id) {
		return configDao.queryObject(id);
	}

	@Override
	public String getValue(String key, String defaultValue) {
		String value = configDao.queryByKey(key);
		if(StringUtils.isBlank(value)){
			return defaultValue;
		}
		return value;
	}
	
	@Override
	public <T> T getConfigObject(String key, Class<T> clazz) {
		String value = getValue(key, null);
		if(StringUtils.isNotBlank(value)){
			return new Gson().fromJson(value, clazz);
		}

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new ResultException("获取参数失败");
		}
	}
}
