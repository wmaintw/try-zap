package proxy;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.lang.System.getProperty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.openqa.selenium.By.name;
import static org.openqa.selenium.remote.CapabilityType.PROXY;

public class WithProxyTest {

    private static final String WEB_APP = "http://localhost:8080/WebGoat";
    public static final String ZAP_PROXY_KEY = "zap.proxy";
    private WebDriver driver;

    @Before
    public void setUp() throws Exception {
        driver = setupDriverProxy();
    }

    @Test
    public void shouldOpenLoginPage() throws Exception {
        driver.get(WEB_APP);
        assertThat(driver.getTitle(), is("Login Page"));
    }

    @Test
    public void shouldLoginSuccessfully() throws Exception {
        driver.get(WEB_APP);
        driver.findElement(name("username")).sendKeys("guest");
        driver.findElement(name("password")).sendKeys("guest");
        driver.findElement(name("loginForm")).submit();

        waitUntilLoggedIn(5);

        assertThat(driver.getTitle(), is("WebGoat"));
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    private void waitUntilLoggedIn(int timeOutInSeconds) {
        new WebDriverWait(driver, timeOutInSeconds).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return input.getTitle().equalsIgnoreCase("WebGoat");
            }
        });
    }

    private WebDriver setupDriverProxy() {
        DesiredCapabilities cap = new DesiredCapabilities();

        String zapProxySetting = getProperty(ZAP_PROXY_KEY);
        if (StringUtils.isNotBlank(zapProxySetting)) {
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(zapProxySetting)
                    .setFtpProxy(zapProxySetting)
                    .setSslProxy(zapProxySetting);
            cap.setCapability(PROXY, proxy);
        }

        return new FirefoxDriver(cap);
    }
}
