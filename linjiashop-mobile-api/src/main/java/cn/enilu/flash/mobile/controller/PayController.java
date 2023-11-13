package cn.enilu.flash.mobile.controller;

import cn.enilu.flash.bean.entity.shop.Order;
import cn.enilu.flash.bean.entity.shop.ShopUser;
import cn.enilu.flash.bean.enumeration.shop.OrderEnum;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.service.WeixinPayService;
import cn.enilu.flash.service.shop.OrderService;
import cn.enilu.flash.service.shop.ShopUserService;
import cn.enilu.flash.utils.StringUtil;
import cn.enilu.flash.web.controller.BaseController;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.trhui.mallbook.client.PaymentClient;
import com.trhui.mallbook.domain.common.BaseResponse;
import com.trhui.mallbook.domain.request.PaymentOrderUser;
import com.trhui.mallbook.domain.request.hf.HfPaymentOrderRequest;
import com.trhui.mallbook.domain.response.hf.HfPaymentOrderResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ：enilu
 * @date ：Created in 2020/3/17 20:15
 */
@RestController
@RequestMapping("/pay")
public class PayController extends BaseController {
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private WeixinPayService weixinPayService;
    @Autowired
    private OrderService orderService;
    @RequestMapping(value = "wx/prepare",method = RequestMethod.POST)
    public Object wxPrepare(@RequestParam("orderSn")String orderSn){
        ShopUser user = shopUserService.getCurrentUser();
        if(StringUtil.isEmpty(user.getWechatOpenId())){
            return Rets.failure("非微信用户");
        }
        Order order = orderService.getByOrderSn(orderSn);
        WxPayMpOrderResult wxOrder = weixinPayService.prepare(user,order);
        if(wxOrder!=null) {
            return Rets.success(wxOrder);
        }
        return Rets.failure("数据准备异常");
    }

    @RequestMapping(value = "wx/mallbookPrepare",method = RequestMethod.POST)
    public Object mallbookPrepare(
            @RequestParam("orderSn")String orderSn,
            @RequestParam("payType")String payType

    ){
        ShopUser user = shopUserService.getCurrentUser();
        if(StringUtil.isEmpty(user.getWechatOpenId())){
            return Rets.failure("非微信用户,请先登录。");
        }
        Order order = orderService.getByOrderSn(orderSn);
//        Todo 微信支付 成功后订单状态改为微信支付
//        order.setPayType(payType);
        HfPaymentOrderRequest request = new HfPaymentOrderRequest();
        request.setTransferType("1");       //转账类型
        request.setAsynSplitFlag("0");      //是否异步拆分
        request.setBizOrderId(orderSn);     //订单号
        request.setAmount(order.getRealPrice().toString()); //订单金额
        request.setPayeeUserId("227645440414");     //子商户id
        request.setOrderName("test_order_name");    //订单名称
        request.setPayType(payType);
        request.setTerminalIp("127.0.0.1");
//        JSONArray.parseArray()
//        List<PaymentOrderUser> paymentOrderUsers = JSONArray.parseArray(paymentOrderVO.getSplitList(), PaymentOrderUser.class);
//        request.setSplitList(paymentOrderUsers);
//        List<Goods> goods = JSONArray.parseArray(paymentOrderVO.getGoodsDetail(), Goods.class);
//        request.setGoodsDetail(goods);
//        request.setNotifyUrl("http://xiaogc123.oicp.io/test/merchant/withdrawNotice");
//        request.setFrontUrl("http://xiaogc123.oicp.io/test/merchant/withdrawNotice");
//        request.setMerOrderId(System.currentTimeMillis()+"");
//        request.setMerchantNo(ChannelConfig.merchantNo);
//        request.setAppid(wxMiniProgramProperties.getAppId());

        BaseResponse<HfPaymentOrderResponse> hfPaymentOrderResponseBaseResponse = PaymentClient.hfPayment(request);
        return hfPaymentOrderResponseBaseResponse;

//        request.setPayeeUserId(paymentOrderVO.getPayeeUserId());
//        request.setOrderName(paymentOrderVO.getOrderName());
//        request.setPayType(paymentOrderVO.getPayType());
//        request.setTerminalIp("127.0.0.1");
//        List<PaymentOrderUser> paymentOrderUsers = JSONArray.parseArray(paymentOrderVO.getSplitList(), PaymentOrderUser.class);
//        request.setSplitList(paymentOrderUsers);
//        List<Goods> goods = JSONArray.parseArray(paymentOrderVO.getGoodsDetail(), Goods.class);
//        request.setGoodsDetail(goods);
//        request.setNotifyUrl("http://xiaogc123.oicp.io/test/merchant/withdrawNotice");
//        request.setFrontUrl("http://xiaogc123.oicp.io/test/merchant/withdrawNotice");
//        request.setMerOrderId(System.currentTimeMillis()+"");
//        request.setMerchantNo(ChannelConfig.merchantNo);
//        request.setAppid(wxMiniProgramProperties.getAppId());

//        MallbookPayService wxOrder = MallbookPayService.prepare(user,order);
//        if(wxOrder!=null) {
//            return Rets.success(wxOrder);
//        }
//        return Rets.failure("数据准备异常");
//        BaseResponse<HfPaymentOrderResponse> hfPaymentOrderResponseBaseResponse = PaymentClient.hfPayment(request);
//        return hfPaymentOrderResponseBaseResponse;
    }

    /**
     * 微信支付回调
     * @return
     */
    @RequestMapping(value = "wx/notify",method = RequestMethod.POST)
    public Object wxNotify(){
        String  msg = weixinPayService.resultNotify();
        return msg;
    }

    /**
     * 查询支付结果
     * @param orderSn
     * @return
     */
    @RequestMapping(value = "queryResult/{orderSn}",method = RequestMethod.GET)
    public Object wxNotify(@PathVariable("orderSn") String orderSn){
        Order order = orderService.getByOrderSn(orderSn);
        Boolean payResult = OrderEnum.PayStatusEnum.UN_SEND.getId().equals(order.getPayStatus())
                && OrderEnum.PayStatusEnum.UN_SEND.getId().equals(order.getStatus());
        return Rets.success(payResult);
    }
}
