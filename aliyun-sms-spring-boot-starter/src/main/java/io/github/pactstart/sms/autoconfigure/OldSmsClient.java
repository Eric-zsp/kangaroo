package io.github.pactstart.sms.autoconfigure;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.BizResult;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 阿里大于短信API客户端
 */
public class OldSmsClient {

    private static Logger logger = LoggerFactory.getLogger(OldSmsClient.class);
    private OldSmsConfig oldSmsConfig;

    public OldSmsClient(OldSmsConfig oldSmsConfig) {
        this.oldSmsConfig = oldSmsConfig;
    }

    /**
     * @param signName     短信签名
     * @param templateCode 模板编号
     * @param phone        手机号
     * @param params       短信参数
     * @return
     * @throws Exception
     */
    public SmsResponse sendSms(String signName, String templateCode, String phone, Map<String, String> params) throws Exception {
        SmsResponse resp = new SmsResponse();
        resp.setSuccess(false);
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", oldSmsConfig.getAppKey(), oldSmsConfig.getAppSecret());
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend(signName);
        req.setSmsType("normal");
        req.setSmsFreeSignName(signName);
        req.setSmsParamString(SmsParamUtils.convert2Json(params));
        req.setRecNum(phone);
        req.setSmsTemplateCode(templateCode);
        AlibabaAliqinFcSmsNumSendResponse response = client.execute(req);
        if ("isv.BUSINESS_LIMIT_CONTROL".equals(response.getSubCode())) {
            resp.setError("频繁获取验证码，请稍后再试!");
            return resp;
        }
        logger.debug(response.getMsg());
        BizResult result = response.getResult();
        if (result == null) {
            resp.setError(response.getSubMsg());
            return resp;
        }
        if (result.getSuccess()) {
            resp.setSuccess(true);
        } else {
            resp.setError(result.getMsg());
            logger.error("errcode: {} ,msg: {}", result.getErrCode(), result.getMsg());
        }
        return resp;
    }

}
