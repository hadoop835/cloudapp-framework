package io.cloudapp.redis.interceptor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Base64;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class SM2DecryptInterceptorTest {
    
    private SM2DecryptInterceptor interceptor;
    
    @Before
    public void setUp() throws Exception {
        String PRIVATE_KEY = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYIt" +
                "BHkwdwIBAQQg0FLS2JNm9j/0zMa5bTGzkdQXqS/gJ/npNri5h6eiq2mgCgYIKoE" +
                "cz1UBgi2hRANCAAS6pCjyaIMpV0aWIT3Jlwt626OpZrWSGZ4Y8KbQZQggYoXjZU" +
                "skofReUZdGAMWXp3ijEijC87qnq1SNmuGBo7Yj";
        interceptor = new SM2DecryptInterceptor(
                Base64.getDecoder().decode(PRIVATE_KEY));
    }
    
    @Test
    public void testIntercept() {
        String content = "BKngm3iAg9nLd8yfkUk5SiLfA0QyRctWV5vJoh8nQkBnUyKg" +
                "4o5ZATm4ex8JpD2MdJOPMMBqAorEEXaW4whoDwElH+P6W+q+d5fXldKxQxR/" +
                "9D4ObUGngJq+lMANI+LGuJzEdlgB38s=";
        
        byte[] decrypt = interceptor.intercept(Base64.getDecoder().decode(content));
        
        assertEquals("content", new String(decrypt));
    }
    
}
