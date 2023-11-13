package cn.enilu.flash.config;

import com.trhui.mallbook.config.MallBookConfig;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

/**
 * @Author xiaogc
 * @Date 2022/2/10 10:03
 */
public class ChannelConfig {
    /**
     * mallbook 测试环境地址
     */
    public static String payUrl;

    /**
     * 业务系统商户平台编号
     */
    public static String merchantNo;
    /**
     * 接口版本号，不同版本号触发不同接口业务
     */
    public static String version;

    /**
     * 商户平台私钥路径
     */
    public static String merchantPrivateKey;
    /**
     * mallbook公钥路径
     */
    public static String mallBookPublicKey;

    static {
        YamlPropertiesFactoryBean yamlProFb = new YamlPropertiesFactoryBean();
        yamlProFb.setResources(new ClassPathResource("mallbook.yaml"));
        Properties properties = yamlProFb.getObject();
        System.out.println("mallbook 参数配置初始化");
        System.out.println("--------------------------------");
        System.out.println("环境地址:" + properties.get("mallbook.pay_url"));
        System.out.println("商户平台编号:" + properties.get("mallbook.merchant_no"));
        System.out.println("接口版本号:" + properties.get("mallbook.version"));
        System.out.println("商户平台私钥:" + properties.get("mallbook.merchant_private_key"));
        System.out.println("mallbook 公钥:" + properties.get("mallbook.mall_book_public_key"));
        System.out.println("--------------------------------");
        payUrl = properties.get("mallbook.pay_url").toString();
        merchantNo = properties.get("mallbook.merchant_no").toString();
        version = properties.get("mallbook.version").toString();
        merchantPrivateKey = properties.get("mallbook.merchant_private_key").toString();
        mallBookPublicKey = properties.get("mallbook.mall_book_public_key").toString();

        MallBookConfig config = new MallBookConfig();
        config.setMerchantPrivateKey(merchantPrivateKey);
        config.setDebug(true);
        config.setProdEnvironment(false);
        config.setApiBase(payUrl);
        config.setMerchantNo(merchantNo);
        config.setVersion(version);
        config.setFileUploadDebug(false);
        config.setTimeout(60000);
        config.init();
    }
}
