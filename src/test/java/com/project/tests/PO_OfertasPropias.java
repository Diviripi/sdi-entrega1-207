package com.project.tests;

import com.project.tests.pageobjects.PO_View;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_OfertasPropias  extends PO_View {
    public static String destacar(WebDriver driver, int i) {
        List<WebElement> elemento=PO_View.checkElement(driver,"free","//td["+(i+1)+"]");
        String nombreElemento=elemento.get(0).getText();

        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[text()='Destacar']");
        elementos.get(i).click();

        return nombreElemento;
    }
}
