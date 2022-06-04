package edu.zjut.zzy.ticklist.PDFReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import edu.zjut.zzy.ticklist.bean.Course;

public class ZJUTCourseReader implements CouseReader{

    @Override
    public ArrayList<Course> parseCourse(String htmlContent) {
        ArrayList<Course> coursesList = new ArrayList<>();
        Document doc = Jsoup.parse(htmlContent);
//        System.out.println(doc);
//        System.out.println("---------------------------------------------------");
        for(int i = 1; i <= 7; i++){
            for(int j = 1; j <= 12; j++){
                Element element = doc.getElementById(i + "-" + j);
                if(element != null && !element.text().equals("")){
                    System.out.println(i + "-" + j + "：  " + element.text());
                    String[] cutsInfo = element.text().split(" ");

                    coursesList.add(new Course(cutsInfo, i));
                }
            }
        }
//        System.out.println("arraylist: ----------------------");
        System.out.println(coursesList);
        return null;
    }
}
