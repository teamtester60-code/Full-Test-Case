package com.ferpfirstcode.customlisteners;

import io.qameta.allure.Allure;
import org.testng.IAnnotationTransformer;
import org.testng.ITestNGListener;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class RetryTransformer implements IAnnotationTransformer, ITestNGListener {

    @Override
    public void transform(ITestAnnotation annotation,
                          Class testClass,
                          Constructor testConstructor,
                          Method testMethod) {

        System.out.println("🔥 Transformer applied on: " + testMethod.getName());
        Allure.step("Transformer applied on: " + testMethod.getName());

        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}