package com.project.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_BuyList  extends PO_View{

    public static void rellenarCuadroDeBusqueda(WebDriver driver,String textoBusqueda){
        WebElement cuadroBusqueda = driver.findElement(By.name("searchText"));
        cuadroBusqueda.click();
        cuadroBusqueda.clear();
        cuadroBusqueda.sendKeys(textoBusqueda);
        By boton = By.className("btn");
        driver.findElement(boton).click();
    }

    public static void comprarOferta(WebDriver driver, int ofertaAComprar) {
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[text()='Comprar']");
        elementos.get(ofertaAComprar).click();
    }

    public static void mensajeAOferta(WebDriver driver, int oferta) {
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[text()='Enviar mensaje']");
        elementos.get(oferta).click();
    }

}
