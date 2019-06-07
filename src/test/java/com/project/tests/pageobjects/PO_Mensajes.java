package com.project.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_Mensajes extends PO_View{
    public static void enviarMensaje(WebDriver driver, String mensaje) {
        WebElement cuadroMensaje = driver.findElement(By.name("message"));
        cuadroMensaje.click();
        cuadroMensaje.clear();
        cuadroMensaje.sendKeys(mensaje);
        By boton = By.className("btn");
        driver.findElement(boton).click();
    }
}
