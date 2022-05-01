package ru.savuri.webprak.web;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.context.TestPropertySource;
import org.springframework.ui.context.Theme;
import ru.savuri.webprak.model.dao.GoodDAO;
import ru.savuri.webprak.model.dao.OrderDAO;
import ru.savuri.webprak.model.dao.OrderGoodDAO;
import ru.savuri.webprak.model.dao.UserDAO;
import ru.savuri.webprak.model.entity.Good;
import ru.savuri.webprak.model.entity.Order;
import ru.savuri.webprak.model.entity.OrderGood;
import ru.savuri.webprak.model.entity.User;

import java.awt.font.TextHitInfo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
public class SeleniumTests {
    @LocalServerPort
    private int port;
    @Autowired
    private WebDriver driver;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private GoodDAO goodDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private OrderGoodDAO orderGoodDAO;

    private void waitLoad() {
        new WebDriverWait(driver, 1).until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

    @Test
    void testGoodsContent() throws InterruptedException {
        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.linkText("Goods")).click();
        waitLoad();
        List<WebElement> elements = driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));

        List<String> expected = new ArrayList<String>() {
            {
                add("GA-B379SQUL Kitchen LG 46687 2");
                add("86QNED916PA NanoCell, HDR, QNED (2021), черный/серый TV LG 399990 22");
                add("D540MC-I58500004R MT i5 8500 (3) 8Gb SSD512Gb UHDG 630 Win 10 Pro Computer ASUS 74990 12");
                add("abcde12345 Bathroom 1 1 1");
            }
        };

        assertEquals(4, elements.size());

        for (int i = 0; i < elements.size(); ++i) {
            assertEquals(expected.get(0), elements.get(0).getText());
        }

        driver.findElement(By.linkText("GA-B379SQUL")).click();
        assertEquals("Name: LG GA-B379SQUL", driver.findElement(By.xpath("/html/body/main/div/span[1]")).getText());
    }

    @Test
    void testGoodsSearch() throws InterruptedException {
        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.linkText("Goods")).click();
        waitLoad();

        // empty search
        driver.findElement(By.xpath("//*[@id=\"typeInput5\"]")).click(); // OTHER search -- Empty
        driver.findElement(By.xpath("/html/body/main/form/button")).click();
        waitLoad();
        List<WebElement> elements = driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));
        assertEquals(0, elements.size());

        // one search
        driver.findElement(By.xpath("//*[@id=\"typeInput3\"]")).click(); // TV search  -- 86QNED916PA NanoCell, HDR, QNED (2021), черный/серый	TV	LG	399990	22
        driver.findElement(By.xpath("/html/body/main/form/button")).click();
        waitLoad();
        String expected = "86QNED916PA NanoCell, HDR, QNED (2021), черный/серый TV LG 399990 22";
        elements = driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));
        assertEquals(1, elements.size());
        assertEquals(expected, elements.get(0).getText());

        // empty search
        driver.findElement(By.xpath("/html/body/main/form/button")).click();
        waitLoad();
        elements = driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));
        List<String> expected1 = new ArrayList<String>() {
            {
                add("GA-B379SQUL Kitchen LG 46687 2");
                add("86QNED916PA NanoCell, HDR, QNED (2021), черный/серый TV LG 399990 22");
                add("D540MC-I58500004R MT i5 8500 (3) 8Gb SSD512Gb UHDG 630 Win 10 Pro Computer ASUS 74990 12");
                add("abcde12345 Bathroom 1 1 1");
            }
        };


        assertEquals(4, elements.size());

        for (int i = 0; i < elements.size(); ++i) {
            assertEquals(expected1.get(i), elements.get(i).getText());
        }

        // all search
        String manufacturerInput = "LG";
        String descriptionInput = "No Frost";
        expected = "GA-B379SQUL Kitchen LG 46687 2";
        driver.findElement(By.xpath("//*[@id=\"typeInput1\"]")).click(); // Kitchen
        driver.findElement(By.id("manufacturerInput")).sendKeys(manufacturerInput);
        driver.findElement(By.id("descriptionInput")).sendKeys(descriptionInput);
        driver.findElement(By.xpath("/html/body/main/form/button")).click();
        waitLoad();
        elements = driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));
        assertEquals(1, elements.size());
        assertEquals(expected, elements.get(0).getText());
    }

    @Test
    void testGoodCreate() throws InterruptedException {
        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.linkText("Goods")).click();
        waitLoad();
        driver.findElement(By.linkText("Create good")).click();
        waitLoad();

        Good expected = new Good("123", Good.GoodType.OTHER, 100, 100, "man", "Франция", "description");

        driver.findElement(By.id("modelInput")).sendKeys(expected.getModel());
        driver.findElement(By.id("manufacturerInput")).sendKeys(expected.getManufacturer());
        driver.findElement(By.id("assemblyPlace")).sendKeys(expected.getAssemblyPlace());
        driver.findElement(By.id("priceInput")).sendKeys(String.valueOf(expected.getPrice()));
        driver.findElement(By.id("quantityInput")).sendKeys(String.valueOf(expected.getQuantity()));
        driver.findElement(By.id("inputDescription")).sendKeys(expected.getDescription()); // OTHER
        driver.findElement(By.id("typeInput5")).click(); // OTHER
        waitLoad();
        driver.findElement(By.xpath("/html/body/main/form/button")).submit();
        waitLoad();

        List<Good> got = goodDAO.getByFilter(GoodDAO.GoodFilter.builder().type(Good.GoodType.OTHER).build());
        assertEquals(1, got.size());
        expected.setId(got.get(0).getId());
        assertEquals(got.get(0), expected);
    }

    @Test
    void testGoodEdit() throws InterruptedException {
        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.linkText("Goods")).click();
        waitLoad();
        driver.findElement(By.linkText("GA-B379SQUL")).click();
        waitLoad();
        driver.findElement(By.xpath("/html/body/main/a/button")).click();
        waitLoad();

        String expectedModel = "newModel";
        driver.findElement(By.id("modelInput")).clear();
        driver.findElement(By.id("modelInput")).sendKeys(expectedModel);
        waitLoad();
        driver.findElement(By.xpath("/html/body/main/form/button")).submit();
        waitLoad();
        driver.findElement(By.linkText("Goods")).click();
        waitLoad();
        driver.findElement(By.xpath("//*[@id=\"typeInput1\"]")).click();
        driver.findElement(By.xpath("/html/body/main/form/button")).click();
        waitLoad();
        List<WebElement> elements = driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));
        assertEquals(1, elements.size());
        assertEquals(expectedModel + " Kitchen LG 46687 2", elements.get(0).getText());
    }

    @Test
    void testGoodDelete() throws InterruptedException {
        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.linkText("Goods")).click();
        waitLoad();
        driver.findElement(By.linkText("GA-B379SQUL")).click();
        waitLoad();
        driver.findElement(By.xpath("/html/body/main/form/button")).submit();
        waitLoad();
        Thread.sleep(300);
        WebElement element = driver.findElement(By.id("errorMsg"));
        assertEquals("Error msg: This good can not be deleted because exist order which contain this good.", element.getText());
        driver.findElement(By.linkText("Goods")).click();
        waitLoad();
        List<WebElement> elements = driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));
        List<String> expected1 = new ArrayList<String>() {
            {
                add("GA-B379SQUL Kitchen LG 46687 2");
                add("86QNED916PA NanoCell, HDR, QNED (2021), черный/серый TV LG 399990 22");
                add("D540MC-I58500004R MT i5 8500 (3) 8Gb SSD512Gb UHDG 630 Win 10 Pro Computer ASUS 74990 12");
                add("abcde12345 Bathroom 1 1 1");
            }
        };

        assertEquals(4, elements.size());

        for (int i = 0; i < elements.size(); ++i) {
            assertEquals(expected1.get(i), elements.get(i).getText());
        }

        driver.findElement(By.linkText("abcde12345")).click();
        waitLoad();
        driver.findElement(By.xpath("/html/body/main/form/button")).submit();
        waitLoad();
        Thread.sleep(300);
        elements = driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));
        expected1 = new ArrayList<String>() {
            {
                add("GA-B379SQUL Kitchen LG 46687 2");
                add("86QNED916PA NanoCell, HDR, QNED (2021), черный/серый TV LG 399990 22");
                add("D540MC-I58500004R MT i5 8500 (3) 8Gb SSD512Gb UHDG 630 Win 10 Pro Computer ASUS 74990 12");
            }
        };

        Thread.sleep(300);

        assertEquals(3, elements.size());

        for (int i = 0; i < elements.size(); ++i) {
            assertEquals(expected1.get(i), elements.get(i).getText());
        }
    }

    @Test
    void testGoodInfo() {
        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.linkText("Goods")).click();
        waitLoad();
        driver.findElement(By.linkText("GA-B379SQUL")).click();
        waitLoad();
        WebElement element = driver.findElement(By.id("divId"));
        String expected = "Name: LG GA-B379SQUL\n" +
                "Assembly place: Китай\n" +
                "Price: 46687\n" +
                "Quantity: 2\n" +
                "Type: Kitchen\n" +
                "Description:\n" +
                "ШхВхГ 59.50х173.70х65.50 с\n" +
                "Общий объем 261 л\n" +
                "Размораживание No Frost\n" +
                "Класс энергопотребления A+\n" +
                "Объем холодильной камеры 182 л\n" +
                "Объем морозильной камеры 79 л\n" +
                "Тип компрессора инверторный\n" +
                "Мощность замораживания 9.3 кг/сут\n" +
                "Режимы суперзаморозка\n" +
                "Особенности конструкции перевешиваемые двери, дисплей\n" +
                "Дополнительные функции индикация температуры, защита от детей";
        assertEquals(expected, element.getText());
    }

    @Test
    void testOrdersContent() {
        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.linkText("Orders")).click();
        waitLoad();
        List<WebElement> elements = driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));

        List<String> expected = new ArrayList<String>() {
            {
                add("8 Egor Ivanov Ivanovich 2020-08-08T04:05 г. Москва ул. Пушкина дом 22 кв 33 PROCESSING");
                add("11 Egor Ivanov Ivanovich 2020-01-08T04:05 Самовывоз DELIVERED");
                add("13 Andrey Ray Xray 2020-09-12T04:05 г. Москва ул. Лермонтова дом 11 кв 15 SHIPPED");
            }
        };

        assertEquals(3, elements.size());

        for (int i = 0; i < elements.size(); ++i) {
            assertEquals(expected.get(i), elements.get(i).getText());
        }

        driver.findElement(By.linkText("8")).click();
        Order expectedOrder = orderDAO.getById(8L);
        assertEquals("Id: 8", driver.findElement(By.xpath("/html/body/main/div/span[1]")).getText());
    }

    @Test
    void testOrderCreate() throws InterruptedException {
        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.linkText("Orders")).click();
        waitLoad();
        driver.findElement(By.linkText("Create order")).click();
        waitLoad();

        String dateString = "08/08/002020 11:05";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        User user = userDAO.getAll().get(0);
        Order expectedOrder = new Order(new HashSet<>(), user, LocalDateTime.parse("2020-08-08 11:05:00", formatter), "г. Москва ул. Пушкина дом 22 кв 33", Order.Status.PROCESSING);
        Good expectedGood = goodDAO.getAll().get(0);

        // not sufficient of material
        driver.findElement(By.id("customerIdInput")).sendKeys("123123");
        driver.findElement(By.id("deliveryDateInput")).sendKeys(String.valueOf(dateString));
        driver.findElement(By.id("deliveryPlaceInput")).sendKeys(expectedOrder.getDeliveryPlace());
        driver.findElement(By.id("QuantityId" + String.valueOf(expectedGood.getId()))).sendKeys("12312312");
        driver.findElement(By.xpath("/html/body/main/form/button")).submit();
        waitLoad();
        Thread.sleep(300);
        waitLoad();
        WebElement element = driver.findElement(By.id("errorMsg"));
        assertEquals("Error msg: Wrong purchase quantity for GA-B379SQUL LG", element.getText());
        //
        driver.findElement(By.linkText("Orders")).click();
        waitLoad();
        driver.findElement(By.linkText("Create order")).click();
        waitLoad();
        //empty basket
        driver.findElement(By.id("customerIdInput")).sendKeys(String.valueOf(expectedOrder.getUser().getId()));
        driver.findElement(By.id("deliveryDateInput")).sendKeys(String.valueOf(dateString));
        driver.findElement(By.id("deliveryPlaceInput")).sendKeys(expectedOrder.getDeliveryPlace());
        waitLoad();
        driver.findElement(By.xpath("/html/body/main/form/button")).submit();
        waitLoad();
        Thread.sleep(300);
        element = driver.findElement(By.id("errorMsg"));
        assertEquals("Error msg: Empty basket", element.getText());

        driver.findElement(By.linkText("Orders")).click();
        waitLoad();
        driver.findElement(By.linkText("Create order")).click();
        waitLoad();

        //user does not exist
        driver.findElement(By.id("customerIdInput")).sendKeys("123123");
        driver.findElement(By.id("deliveryDateInput")).sendKeys(String.valueOf(dateString));
        driver.findElement(By.id("deliveryPlaceInput")).sendKeys(expectedOrder.getDeliveryPlace());
        driver.findElement(By.id("QuantityId" + String.valueOf(expectedGood.getId()))).sendKeys("1");
        waitLoad();
        driver.findElement(By.xpath("/html/body/main/form/button")).submit();
        waitLoad();
        Thread.sleep(300);
        element = driver.findElement(By.id("errorMsg"));
        assertEquals("Error msg: This user does not exists", element.getText());

        driver.findElement(By.linkText("Orders")).click();
        waitLoad();
        driver.findElement(By.linkText("Create order")).click();
        waitLoad();

        // successful create
        OrderGood expectedOrderGood = new OrderGood(expectedOrder, expectedGood, 1, expectedGood.getPrice());

        driver.findElement(By.id("customerIdInput")).sendKeys(String.valueOf(expectedOrder.getUser().getId()));
        driver.findElement(By.id("deliveryDateInput")).sendKeys(String.valueOf(dateString));
        driver.findElement(By.id("deliveryPlaceInput")).sendKeys(expectedOrder.getDeliveryPlace());
        driver.findElement(By.id("QuantityId" + String.valueOf(expectedGood.getId()))).sendKeys(String.valueOf(expectedOrderGood.getPurchaseQuantity()));
        waitLoad();
        driver.findElement(By.xpath("/html/body/main/form/button")).submit();
        Thread.sleep(300);
        Order actualOrder = orderDAO.getAll().get(orderDAO.getAll().size() - 1);
        OrderGood actualOrderGood = orderGoodDAO.getAll().get(orderGoodDAO.getAll().size() - 1);
        expectedOrder.setId(actualOrder.getId());
        expectedOrderGood.setId(actualOrderGood.getId());

        assertEquals(expectedOrder, actualOrder);
        assertEquals(expectedOrderGood, actualOrderGood);
    }

    @Test
    void testOrderDelete() throws InterruptedException {
        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.linkText("Orders")).click();
        waitLoad();
        driver.findElement(By.linkText("8")).click();
        waitLoad();
        driver.findElement(By.xpath("/html/body/main/form/button")).submit();
        waitLoad();
        Thread.sleep(500);
        List<WebElement> elements = driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));
        assertEquals(2, elements.size());

        List<String> expected1 = new ArrayList<String>() {
            {
                add("11 Egor Ivanov Ivanovich 2020-01-08T04:05 Самовывоз DELIVERED");
                add("13 Andrey Ray Xray 2020-09-12T04:05 г. Москва ул. Лермонтова дом 11 кв 15 SHIPPED");
            }
        };

        for (int i = 0; i < elements.size(); ++i) {
            assertEquals(expected1.get(i), elements.get(i).getText());
        }
    }

    @Test
    void testOrderSearch() throws InterruptedException {
        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.linkText("Orders")).click();
        waitLoad();

        // all search
        driver.findElement(By.id("userIdInput")).sendKeys("3");
        driver.findElement(By.xpath("/html/body/main/form/button")).click();
        waitLoad();
        List<WebElement> elements = driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));
        assertEquals(0, elements.size());
        driver.findElement(By.id("userIdInput")).sendKeys("5");
        driver.findElement(By.xpath("/html/body/main/form/button")).click();
        waitLoad();
        elements = driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));
        assertEquals(2, elements.size());
        // empty search
        // all search
        driver.findElement(By.linkText("Orders")).click();
        waitLoad();
        driver.findElement(By.id("userIdInput")).clear();
        driver.findElement(By.xpath("/html/body/main/form/button")).click();
        waitLoad();
        elements = driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));
        assertEquals(3, elements.size());
    }

    @Test
    void testUserContent() {
        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.linkText("Users")).click();
        waitLoad();
        List<WebElement> elements = driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));

        List<String> expected = new ArrayList<String>() {
            {
                add("Egor Ivanov Ivanovich +79556834566 EgorIvanov@mail.ru");
                add("Andrey Ray Xray +79556834333 RayXray@mail.ru");
                add("Antyan Ego Vil +79123124500 AntyanEgo@gmail.ru");
            }
        };

        assertEquals(3, elements.size());

        for (int i = 0; i < elements.size(); ++i) {
            assertEquals(expected.get(0), elements.get(0).getText());
        }

        driver.findElement(By.linkText("Egor Ivanov Ivanovich")).click();
        assertEquals("Full name: Egor Ivanov Ivanovich", driver.findElement(By.xpath("/html/body/main/div/span[2]")).getText());
    }

    @Test
    void testUserCreate() throws InterruptedException {
        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.linkText("Users")).click();
        waitLoad();
        driver.findElement(By.linkText("Create user")).click();
        waitLoad();

        User expected = new User("Name", "+71231231233", "Самовывоз", "my@mail.com");

        driver.findElement(By.id("fullNameInput")).sendKeys(expected.getFullName());
        driver.findElement(By.id("phoneNumberInput")).sendKeys(expected.getPhoneNumber());
        driver.findElement(By.id("addressInput")).sendKeys(expected.getAddress());
        driver.findElement(By.id("emailInput")).sendKeys(String.valueOf(expected.getEmail()));
        waitLoad();
        driver.findElement(By.xpath("/html/body/main/form/button")).submit();
        waitLoad();
        Thread.sleep(300);
        User user = userDAO.getAll().get(userDAO.getAll().size() - 1);
        expected.setId(user.getId());
        assertEquals(expected, user);
    }

    @Test
    void testUserDelete() throws InterruptedException {
        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.linkText("Users")).click();
        waitLoad();
        driver.findElement(By.linkText("Egor Ivanov Ivanovich")).click();
        waitLoad();
        driver.findElement(By.xpath("/html/body/main/form/button")).submit();
        waitLoad();
        Thread.sleep(500);
        List<WebElement> elements = driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));
        assertEquals(2, elements.size());

        List<String> expected1 = new ArrayList<String>() {
            {
                add("Andrey Ray Xray +79556834333 RayXray@mail.ru");
                add("Antyan Ego Vil +79123124500 AntyanEgo@gmail.ru");
            }
        };

        for (int i = 0; i < elements.size(); ++i) {
            assertEquals(expected1.get(i), elements.get(i).getText());
        }
    }

    @Test
    void testUserEdit() throws InterruptedException {
        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.linkText("Users")).click();
        waitLoad();
        driver.findElement(By.linkText("Andrey Ray Xray")).click();
        waitLoad();
        driver.findElement(By.xpath("/html/body/main/a/button")).click();
        waitLoad();

        String expectedName = "newName";
        driver.findElement(By.id("fullNameInput")).clear();
        driver.findElement(By.id("fullNameInput")).sendKeys(expectedName);
        waitLoad();
        driver.findElement(By.xpath("/html/body/main/form/button")).submit();
        Thread.sleep(300);
        waitLoad();
        driver.findElement(By.linkText("Users")).click();
        waitLoad();
        List<WebElement> elements = driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));
        assertEquals(3, elements.size());
        assertEquals(expectedName + " +79556834333 RayXray@mail.ru", elements.get(1).getText());
    }

    @Test
    void testUserEditCreateCancel() throws InterruptedException {
        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.linkText("Users")).click();
        waitLoad();
        driver.findElement(By.linkText("Create user")).click();
        waitLoad();
        driver.findElement(By.xpath("/html/body/main/form/a/button")).click();
        waitLoad();
        assertEquals("Users", driver.getTitle());
    }

    @Test
    void testGoodEditCreateCancel() throws InterruptedException {
        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.linkText("Goods")).click();
        waitLoad();
        driver.findElement(By.linkText("Create good")).click();
        waitLoad();
        WebElement element = driver.findElement(By.xpath("/html/body/main/form/a/button"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(500);
        element.click();
        waitLoad();

        assertEquals("Goods", driver.getTitle());
    }

    @Test
    void testOrderEditCreateCancel() {
        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.linkText("Orders")).click();
        waitLoad();
        driver.findElement(By.linkText("Create order")).click();
        waitLoad();
        driver.findElement(By.xpath("/html/body/main/form/a/button")).click();
        waitLoad();
        assertEquals("Orders", driver.getTitle());
    }


    @BeforeEach
    public void setUp() {
        List<Good> goodList = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        List<Order> orderList = new ArrayList<>();
        List<OrderGood> orderGoodList = new ArrayList<>();

        goodList.add(new Good("GA-B379SQUL", Good.GoodType.KITCHEN, 46687, 2, "LG", "Китай", "ШхВхГ	59.50х173.70х65.50 с\n" + "Общий объем\t261 л\n" + "Размораживание	No Frost\n" + "Класс энергопотребления\tA+\n" + "Объем холодильной камеры\t182 л\n" + "Объем морозильной камеры\t79 л\n" + "Тип компрессора\tинверторный\n" + "Мощность замораживания	9.3 кг/сут\n" + "Режимы	суперзаморозка\n" + "Особенности конструкции\tперевешиваемые двери, дисплей\n" + "Дополнительные функции	индикация температуры, защита от детей\n"));
        goodList.add(new Good("86QNED916PA NanoCell, HDR, QNED (2021), черный/серый", Good.GoodType.TV, 399990, 22, "LG", "Китай", "Диагональ\t86\"\n" + "Разрешение HD\t4K UHD\n" + "Частота обновления экрана\t100 Гц\n" + "Форматы HDR\tDolby Vision, HDR 10 Pro\n" + "Технология экрана\tHDR, NanoCell, QNED\n" + "Разъемы и интерфейсы\tвход аудио коаксиальный, выход на наушники, Ethernet - RJ-45, USB Type-A x 3, вход HDMI x 4, слот CI/CI+, выход аудио оптический\n" + "Беспроводная связь\tMiracast, Airplay, Wi-Fi, Bluetooth\n" + "Год создания модели\t2021\n" + "Платформа Smart TV\twebOS\n" + "Экосистема умного дома\tApple HomeKit, LG Smart ThinQ\n" + "Разрешение\t3840x2160\n" + "Тип матрицы экрана\tIPS\n" + "Тип подсветки\tmini-LED\n" + "Мощность звука\t40 Вт"));
        goodList.add(new Good("D540MC-I58500004R MT i5 8500 (3) 8Gb SSD512Gb UHDG 630 Win 10 Pro", Good.GoodType.COMPUTER, 74990, 12, "ASUS", "Россия", "Линейка процессора\tIntel Core i5\n" + "Частота процессора\t3000 МГц\n" + "Объем оперативной памяти\t8 ГБ\n" + "Видеокарта\tIntel UHD Graphics 630\n" + "Общий объем накопителей SSD\t512\n" + "Операционная система\tWindows 10 Pro\n" + "Беспроводные интерфейсы\tнет\n" + "Игровой компьютер"));
        goodList.add(new Good("abcde12345", Good.GoodType.BATHROOM, 1, 1, "1", "1", "1"));

        goodDAO.saveCollection(goodList);

        userList.add(new User("Egor Ivanov Ivanovich", "+79556834566", "г. Москва ул. Пушкина дом 22 кв 33", "EgorIvanov@mail.ru"));
        userList.add(new User("Andrey Ray Xray", "+79556834333", "г. Москва ул. Дружко дом 2 кв 3", "RayXray@mail.ru"));
        userList.add(new User("Antyan Ego Vil", "+79123124500", "г. Москва ул. Лермонтова дом 11 кв 15", "AntyanEgo@gmail.ru"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        orderList.add(new Order(new HashSet<>(), userList.get(0), LocalDateTime.parse("2020-08-08 04:05:00", formatter), "г. Москва ул. Пушкина дом 22 кв 33", Order.Status.PROCESSING));
        orderList.add(new Order(new HashSet<>(), userList.get(0), LocalDateTime.parse("2020-01-08 04:05:00", formatter), "Самовывоз", Order.Status.DELIVERED));
        orderList.add(new Order(new HashSet<>(), userList.get(1), LocalDateTime.parse("2020-09-12 04:05:00", formatter), "г. Москва ул. Лермонтова дом 11 кв 15", Order.Status.SHIPPED));

        orderGoodList.add(new OrderGood(orderList.get(0), goodList.get(0), 2, 46000));
        orderGoodList.add(new OrderGood(orderList.get(0), goodList.get(1), 1, 400000));
        orderGoodList.add(new OrderGood(orderList.get(1), goodList.get(2), 3, 75000));
        orderGoodList.add(new OrderGood(orderList.get(2), goodList.get(0), 1, 46000));

        orderList.get(0).setOrderGoods(Stream.of(orderGoodList.get(0), orderGoodList.get(1)).collect(Collectors.toCollection(HashSet::new)));
        orderList.get(1).setOrderGoods(Stream.of(orderGoodList.get(2)).collect(Collectors.toCollection(HashSet::new)));
        orderList.get(2).setOrderGoods(Stream.of(orderGoodList.get(3)).collect(Collectors.toCollection(HashSet::new)));


        goodList.get(0).setOrderGoods(Stream.of(orderGoodList.get(0), orderGoodList.get(3)).collect(Collectors.toCollection(HashSet::new)));
        goodList.get(1).setOrderGoods(Stream.of(orderGoodList.get(1)).collect(Collectors.toCollection(HashSet::new)));
        goodList.get(2).setOrderGoods(Stream.of(orderGoodList.get(2)).collect(Collectors.toCollection(HashSet::new)));

        userDAO.saveCollection(userList);

        userList.get(0).setOrders(Stream.of(orderList.get(0), orderList.get(1)).collect(Collectors.toCollection(HashSet::new)));
        userList.get(1).setOrders(Stream.of(orderList.get(2)).collect(Collectors.toCollection(HashSet::new)));
        userList.get(2).setOrders(new HashSet<>());

        orderDAO.saveCollection(orderList);

        for (User user : userList) {
            userDAO.update(user);
        }

        for (Good good : goodList) {
            goodDAO.update(good);
        }
    }

    @AfterEach
    void tearDown() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE users RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE orders RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE goods RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE ordergoods RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE hibernate_sequence RESTART WITH 1;").executeUpdate();
            session.getTransaction().commit();
        }
    }
}
