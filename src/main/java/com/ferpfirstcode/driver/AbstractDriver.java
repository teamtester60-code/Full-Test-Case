package com.ferpfirstcode.driver;

import com.ferpfirstcode.utils.dataReader.PropertyReader;
import org.openqa.selenium.WebDriver;

public abstract class AbstractDriver {
    protected final String remoteHost = PropertyReader.getProperty("remoteHost");
    protected final String remoteport = PropertyReader.getProperty("remotePort");

    // protected File  blurimageextensions= new File("src/main/resources/extensions/BlurImage.crx");
    public abstract WebDriver createDriver();
}
