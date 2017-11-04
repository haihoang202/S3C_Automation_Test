import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.regex.Pattern;

public class S3C_Automation_Test {
    final static String EDIT_STRING_TEST = "THISISALONGSTRINGANDUNIQUETOTESTEDIT";
    public static void main(String [] args){
        System.setProperty("webdriver.chrome.driver","/Users/hoang/Workspace/Java_Workspace/SeleniumWebDriverTut/chromedriver");
        WebDriver driver = new ChromeDriver();

        String baseURL = "http://ec2-34-215-61-251.us-west-2.compute.amazonaws.com/S3C/S3Client/home.php";

        //Connect to the site
        driver.get(baseURL);

        if(driver.getTitle().contains("Semantic Search Search over Secured Data in the Cloud"))
            System.out.println("Successfully connected to the base URL");
        else{
            System.out.println("Cannot connect to the base URL");
            System.out.println(driver.getTitle());
            System.exit(1);
        }

        WebElement uploadBtn = driver.findElement(By.id("uploadPanel"));

        uploadBtn.click();

        WebElement uploadBox = driver.findElement(By.id("files"));

        uploadBox.sendKeys("/Users/hoang/Desktop/Shiitt/viet.txt");

        driver.findElement(By.xpath("//*[@id=\"uploadform\"]/p/a")).click();

        driver.findElement(By.id("pass")).sendKeys("hpccull16");

        driver.findElement(By.xpath("//*[@id=\"popup\"]/button[1]")).click();

        if(driver.findElement(By.xpath("//*[@id=\"uploadContentPanel\"]/div[3]")).getText().contains("Successfully Uploaded"))
            System.out.println("Uploaded Successfully: Done");
        else {
            System.out.println("Failed to upload");
            System.exit(1);
        }


        driver.findElement(By.id("searchPanel")).click();


        Select searchOpt = new Select(driver.findElement(By.name("opt")));

        searchOpt.selectByValue("1");
        WebElement searchQuery = driver.findElement(By.id("search"));
        searchQuery.sendKeys("vietnam ");

        WebElement submitBtn = driver.findElement(By.xpath("//*[@id=\"SBox\"]/button"));
        submitBtn.click();

        WebElement expandQueryBtn = driver.findElement(By.id("popoverData"));
        expandQueryBtn.click();
        String expandQuery = driver.findElement(By.className("popover-content")).getText();

        if(expandQuery.contains("Synonyms expansion") && expandQuery.contains("Wikipedia expansion")){
            System.out.println("Expansion query: Done");
        }
        else{
            System.out.println("Query is not expanded correctly.\nHere is the content of expanded query for debugging: ");
            System.out.println(expandQuery);
        }

        List<WebElement> rows = driver.findElements(By.xpath("//*[@id=\"searchContentPanel\"]/div[4]/div/table/tbody/tr"));
        if(rows.size() > 1)
            System.out.println("Got results: Done\nThere are " + rows.size() + " rows");
        else {
            System.out.println("There is no result returned from the query");
            System.exit(1);
        }

        List<WebElement> rowContent = rows.get(0).findElements(By.xpath("td"));
        WebElement operationsContent = rowContent.get(3);
        String thisFile = rowContent.get(1).getText();
        List<WebElement> operations = operationsContent.findElements(By.xpath("a"));

        System.out.println("Start to check operations of each file: ");

        WebElement edit = operations.get(0);
        edit.click();

        WebElement textarea = driver.findElement(By.name("text"));
        String fileContent = textarea.getText();

        if(fileContent != ""){
//            Pattern pattern = Pattern.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$");
//            if(pattern.matcher(fileContent).matches()){
//                System.out.println("File is decrypted correctly and ready to be edited: Done");
//            }
//            else{
//                System.out.println("[ERROR!] File is in encrypted format");
//                System.exit(1);
//            }

            fileContent += EDIT_STRING_TEST;

            textarea.clear();
            textarea.sendKeys(fileContent);

            String editURL = driver.getCurrentUrl();

            driver.findElement(By.xpath("/html/body/div/div/div/form/button")).click();

            textarea = driver.findElement(By.name("text"));
            fileContent = textarea.getText();

            if(fileContent.contains(EDIT_STRING_TEST)){
                System.out.println("Successfully Test edit operation: Done\nReverting the file to original state");
                fileContent.replace(EDIT_STRING_TEST,"");
            }

            driver.findElement(By.xpath("/html/body/div/div/div/form/button")).click();

        }
    }
}

