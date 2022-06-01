package edu.zjut.zzy.ticklist.PDFReader;

import java.util.ArrayList;

import edu.zjut.zzy.ticklist.bean.Course;

public interface PDFReader {
    public ArrayList<Course> ReadCourses(String PDFPath);
}
