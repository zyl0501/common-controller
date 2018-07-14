package com.tomtaw.third_party;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
        String unionIdCallBack= "callback( {\"client_id\":\"101399320\",\"openid\":\"73FC99F4DC2C9A0B10330673FBB19914\",\"unionid\":\"UID_C23E6FAFF5CF29C19779669A98E90E3E\"} );\n";

        String pattern = "callback\\((.*)\\);";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(unionIdCallBack);
        if (m.find()) {
            String json = m.group(1);
            System.out.println("解析结果："+json);
        } else {
           System.out.println("解析失败");
        }
    };
}