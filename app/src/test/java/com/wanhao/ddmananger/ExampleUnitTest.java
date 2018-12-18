package com.wanhao.ddmananger;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String s = "{\"success\":true,\"id\":81,\"item\":{\"id\":\"CI1001251\",\"name\":\"钻井\",\"refId\":\"BJS001ZJ01DG045G\",\"status\":\"在用\",\"manufacturer\":\"江苏钻井公司\",\"vendor\":null,\"serialNo\":null,\"factname\":null,\"type\":\"Facility System\",\"subtype\":null,\"deviceclass\":\"大型\",\"assetag\":null,\"classes\":\"A类(关键）\",\"partno\":\"TC450\",\"specialfa\":null,\"location\":\"中国\",\"wellteam\":null,\"assignment\":\"HD603固井队\"},\"relatedItems\":[{\"id\":\"BJS001ZJ01DG012G\",\"name\":\"大钩\"},{\"id\":\"CI1001225\",\"name\":\"天车\"},{\"id\":\"CI1001328\",\"name\":\"液压大钳\"},{\"id\":\"CI1001226\",\"name\":\"游车\"},{\"id\":\"BJS001ZJ01SL015G\",\"name\":\"水龙头\"},{\"id\":\"CI1001289\",\"name\":\"绞车\"},{\"id\":\"CI1001206\",\"name\":\"转盘\"}],\"mtnItems\":[]}";
        System.out.print(s.replace("\\", ""));

    }
}