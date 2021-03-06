package io.github.pactstart.pay;

import com.alibaba.fastjson.JSON;
import io.github.pactstart.pay.wxpay.WxPayService;
import io.github.pactstart.pay.wxpay.autoconfigure.WxPayProperties;
import io.github.pactstart.pay.wxpay.request.OrderQueryRequest;
import io.github.pactstart.pay.wxpay.request.PayResultNoticeRequest;
import io.github.pactstart.pay.wxpay.response.OrderQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class WxPayServiceTest {

    private WxPayService wxPayService;

    @Before
    public void init() throws Exception {
        WxPayProperties wxPayProperties = new WxPayProperties();
        wxPayProperties.setAppId("");
        wxPayProperties.setMchId("");
        wxPayProperties.setCertFile("classpath:cert/apiclient_cert.p12");
        wxPayProperties.setKey("");
        wxPayProperties.setNotifyUrl("");
        wxPayService = new WxPayService(wxPayProperties);
    }

    @Test
    public void testParsePayNoticeData() throws Exception {
        String xml = "<xml><appid><![CDATA[" + wxPayService.getWxPayProperties().getAppId() + "]]></appid>\n" +
                "<attach><![CDATA[支付0.03元]]></attach>\n" +
                "<bank_type><![CDATA[SPDB_CREDIT]]></bank_type>\n" +
                "<cash_fee><![CDATA[3]]></cash_fee>\n" +
                "<fee_type><![CDATA[CNY]]></fee_type>\n" +
                "<is_subscribe><![CDATA[N]]></is_subscribe>\n" +
                "<mch_id><![CDATA[" + wxPayService.getWxPayProperties().getMchId() + "]]></mch_id>\n" +
                "<nonce_str><![CDATA[ZRZqfCpGBt5qYIkGGcNUEq4DItu2CvKA]]></nonce_str>\n" +
                "<openid><![CDATA[oJBoq02YKtvXfKO5gdWkCMSe8PL0]]></openid>\n" +
                "<out_trade_no><![CDATA[101019270488012230656]]></out_trade_no>\n" +
                "<result_code><![CDATA[SUCCESS]]></result_code>\n" +
                "<return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "<sign><![CDATA[9D8D09B71173A78F9ABE39CEB5862D7C]]></sign>\n" +
                "<time_end><![CDATA[20180718012006]]></time_end>\n" +
                "<total_fee>3</total_fee>\n" +
                "<trade_type><![CDATA[APP]]></trade_type>\n" +
                "<transaction_id><![CDATA[4200000114201807181502724780]]></transaction_id>\n" +
                "</xml> ";
        PayResultNoticeRequest payResultNoticeRequest = wxPayService.parsePayNoticeData(xml);
        log.info(JSON.toJSONString(payResultNoticeRequest));
    }

    @Test
    public void testOrderQuery() throws Exception {
        OrderQueryRequest orderQueryRequest = new OrderQueryRequest(null, "101019263574247346176");
        OrderQueryResponse orderQueryResponse = wxPayService.orderQuery(orderQueryRequest);
        log.info(JSON.toJSONString(orderQueryResponse));
    }
}
