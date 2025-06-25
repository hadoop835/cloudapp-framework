package com.alibaba.cloudapp.api.trace;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class MonitorDTOTest {
    
    private MonitorDTO monitorDTOUnderTest;
    
    @Before
    public void setUp() throws Exception {
        Date date = new GregorianCalendar(
                2020, Calendar.JANUARY,1
        ).getTime();
        
        monitorDTOUnderTest = new MonitorDTO(
                "tenantCode", 0, date,
                "bizCode", "appSign",
                "epCode", "serviceName",
                0, 0
        );
    }
    
    @Test
    public void testTenantCodeGetterAndSetter() {
        final String tenantCode = "tenantCode";
        monitorDTOUnderTest.setTenantCode(tenantCode);
        assertEquals(tenantCode, monitorDTOUnderTest.getTenantCode());
    }
    
    @Test
    public void testProvinceCodeGetterAndSetter() {
        final Integer provinceCode = 0;
        monitorDTOUnderTest.setProvinceCode(provinceCode);
        assertEquals(provinceCode, monitorDTOUnderTest.getProvinceCode());
    }
    
    @Test
    public void testSttTimeGetterAndSetter() {
        final Date sttTime = new GregorianCalendar(2020, Calendar.JANUARY,
                                                   1
        ).getTime();
        monitorDTOUnderTest.setSttTime(sttTime);
        assertEquals(sttTime, monitorDTOUnderTest.getSttTime());
    }
    
    @Test
    public void testBizCodeGetterAndSetter() {
        final String bizCode = "bizCode";
        monitorDTOUnderTest.setBizCode(bizCode);
        assertEquals(bizCode, monitorDTOUnderTest.getBizCode());
    }
    
    @Test
    public void testAppSignGetterAndSetter() {
        final String appSign = "appSign";
        monitorDTOUnderTest.setAppSign(appSign);
        assertEquals(appSign, monitorDTOUnderTest.getAppSign());
    }
    
    @Test
    public void testEpCodeGetterAndSetter() {
        final String epCode = "epCode";
        monitorDTOUnderTest.setEpCode(epCode);
        assertEquals(epCode, monitorDTOUnderTest.getEpCode());
    }
    
    @Test
    public void testServiceNameGetterAndSetter() {
        final String serviceName = "serviceName";
        monitorDTOUnderTest.setServiceName(serviceName);
        assertEquals(serviceName, monitorDTOUnderTest.getServiceName());
    }
    
    @Test
    public void testCountSuccessGetterAndSetter() {
        final Integer countSuccess = 0;
        monitorDTOUnderTest.setCountSuccess(countSuccess);
        assertEquals(countSuccess, monitorDTOUnderTest.getCountSuccess());
    }
    
    @Test
    public void testCountFailGetterAndSetter() {
        final Integer countFail = 0;
        monitorDTOUnderTest.setCountFail(countFail);
        assertEquals(countFail, monitorDTOUnderTest.getCountFail());
    }
    
    @Test
    public void testToString() {
        String result = "MonitorDTO{tenantCode=tenantCode, provinceCode=0," +
                " sttTime=Wed Jan 01 00:00:00 CST 2020, bizCode='bizCode'," +
                " appSign='appSign', epCode='epCode', serviceName='serviceName'," +
                " countSuccess=0, countFail=0}";
        assertEquals(result, monitorDTOUnderTest.toString());
    }
    
}
