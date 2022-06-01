package edu.zjut.zzy.ticklist.PDFReader;


import android.graphics.pdf.PdfDocument;

import java.util.ArrayList;
import java.util.Arrays;

import edu.zjut.zzy.ticklist.bean.Course;

public class ZjutReader implements PDFReader{

    public String getWeekDay(int i){
        String weekDay="";
        switch (i) {
            case 2:  weekDay = "Monday"; break;
            case 3: weekDay = "Tuesday"; break;
            case 4: weekDay = "Wednesday"; break;
            case 5: weekDay = "Thursday"; break;
            case 6: weekDay = "Friday"; break;
            case 7: weekDay = "Saturday"; break;
            case 8: weekDay = "Sunday"; break;
        }
        return weekDay;
    }

    @Override
    public ArrayList<Course> ReadCourses(String PDFPath) {
        ArrayList<Course> courseList = new ArrayList<>();
        return null;
    }
}
