package net.dowish.modules.oss.cloud;

import net.dowish.modules.sys.service.DictService;
import net.dowish.common.utils.ConfigConstant;
import net.dowish.common.utils.Constant;
import net.dowish.common.utils.SpringContextHolder;

/**
 * 文件上传Factory
 */
public final class OSSFactory {
    private static DictService dictService;

    static {
        OSSFactory.dictService = (DictService) SpringContextHolder.getBean("dictService");
    }

    public static CloudStorageService build(){
        //获取云存储配置信息
        CloudStorageConfig config = dictService.getConfigObject(ConfigConstant.CLOUD_STORAGE_CONFIG_KEY, CloudStorageConfig.class);

        if(config.getType() == Constant.CloudService.QINIU.getValue()){
            return new QiniuCloudStorageService(config);
        }else if(config.getType() == Constant.CloudService.ALIYUN.getValue()){
            return new AliyunCloudStorageService(config);
        }else if(config.getType() == Constant.CloudService.QCLOUD.getValue()){
            return new QcloudCloudStorageService(config);
        }

        return null;
    }

}
