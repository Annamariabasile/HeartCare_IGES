// Generated by Selenium IDE
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
public class SystemTestingRegistrazioneCaregiverNomeVuotoTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  @Before
  public void setUp() {
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void systemTestingRegistrazioneCaregiverNomeVuoto() {
    driver.get("http://localhost:3000/registrazioneCaregiver?idPaziente=1&idCaregiver=76");
    driver.manage().window().setSize(new Dimension(1016, 721));
    driver.findElement(By.name("cognome")).click();
    driver.findElement(By.name("cognome")).sendKeys("zoccola");
    driver.findElement(By.name("numeroTelefono")).click();
    driver.findElement(By.name("numeroTelefono")).sendKeys("+393272349622");
    driver.findElement(By.name("genere")).click();
    driver.findElement(By.name("genere")).sendKeys("M");
    driver.findElement(By.name("dataDiNascita")).click();
    driver.findElement(By.name("dataDiNascita")).sendKeys("2001-07-05");
    driver.findElement(By.id("codiceFiscale")).click();
    driver.findElement(By.id("codiceFiscale")).sendKeys("ZCCLSN01L05H703M");
    driver.findElement(By.id("password")).click();
    driver.findElement(By.id("password")).sendKeys("Wpasswd1!%");
    driver.findElement(By.name("confermaPassword")).click();
    driver.findElement(By.name("confermaPassword")).sendKeys("Wpasswd1!%");
    driver.findElement(By.cssSelector(".formButton")).click();
    driver.findElement(By.name("cognome")).sendKeys("Zoccola");
    driver.findElement(By.cssSelector(".formButton")).click();
  }
}
