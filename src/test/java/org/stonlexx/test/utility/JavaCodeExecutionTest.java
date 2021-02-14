package org.stonlexx.test.utility;

import org.stonlexx.gamelibrary.utility.JavaCodeExecutionUtil;

public class JavaCodeExecutionTest {

    public static void main(String[] args) {
        JavaCodeExecutionUtil.executeCode(
                "java.lang.String text = \"Test message\";",
                "System.out.println(text);"
        );
    }

}
