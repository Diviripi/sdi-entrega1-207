package com.project.tests.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class PO_BoughtOffers  extends PO_View{
    public static void countMyOffers(WebDriver driver, int numeroOfertas ) {
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//tr");
        assertTrue("El numero de ofertas no coincide,el numero es "+elementos.size(),
                elementos.size()==numeroOfertas+1);//Por el header
    }
}
