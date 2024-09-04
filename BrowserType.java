	package report_utilities.common;

public class BrowserType
{
    public enum Browser { IE, CHROME, FIREFOX, EDGE, SAFARI}

    public Browser getBrowser(String browser)
    {
        if (browser.equalsIgnoreCase("IE"))
        {
            return Browser.IE;
        }
        else if (browser.equalsIgnoreCase("CHROME"))
        {
            return Browser.CHROME;
        }
        else if (browser.equalsIgnoreCase("FIREFOX"))
        {
            return Browser.FIREFOX;
        }
        else if (browser.equalsIgnoreCase("EDGE"))
        {
            return Browser.EDGE;
        }
        else if (browser.equalsIgnoreCase("SAFARI"))
        {
            return Browser.SAFARI;
        }
        else
        {
            return Browser.CHROME;
        }
    }


    public String getBrowserfromEnum(Browser browser)
    {
        if (browser==Browser.IE)
        {
            return "IE";
        }
        else if (browser == Browser.CHROME)
        {
            return "Chrome";
        }
        else if (browser == Browser.FIREFOX)
        {
            return "Firefox";
        }
        else if (browser == Browser.EDGE)
        {
            return "Edge";
        }
        else if (browser == Browser.SAFARI)
        {
            return "Safari";
        }
        else
        {
            return "Chrome";
        }
    }


}

