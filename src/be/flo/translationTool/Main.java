package be.flo.translationTool;

import be.flo.translationTool.reader.Reader;
import be.flo.translationTool.writer.Writer;

/**
 * Created by florian on 11/05/15.
 */
public class Main {


    public static void main(String[] args){

        Reader reader = new Reader();
        reader.browse();
        Writer writer =new Writer();
        try {
            writer.write(reader.getKeyMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("FINISH !! ");


    }
}
