package edu.zjut.zzy.ticklist.PDFReader;

import java.util.ArrayList;

import edu.zjut.zzy.ticklist.bean.Course;

public interface CouseReader {
    public ArrayList<Course> parseCourse(String htmlContent);
}
