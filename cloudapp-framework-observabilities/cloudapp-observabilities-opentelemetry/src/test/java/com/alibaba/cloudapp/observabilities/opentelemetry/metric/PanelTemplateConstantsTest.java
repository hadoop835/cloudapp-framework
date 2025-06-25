package com.alibaba.cloudapp.observabilities.opentelemetry.metric;

import com.alibaba.cloudapp.api.observabilities.MetricType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PanelTemplateConstantsTest {
    
    @Test
    public void testGetRowTemplate() {
        Assert.assertEquals("row.ftl", PanelTemplateConstants.getRowTemplate());
    }
    
    @Test
    public void testGetModelTemplate() {
        Assert.assertEquals("model.ftl", PanelTemplateConstants.getModelTemplate());
    }
    
    @Test
    public void testGetTemplatesByMetricType() {
        List<String> counterTemplates = Arrays.asList("gauge.ftl",
                                                      "timeseries.ftl");
        List<String> gaugeTemplates = Arrays.asList("avg_gauge.ftl",
                                                    "max_gauge.ftl");
        List<String> histogramTemplates = Arrays.asList("histogram.ftl",
                                                        "heatmap.ftl");
        Assert.assertEquals(
                counterTemplates,
                PanelTemplateConstants.getTemplatesByMetricType(
                        MetricType.COUNTER));
        Assert.assertEquals(
                gaugeTemplates,
                PanelTemplateConstants.getTemplatesByMetricType(
                        MetricType.GAUGE));
        Assert.assertEquals(
                histogramTemplates,
                PanelTemplateConstants.getTemplatesByMetricType(
                        MetricType.HISTOGRAM));
    }
}
