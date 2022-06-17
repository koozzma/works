package com.javarush.task.task28.task2810.view;

import com.javarush.task.task28.task2810.Controller;
import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.List;

public class HtmlView implements View{
    private final String filePath = "./4.JavaCollections/src/" + this.getClass().getPackage().getName().replaceAll("[.]", "/") + "/vacancies.html";

    private Controller controller;
    @Override
    public void update(List<Vacancy> vacancies) {
        String newContent = getUpdatedFileContent(vacancies);
        updateFile(newContent);

    }

    @Override
    public void setController(Controller controller) {
    this.controller = controller;
    }

    public void userCitySelectEmulationMethod(){
      controller.onCitySelect("Odessa");

    }

    private String getUpdatedFileContent(List<Vacancy> vacancies){

        try {
            Document doc = getDocument();
            Elements templateHidden = doc.getElementsByClass("template");
            Element template = templateHidden.clone().removeAttr("style").removeClass("template").get(0);

            Elements prevVacancies = doc.getElementsByClass("vacancy");
            for(Element element : prevVacancies) {
                if (!element.hasClass("template")) {
                    element.remove();
                }
            }

                for (Vacancy vacancy : vacancies) {
                    Element vacancyElement = template.clone();
                    Element vacancyLink = vacancyElement.getElementsByAttribute("href").get(0);
                    vacancyLink.appendText(vacancy.getTitle());
                    vacancyLink.attr("href", vacancy.getUrl());
                    Element city = vacancyElement.getElementsByClass("city").get(0);
                    city.appendText(vacancy.getCity());
                    Element companyName = vacancyElement.getElementsByClass("companyName").get(0);
                    companyName.appendText(vacancy.getCompanyName());
                    Elements salaryEs = vacancyElement.getElementsByClass("salary");
                    Element salary = salaryEs.get(0);
                    salary.appendText(vacancy.getSalary());

                    templateHidden.before(vacancyElement.outerHtml());
                }

                return doc.html();
        } catch (IOException e) {
            e.printStackTrace();
        }
       return "Some exception occurred";
    }
    private void updateFile(String content) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream((new File(filePath)));
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
             e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected Document getDocument() throws IOException{
        return Jsoup.parse(new File(filePath),"UTF-8");
    }
}
