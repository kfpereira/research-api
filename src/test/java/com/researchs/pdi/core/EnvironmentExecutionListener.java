package com.researchs.pdi.core;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class EnvironmentExecutionListener implements TestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {

    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {

    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        Environment environmentAnnotation = testContext.getTestClass().getAnnotation(Environment.class);
        if(testContext.getTestMethod().getAnnotation(Environment.class) != null)
            environmentAnnotation = testContext.getTestMethod().getAnnotation(Environment.class);

        if (environmentAnnotation != null)
            new EnvironmentRunner().run(environmentAnnotation.value(),
                    testContext.getApplicationContext());
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {

    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {

    }

}
