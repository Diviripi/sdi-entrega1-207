package com.project.tests;

import com.project.repositories.ConversationRepository;
import com.project.repositories.MessageRepository;
import com.project.repositories.ProductRepository;
import com.project.repositories.UserRepository;
import com.project.services.InsertSampleDataService;
import com.project.services.RolesService;
import com.project.services.UserService;
import com.project.tests.pageobjects.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
//Ordenamos las pruebas por el nombre del método
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectTests {


    @Autowired
    private UserService usersService;
    @Autowired
    private RolesService rolesService;
    @Autowired
    private UserRepository usersRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private InsertSampleDataService insertSampleDataService;

    // En Windows (Debe ser la versión 65.0.1 y desactivar las actualizacioens
    // automáticas)):
    static String PathFirefox64 = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    static String Geckdriver022 = "lib\\geckodriver.exe";
    // En MACOSX (Debe ser la versión 65.0.1 y desactivar las actualizacioens
    // automáticas):
    // static String PathFirefox65 =
    // "/Applications/Firefox.app/Contents/MacOS/firefox-bin";
    // static String Geckdriver024 = "/Users/delacal/selenium/geckodriver024mac";
    // Común a Windows y a MACOSX
    static WebDriver driver = getDriver(PathFirefox64, Geckdriver022);
    static String URL = "http://localhost:8080";

    public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
        System.setProperty("webdriver.gecko.driver", Geckdriver);
        WebDriver driver = new FirefoxDriver();
        return driver;
    }

    // Antes de cada prueba se navega al URL home de la aplicaciónn
    @Before
    public void setUp() {
        driver.navigate().to(URL);
        initdb();
    }

    public void initdb() {
        messageRepository.deleteAll();
        conversationRepository.deleteAll();
        productRepository.deleteAll();
        usersRepository.deleteAll();

        insertSampleDataService.init();

    }

    // Después de cada prueba se borran las cookies del navegador
    @After
    public void tearDown() {
        driver.manage().deleteAllCookies();
    }

    // Antes de la primera prueba
    @BeforeClass
    static public void begin() {
    }

    // Al finalizar la última prueba
    @AfterClass
    static public void end() {
        // Cerramos el navegador al finalizar las pruebas
        driver.quit();
    }

    //Registro del usuario con datos validos
    @Test
    public void PR01() {
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_RegisterView.fillForm(driver, "usuario@email.com", "usuario", "usuario", "123456", "123456");
        PO_View.checkElement(driver, "text", "Buscar ofertas");
    }

    //Registro de Usuario con datos invalidos (email vacio,nombre vacio,apellidos vacios)
    @Test
    public void PR02() {
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_RegisterView.fillForm(driver, "", "", "", "", "");
        PO_View.checkElement(driver, "text", "Registrate");
    }

    //Registro de usuario con repeticion de password erronea
    @Test
    public void PR03() {
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_RegisterView.fillForm(driver, "email", "ejemplo", "ejemplo", "123456", "654321");
        PO_View.checkElement(driver, "text", "Las contraseñas no coinciden");
    }

    //Registro de usuario con email ya existente
    @Test
    public void PR04() {
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_RegisterView.fillForm(driver, "emailExistente", "ejemplo", "ejemplo", "123456", "123456");
        driver.navigate().to(URL);//Vuelta al menu principal

        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_RegisterView.fillForm(driver, "emailExistente", "ejemplo", "ejemplo", "123456", "123456");

        PO_View.checkElement(driver, "text", "email ya existente");
    }

    //Inicio de sesion con datos validos (administrador)
    @Test
    public void PR05() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        PO_NavView.checkElement(driver, "id", "users-list");
    }

    //Inicio de sesion con datos validos (estandar)
    @Test
    public void PR06() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");
        PO_NavView.checkElement(driver, "id", "products-menu");
        PO_NavView.elementoNoPresenteEnLaPagina(driver, "Ver usuarios");//Comprobar que no aparecen las opciones de
        // admin
    }

    //inicio de sesion con datos invalidos(email y password vacios)
    @Test
    public void PR07() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "", "");

        PO_LoginView.checkElement(driver, "text", "Identificate");

    }

    //inicio de sesion con datos invalidos(usuario estandar,email existente, pero contraseña incorrecta)
    @Test
    public void PR08() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "654321");

        PO_LoginView.checkElement(driver, "text", "Identificate");

    }

    //inicio de sesion con datos invalidos(usuario estandar,email no existente)
    @Test
    public void PR09() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "noExiste@email.com", "654321");

        PO_LoginView.checkElement(driver, "text", "Identificate");

    }

    //Click en la opcion de salir de sesion y comprobar que se redirige al login
    @Test
    public void PR10() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");

        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'account-menu')]/a");
        elementos.get(0).click();
        // Esperamos a aparezca la opción de desconectar
        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/logout')]");
        elementos.get(0).click();
        PO_LoginView.checkElement(driver, "text", "Identificate");
    }

    //Comprobar boton cerrar sesion si no esta visivle cuando el usauro no esta autenticado
    @Test
    public void PR11() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_NavView.elementoNoPresenteEnLaPagina(driver, "Desconectar");
    }

    //Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el sistema
    //por defecto existen 5 usuarios(1 admin,javi,ivan, 2 autogenerados)
    @Test
    public void PR12() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        PO_NavView.clickOption(driver, "/users/list", "class", "btn btn-primary");
        List<WebElement> elementos = PO_View
                .checkElement(driver, "free", "//tbody/tr");
        assertEquals(5, elementos.size());

    }

    //Ir a la lista de usuarios,borrar el primer usuario de la lista, comprobar que la lista se actualiza y dicho
    //usuario desaparece
    //Hay que tener en cuenta que, dado que solo existe un admin en el sistema, se ha optado por no dar la opcion
    //de borrarlo, por lo que no aparece la check box para este proposito, el admin es el primer usuario, asi que
    //para completar esta prueba se borrara el segundo usuario (Javi), disculpen las molestias
    @Test
    public void PR13(){
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        PO_NavView.clickOption(driver, "/users/list", "class", "btn btn-primary");
        List<WebElement> elementos = PO_View
                .checkElement(driver, "free", "//input");
        elementos.get(0).click();
        PO_ListUsers.clickDeleteButton(driver);
        elementos = PO_View
                .checkElement(driver, "free", "//tbody/tr");
        assertEquals(4,elementos.size());
        PO_ListUsers.elementoNoPresenteEnLaPagina(driver,"Javi");
    }

    //Ir a la lista de los usuarios,borrar el ultimo de la lista y comprobar que es ultimo desaparece
    @Test
    public void PR14(){
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        PO_NavView.clickOption(driver, "/users/list", "class", "btn btn-primary");
        List<WebElement> elementos = PO_View
                .checkElement(driver, "free", "//input");
        elementos.get(elementos.size()-1).click();

        List<WebElement> elementosABorrar=PO_View.checkElement(driver,"free","//td");
        String elementoAEliminar=elementosABorrar.get(elementosABorrar.size()-3).getText();//Obtenemos la antepenultima
        //td, con su texto en el interior, que sera el elemento que se borrara y cuyo texto debemos comprobar
        //que ya no existe

        PO_ListUsers.clickDeleteButton(driver);
        elementos = PO_View
                .checkElement(driver, "free", "//tbody/tr");
        assertEquals(4,elementos.size());
        PO_ListUsers.elementoNoPresenteEnLaPagina(driver,elementoAEliminar);
    }

    //Ir a la lista de los usuarios,borrar 3 usuarios, comprobar que la lista se actualiza y esos usuarios desaparecen

    @Test
    public void PR15(){
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        PO_NavView.clickOption(driver, "/users/list", "class", "btn btn-primary");
        List<WebElement> elementos = PO_View
                .checkElement(driver, "free", "//input");
        elementos.get(0).click();
        elementos.get(1).click();
        elementos.get(2).click();
        PO_ListUsers.clickDeleteButton(driver);
        elementos = PO_View
                .checkElement(driver, "free", "//tbody/tr");
        assertEquals(2,elementos.size());
        PO_ListUsers.elementoNoPresenteEnLaPagina(driver,"Ivan");
        PO_ListUsers.elementoNoPresenteEnLaPagina(driver,"Javi");
        PO_ListUsers.elementoNoPresenteEnLaPagina(driver,"User01");
    }

    //Ir al formulario de alta de oferta, rellenarla con datos validos, y comprobar que la oferta se ha añadido al
    //listado de ofertas del usuario
    @Test
    public void PR16(){
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");

        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'products-menu')]/a");
        elementos.get(0).click();
        // Esperamos a aparezca la opción de desconectar
        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/sales/addProduct')]");
        elementos.get(0).click();

        PO_AddProduct.rellenarFormulario(driver,"NuevoElemento","Una descripcion","12");
        PO_AddProduct.checkElement(driver,"text","NuevoElemento");
    }

    //Ir al formulario de alta de oferta, rellenarla con datos invalidos, y comprobar que se muestra el error en la pagina
    @Test
    public void PR17(){
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");

        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'products-menu')]/a");
        elementos.get(0).click();
        // Esperamos a aparezca la opción de desconectar
        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/sales/addProduct')]");
        elementos.get(0).click();

        PO_AddProduct.rellenarFormulario(driver,"","","");
        PO_AddProduct.checkElement(driver,"text","Crear oferta");
    }

    //Mostrar las ofertas de un usuario y comprobar que se muestran todas sus ofertas
    @Test
    public void PR18(){
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");

        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'products-menu')]/a");
        elementos.get(0).click();
        // Esperamos a aparezca la opción de desconectar
        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/sales/list')]");
        elementos.get(0).click();

        elementos = PO_View
                .checkElement(driver, "free", "//tbody/tr");
        assertEquals(3,elementos.size());

    }

    //Borrar primera oferta de un usuario, comprobar que se borra
    @Test
    public void PR19(){
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");

        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'products-menu')]/a");
        elementos.get(0).click();
        String elementoAEliminar=PO_View.checkElement(driver,"free","//td").get(0).getText();
        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/sales/delete')]");

        elementos.get(0).click();

        elementos = PO_View
                .checkElement(driver, "free", "//tbody/tr");
        assertEquals(2,elementos.size());
        PO_View.elementoNoPresenteEnLaPagina(driver,elementoAEliminar);

    }

    //Borrar ultima oferta de un usuario, comprobar que se borra
    @Test
    public void PR20(){
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");

        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'products-menu')]/a");
        elementos.get(0).click();

        List<WebElement> elementosABorrar=PO_View.checkElement(driver,"free","//td");
        String elementoAEliminar=elementosABorrar.get(elementosABorrar.size()-3).getText();//Obtenemos la antepenultima
        //td, con su texto en el interior, que sera el elemento que se borrara y cuyo texto debemos comprobar
        //que ya no existe

        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/sales/delete')]");


        elementos.get(elementos.size()-1).click();

        elementos = PO_View
                .checkElement(driver, "free", "//tbody/tr");
        assertEquals(2,elementos.size());
        PO_View.elementoNoPresenteEnLaPagina(driver,elementoAEliminar);

    }

    //Buscar ofertas con el cuadro de busqueda vacio y comprobar que se muestran las ofertas del sistema
    @Test
    public void PR21(){
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");

        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href,'/buy/list')]");
        elementos.get(0).click();

        PO_BuyList.rellenarCuadroDeBusqueda(driver,"");

       elementos = PO_View
                .checkElement(driver, "free", "//tbody/tr");

        assertEquals(5,elementos.size());//5 por hoja como  maximo
    }

    //Buscar ofertas con el cuadro de busqueda algo que no exista y comprobar que no se mustra nada
    @Test
    public void PR22(){
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");

        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href,'/buy/list')]");
        elementos.get(0).click();

        PO_BuyList.rellenarCuadroDeBusqueda(driver,"AlgoQueNoExiste");


        List<WebElement> list = driver.findElements(By.tagName("td"));


        assertEquals(0,list.size());
    }


    //comprar oferta con saldo positivo
    @Test
    public void PR23() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href,'/buy/list')]");
        elementos.get(0).click();
        PO_BuyList.rellenarCuadroDeBusqueda(driver, "Producto positivo");
        PO_BuyList.comprarOferta(driver, 0);
        PO_NavView.clickCuenta(driver);
        PO_View.checkElement(driver, "text", "80");
    }


    //comprar oferta con saldo a 0
    @Test
    public void PR24() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href,'/buy/list')]");
        elementos.get(0).click();
        PO_BuyList.rellenarCuadroDeBusqueda(driver, "Producto cero");
        PO_BuyList.comprarOferta(driver, 0);
        PO_NavView.clickCuenta(driver);
        PO_View.checkElement(driver, "text", "0");
    }

    //comprar oferta con saldo a negativo
    @Test
    public void PR25() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href,'/buy/list')]");
        elementos.get(0).click();
        PO_BuyList.rellenarCuadroDeBusqueda(driver, "Producto negativo");
        PO_BuyList.comprarOferta(driver, 0);
        PO_NavView.clickCuenta(driver);
        PO_View.checkElement(driver, "text", "No tienes");
    }

    //ver ofertas compradas
    @Test
    public void PR26() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");
        PO_NavView.clickOption(driver, "/bought/list", "text", "Precio");
        PO_BoughtOffers.countMyOffers(driver, 2);

    }

    //Internacionalizacion de 4 paginas
    @Test
    public void PR27() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");

        //Tienda
        PO_NavView.clickOption(driver, "/buy/list", "text", "Buscar");
        PO_NavView.changeIdiom(driver,"btnEnglish");
        PO_View.checkElement(driver,"text","Search");
        PO_NavView.changeIdiom(driver,"btnSpanish");
        PO_View.checkElement(driver,"text","Buscar");
        //Productos comprados
        PO_NavView.clickOption(driver, "/bought/list", "text", "Precio");
        PO_NavView.changeIdiom(driver,"btnEnglish");
        PO_View.checkElement(driver,"text","Price");
        PO_NavView.changeIdiom(driver,"btnSpanish");
        PO_View.checkElement(driver,"text","Precio");
        //Usuarios
        PO_NavView.clickOption(driver, "/users/list", "text", "Nombre");
        PO_NavView.changeIdiom(driver,"btnEnglish");
        PO_View.checkElement(driver,"text","Name");
        PO_NavView.changeIdiom(driver,"btnSpanish");
        PO_View.checkElement(driver,"text","Nombre");
        //Alta de oferta
        //Dropdown
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'products-menu')]/a");
        elementos.get(0).click();
        PO_NavView.clickOption(driver, "/sales/addProduct", "text", "Título");
        PO_NavView.changeIdiom(driver,"btnEnglish");
        PO_View.checkElement(driver,"text","title");
        PO_NavView.changeIdiom(driver,"btnSpanish");
        PO_View.checkElement(driver,"text","Título");

    }

    //Seguridad listar usuarios
    @Test
    public void PR28() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_View.elementoNoPresenteEnLaPagina(driver,"Usuarios");
        driver.navigate().to(URL+"/users/list");//forzar acceso aunque no aparezca el boton
        PO_View.checkElement(driver,"text","eMail");
    }

    //Seguridad listar ofertas propias
    @Test
    public void PR29() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_View.elementoNoPresenteEnLaPagina(driver,"productos");
        driver.navigate().to(URL+"/sales/list");//forzar acceso aunque no aparezca el boton
        PO_View.checkElement(driver,"text","eMail");
    }

    //Seguridad listar usuarios sin ser administrador
    @Test
    public void PR30() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");
        PO_View.elementoNoPresenteEnLaPagina(driver,"Usuarios");
        driver.navigate().to(URL+"/users/list");//forzar acceso aunque no aparezca el boton

        PO_View.checkElement(driver,"free","//*[contains(text(),'Forbidden')]");
    }

    //Mensaje en nueva conversacion
    @Test
    public void PR31() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");

        PO_NavView.clickOption(driver, "/buy/list", "text", "Buscar");
        PO_BuyList.rellenarCuadroDeBusqueda(driver,"Cuadro");
        PO_BuyList.mensajeAOferta(driver,0);
        PO_Mensajes.enviarMensaje(driver,"Hola");
        PO_View.checkElement(driver,"text","Hola");
    }

    //Mensaje de una conversacion ya existente
    @Test
    public void PR32() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");

        PO_NavView.clickOption(driver, "/conversations/list", "text", "Listado");
        PO_ConversationList.seeConversation(driver,0);
        PO_View.checkElement(driver,"text","Mensaje1");
        PO_Mensajes.enviarMensaje(driver,"Hola");
        PO_View.checkElement(driver,"text","Hola");
    }

    //Mostrar listado de conversaciones, comprobar que hay las qu debe haber
    @Test
    public void PR33() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");

        PO_NavView.clickOption(driver, "/conversations/list", "text", "Listado");
        PO_ConversationList.countConversations(driver,2);

    }

    //Sobre enlace de conversaciones, pinchar la primera y borrarla
    @Test
    public void PR34() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");

        PO_NavView.clickOption(driver, "/conversations/list", "text", "Listado");
        PO_ConversationList.deleteConversation(driver,0);
        PO_ConversationList.countConversations(driver,1);

    }

    //Sobre enlace de conversaciones, pinchar la ultima y borrarla
    @Test
    public void PR35() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");

        PO_NavView.clickOption(driver, "/conversations/list", "text", "Listado");
        PO_ConversationList.deleteConversation(driver,1);
        PO_ConversationList.countConversations(driver,1);

    }

    //Crear oferta y destacarla durante la creacion
    @Test
    public void PR36() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");

        //add offer
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'products-menu')]/a");
        elementos.get(0).click();

        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/sales/addProduct')]");
        elementos.get(0).click();

        PO_AddProduct.rellenarFormularioDestacado(driver,"NuevoElemento","Una descripcion","12");
        //see highlighted
         elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'products-menu')]/a");
        elementos.get(0).click();

        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/sales/highlighted')]");
        elementos.get(0).click();

        PO_View.checkElement(driver,"text","NuevoElemento");

        PO_NavView.clickCuenta(driver);
        PO_View.checkElement(driver, "text", "80");

    }

    //Sobre el listado de ofertas destacarla
    @Test
    public void PR37() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "javi@email.com", "123456");

        String elementoDestacado=PO_OfertasPropias.destacar(driver,0);
        //see highlighted
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'products-menu')]/a");
        elementos.get(0).click();

        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/sales/highlighted')]");
        elementos.get(0).click();

        PO_View.checkElement(driver,"text",elementoDestacado);

        PO_NavView.clickCuenta(driver);
        PO_View.checkElement(driver, "text", "80");

    }

    //Sobre el listado de ofertas destacarla
    @Test
    public void PR38() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "user3@email.com", "123456");

        String elementoDestacado=PO_OfertasPropias.destacar(driver,0);
        //see error

        PO_View.checkElement(driver, "text", "No tienes");

    }


}