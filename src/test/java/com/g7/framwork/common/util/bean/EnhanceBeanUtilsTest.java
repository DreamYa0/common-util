package com.g7.framwork.common.util.bean;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author dreamyao
 * @title
 * @date 2018/9/18 上午11:17
 * @since 1.0.0
 */
public class EnhanceBeanUtilsTest {

    @Test
    public void testConvert() throws Exception {

        for (int i = 0; i < 100; i++) {
            SaveTradeOrderInfoBO orderInfoBO = new SaveTradeOrderInfoBO();
            orderInfoBO.setOrderNo(100L);

            SourceUserInfo userInfo = new SourceUserInfo();
            userInfo.setUserNo(100L);
            userInfo.setAccountNo(100L);
            userInfo.setAccountType(i);

            orderInfoBO.setUserInfo(userInfo);

            orderInfoBO.setLevel(i);
            orderInfoBO.setSymbol("BTC/USD");
            orderInfoBO.setProductCode("BTC");
            orderInfoBO.setCurrencyCode("USD");
            orderInfoBO.setProductUsdPrice(new BigDecimal("10"));
            orderInfoBO.setCurrencyUsdPrice(new BigDecimal("20"));
            orderInfoBO.setAction(i);
            orderInfoBO.setActionName("SELL");
            orderInfoBO.setOrderType(i);
            orderInfoBO.setOrderTypeName("TEST");
            orderInfoBO.setPriceLimit(new BigDecimal("30"));
            orderInfoBO.setPriceAverage(new BigDecimal("40"));
            orderInfoBO.setQuantity(new BigDecimal("50"));
            orderInfoBO.setQuantityRemaining(new BigDecimal("60"));
            orderInfoBO.setAmount(new BigDecimal("70"));
            orderInfoBO.setAmountRemaining(new BigDecimal("80"));
            orderInfoBO.setFeeCurrencyCode("BTC");
            orderInfoBO.setFee(new BigDecimal("90"));
            orderInfoBO.setFeeRate(new BigDecimal("100"));
            orderInfoBO.setStatus(0);
            orderInfoBO.setState(0);
            orderInfoBO.setFromClientType(0);
            orderInfoBO.setUtcCreate(new Date());
            orderInfoBO.setUtcUpdate(new Date());



            long start = System.currentTimeMillis();

            /*MongoTradeOrderInfo orderInfo = new MongoTradeOrderInfo();
            BeanUtils.copyProperties(orderInfoBO, orderInfo);*/

            MongoTradeOrderInfo convert = EnhanceBeanUtils.convert(orderInfoBO, MongoTradeOrderInfo.class,"id");
            System.out.println(System.currentTimeMillis() - start + "ms");
        }


    }

}