package com.g7.framwork.common.util.json;

import com.g7.framework.common.dto.BaseVoidReq;
import com.g7.framwork.common.util.bean.QuestionResp;
import com.g7.framwork.common.util.bean.RepresentationAvailableEnum;
import com.g7.framwork.common.util.bean.RepresentationListResp;
import com.g7.framwork.common.util.bean.RepresentationStatusEnum;
import com.g7.framwork.common.util.bean.RepresentationTypeEnum;
import com.g7.framwork.common.util.bean.URLData;
import com.g7.framwork.common.util.bean.WaybillQuestionStatusEnum;
import com.g7.framwork.common.util.bean.WaybillQuestionTypeEnum;
import com.g7.framwork.common.util.json.domain.Order;
import com.g7.framwork.common.util.json.domain.User;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JsonUtilsTest {

    @Test
    public void testToList() {
        String name = "json/array.json";
        String json = parseJson(name);
        List<Object> result = JsonUtils.fromJson(json);
        System.out.println("JsonUtilTest.testToList: result ==> " + result);
    }

    @Test
    public void testToListWithType() {
        String name = "json/list.json";
        String json = parseJson(name);
        TypeReference<List<User>> type = new TypeReference<List<User>>() { };
        List<User> result = JsonUtils.fromJson(json, type.getType());
        for (int i = 0; i < result.size(); i++) {
            User user = result.get(i);
            System.out.println("JsonUtilTest.testToListWithType: user ==> " + user);
        }
    }

    @Test
    public void testToJson() {
        User user = new User();
        user.setId(1);
        user.setUsername("yidasanqian");

        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setId(0L);
        order.setTraceNo(0);
        order.setCreateAt("dreamyao");
        order.setUpdateAt(new Date());
        orders.add(order);
        user.setOrders(orders);
        user.setAddress(null);
        String result = JsonUtils.toJson(user);
        System.out.println("JsonUtilTest.testToJsonString: result ==> " + result);
    }

    @Test
    public void testFromJson() {
        String json = parseJson("json/user.json");
        User user = JsonUtils.fromJson(json, User.class);
        System.out.println("JsonUtilTest.testToPojo: user ==> " + user);
    }

    @Test
    private void testToMap() {
        String json = parseJson("json/user.json");
        TypeReference<Map<String, Object>> reference = new TypeReference<Map<String, Object>>() {
        };
        Map<String,Object> map = JsonUtils.fromJson(json, reference.getType());
        System.out.println("JsonUtilTest.testToMap: map ==> " + map);
    }

    @Test
    private void fromJson8Map() {
        String json = parseJson("json/user.json");
        TypeReference<Map<String, Object>> reference = new TypeReference<Map<String, Object>>() {
        };
        Map<String,Object> map = JsonUtils.fromJson(json, reference.getType());
        User user = JsonUtils.fromJson(map, User.class);
        System.out.println("JsonUtilTest.fromJson8Map: user ==> " + user);
    }

    public URLData getData() {
        return new URLData("20880", "127.0.0.1", "test", 8080,
                new String[]{"test1,test2"}, new Object[]{"String", "String"}, "1.0.0",
                "dreamyao", "com.test.TestService", "testOne", "dreamyao");
    }

    @Test
    private void testBaseVoid() {
        BaseVoidReq req = new BaseVoidReq();
        System.out.println(JsonUtils.toJson(req));
    }

    @Test
    private void testRepresentation() {
        RepresentationListResp resp = new RepresentationListResp();
        resp.setGmtCreate(new Date());
        resp.setGmtModified(new Date());
        resp.setOrgcode("");
        resp.setWaybillId("");
        resp.setQuestionTypes("");
        resp.setRepresentationType(RepresentationTypeEnum.ABNORMAL_REPRESENTATION_OF_TOTAL_FREIGHT_PRICE);
        resp.setRepresentationStatus(RepresentationStatusEnum.REPRESENTATION_FAILED);
        resp.setRepresentationId("");
        resp.setActualShippingTime(new Date());
        resp.setReceiptTime(new Date());
        resp.setActualShippingImages("");
        resp.setReceiptImages("");
        resp.setOtherImages("");
        resp.setAppealTime(new Date());
        resp.setAuditTime(new Date());
        resp.setOperator("");
        resp.setAuditor("");
        resp.setAvailable(RepresentationAvailableEnum.AVAILABLE);
        resp.setAppealDesc("");
        resp.setAuditDesc("");
        resp.setLinkman("");
        resp.setLinkmanPhone("");
        resp.setPreAvailable(RepresentationAvailableEnum.AVAILABLE);


        List<QuestionResp> questions = new ArrayList<>();
        QuestionResp questionResp = new QuestionResp();
        questionResp.setId(0L);
        questionResp.setGmtCreate(new Date());
        questionResp.setGmtModified(new Date());
        questionResp.setQuestionId("");
        questionResp.setWaybillId("");
        questionResp.setType(WaybillQuestionTypeEnum.ARRIVAL_VOID);
        questionResp.setOperateType("");
        questionResp.setStatus(WaybillQuestionStatusEnum.PENDING);
        questionResp.setSource("");
        questionResp.setDescribe("");
        questionResp.setRequest("");
        questionResp.setResponse("");
        questions.add(questionResp);
        resp.setQuestions(questions);

        System.out.println(JsonUtils.toJson(resp));
    }

    private String parseJson(String name) {
        String json = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(getClass()
                    .getClassLoader()
                    .getResource(name)
                    .getFile()
            ));
            StringBuilder builder = new StringBuilder();
            String tmp = null;
            while ((tmp = reader.readLine()) != null) {
                builder.append(tmp);
            }
            json = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
