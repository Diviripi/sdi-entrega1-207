package com.project.tests.pageobjects;

import com.project.tests.util.SeleniumUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class PO_ConversationList extends PO_View{
    public static void seeConversation(WebDriver driver, int i) {
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[text()='Mostrar']");
        elementos.get(i).click();
    }

    public static void countConversations(WebDriver driver, int i) {
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[text()='Mostrar']");
        assertTrue("El numero de conversaciones no coincide",elementos.size()==i);
    }

    public static void deleteConversation(WebDriver driver, int i) {
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[text()='Borrar']");
        elementos.get(i).click();
    }
}
