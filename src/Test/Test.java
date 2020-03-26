package Test;

import Console.Console;
import Crawer.*;
import Settings.Setting;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        System.setProperty("java.net.useSystemProxies","true");
        Setting.getSettingsInPropetries();
        Console c = new Console();
        c.work();
    }

}
