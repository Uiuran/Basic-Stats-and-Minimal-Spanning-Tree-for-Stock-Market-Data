/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BaseStats;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 *
 * @author penalva
 */
public class DataReader {

    protected static Double truncate(double a,int dec){


    NumberFormat df  = NumberFormat.getInstance(Locale.US);
    df.setGroupingUsed(false);
    df.setMaximumFractionDigits(dec);

    return Double.valueOf(df.format(a));

    }

    protected static String renderTitle(String filetype,String filename){

    if(filetype.equalsIgnoreCase("ADVFN CSV")){

    filename = filename.split("-")[filename.split("-").length-1];

    return filename.substring(0, filename.indexOf("_"));}
    else
    if( filetype.equalsIgnoreCase("D,T,V,O,C,MIN,MAX") ){

    return filename;

    }else
    return "selected file type does not exist";

    }

    public static void scrapFile(Data datax,Data datay, String path){

    try{

    Writer write = new FileWriter(path);

    for(int index = 0; index < datax.getLength() ; index++){ write.write("\t\t"+ String.valueOf( datax.get(index).getDatum() ) + "\t\t" + String.valueOf( datay.get(index).getDatum() )+ "\n" );}

    write.close();

    }catch(IOException e){ System.err.println(e);}

    }

    protected static boolean verifyLength(Data data1,Data data2){

    if( data1.getLength() != data2.getLength())

    return false;

    else return true;

    }

    protected static Data[] portfolio(String path,Data[] data)throws java.io.IOException{

    Data[] returnedportfolio;
    ArrayList<String> portfolio = new ArrayList<String>();
    Scanner reader = new Scanner(new File(path));
    ArrayList<Integer> marktoremove = new ArrayList<Integer>();
    int iterator = 0;

    while(reader.hasNext()) portfolio.add(reader.nextLine());

    for(int i = 0; i < data.length; i++) if( !portfolio.contains(data[i].getTitle()) ) marktoremove.add(i);

    for(int j = 0; j < marktoremove.size(); j++) data[marktoremove.get(j)].erase();

    returnedportfolio = new Data[data.length - marktoremove.size()];

    for(int j = 0; j < data.length; j++) if( !data[j].isEmpty() ){ returnedportfolio[iterator] = data[j]; iterator++;}

    return returnedportfolio;
    }

    protected static void readTxt(File file,Data data,String column) throws java.io.IOException{

    Scanner reader = new Scanner(new FileReader(file));
    int cont = 0,line = 1;
    reader.reset();
    reader.useDelimiter("\t\t");
    if(column.equalsIgnoreCase("second"))
    while(reader.hasNext()){
    if( (cont+1)%2 == 0){ data.addValue(Double.valueOf( reader.nextLine() ), ""); cont++;}else{ reader.next(); cont++;} }
    else if(column.equalsIgnoreCase("first")){data.addValue(Double.valueOf( reader.next() ), ""); reader.nextLine(); cont = 3; while(reader.hasNext()){
    if( (cont+1)%(2*line + 1) == 0){ data.addValue(Double.valueOf( reader.next() ),""); cont++;}
    else{ reader.nextLine(); cont++; line++;} } }

    reader.close();

    }

    public static void scrapFile(Data data,String path){
        try{

            BufferedWriter buffer_writer = new BufferedWriter(new FileWriter(new File(path)));

            String text = "start"+data.getLength()+"...\n";

            int text_lenght = String.valueOf(text).length();
            
            buffer_writer.write(text, 0, text_lenght);

           for(int index = 0; index <data.getLength() ; index++){

               text = data.get(index).getDate().getDate()+";"+data.get(index).getName()+";"+"0000"+";"+data.get(index).getDatum()+";"+data.get(index).getVol()+";"+data.get(index).getClock().getClock()+";"+"...\n";

               text_lenght = String.valueOf(text).length();

               buffer_writer.write(text, 0, text_lenght);

           }
           
           buffer_writer.close();

        }

catch(IOException e){ System.err.println(e);}

}

    protected static void readFile(File file,Data data,String filetype,String normal_type) throws java.io.IOException{

    try{  
   
        Scanner reader = new Scanner(new FileReader(file));

        data.setTitle(file.getName().substring(11, 17));

        reader.useDelimiter(";");

        reader.nextLine();
                
    while ( reader.hasNext() ){
        
        Date date = new Date(reader.next(),filetype);

        String Name = reader.next();

        reader.next();

        Double datum = Double.parseDouble(reader.next());
        
        Long volume = Long.parseLong(reader.next());

        Clock clock = new Clock(reader.next(),filetype);
        
        data.addValue(date, Name, datum, volume, clock);

        data.get(data.getLength() - 1).getClock().normalize(data.get(0).getClock().getComponent("hour"), normal_type);

        reader.nextLine();

    }

    reader.close();

    }catch(FileNotFoundException e){

    System.err.print(e);

    }

    }/*

    protected static Data readFile(File file,int column,String dataintervalini,String dataintervalend,String filetype) throws java.io.IOException{

    Data data = new Data();

    try{
    Scanner reader = new Scanner(file);

    data.setTitle( findTitle(file,filetype) );

    reader.reset();
    String dataend = "";
    int cont = 1,line = 0;

    if( filetype.equalsIgnoreCase("ADVFN CSV") ){

    reader.useDelimiter(",");

    reader.findWithinHorizon(dataintervalini, 2147483647);
    data.addDes(dataintervalini);


    while ( reader.hasNext() && !dataend.equals(dataintervalend) ){

    if(column == 6){

    if( (cont+1)%6 == 0) { data.addValue( Double.valueOf( reader.nextLine() ) ); }
    else { dataend = String.valueOf(reader.next()); if( dataend.contains("/")) data.addDes(dataend);}

    }
    else{

    if( (cont+1)%(column + 6*line) == 0 ) { data.addValue( Double.valueOf( reader.next() ) ); }
    else{

    if( (cont+1)%6 == 0) { reader.nextLine(); line++; }
    else { dataend = String.valueOf(reader.next()); if( dataend.contains("/") ) data.addDes(dataend);}

    }

    }
    cont++;
    }

    reader.close();

    }else{

    reader.useDelimiter("\t");

    reader.findWithinHorizon(dataintervalini, 2147483647);
    data.addDes(dataintervalini);

    while ( reader.hasNext() && !dataend.equals(dataintervalend) ){

    if(column == 7){

    if( (cont+1)%7 == 0) { data.addValue( Double.valueOf( reader.nextLine() ) ); }
    else { dataend = String.valueOf(reader.next()); if( dataend.contains("/")) data.addDes(dataend); if( dataend.contains(":")) data.setDes(data.getLengthDes()-1,data.getDes(data.getLengthDes()-1)+dataend); }

    }
    else{

    if( (cont+1)%(column + 7*line) == 0 ) { data.addValue( Double.valueOf( reader.next() ) ); }
    else{

    if( (cont+1)%7 == 0) { reader.nextLine(); line++; }
    else { dataend = String.valueOf(reader.next()); if( dataend.contains("/") ) data.addDes(dataend); if( dataend.contains(":") ) data.setDes(data.getLengthDes()-1, data.getDes(data.getLengthDes()-1)+dataend ); }

    }

    }
    cont++;
    }

    reader.close();

    }

    }catch(FileNotFoundException e){

    System.err.print(e);

    }

    return data;

    }

    protected static void readFile(File file,Data data,int column,String filetype) throws java.io.IOException{

    try{
    Scanner reader = new Scanner(file);
    Long vol = 0L;

    data.setTitle( findTitle(file,filetype) );

    reader.reset();
    String dataend = "";
    int cont = 1,line = 0;

    if( filetype.equalsIgnoreCase("ADVFN CSV") ){

    reader.useDelimiter(",");

    while ( reader.hasNext() ){

    if(column == 6){

    if( (cont+1)%6 == 0) {vol = Long.valueOf( reader.nextLine().split(",")[1] ); data.addValue( Double.valueOf( String.valueOf(vol) ) ); data.addVol(vol); }
    else { dataend = String.valueOf(reader.next()); if( dataend.contains("/")) data.addDes(dataend); }

    }
    else{

    if( (cont+1)%(column + 6*line) == 0 ) { data.addValue( Double.valueOf( reader.next() ) ); }
    else{

    if( (cont+1)%6 == 0) { data.addVol( Long.valueOf( reader.nextLine().split(",")[1] ) ); line++; }
    else { dataend = String.valueOf(reader.next()); if( dataend.contains("/") ) data.addDes(dataend);}

    }

    }
    cont++;
    }

    reader.close();

    }else{

    reader.useDelimiter("\t");

    while ( reader.hasNext() ){

    if(column == 7){

    if( (cont+1)%7 == 0) { vol = Long.valueOf( reader.nextLine().split(",")[1] ); data.addValue( Double.valueOf( String.valueOf(vol) ) ); data.addVol(vol); }
    else { dataend = String.valueOf(reader.next()); if( dataend.contains("/")) data.addDes(dataend); if( dataend.contains(":")) data.setDes(data.getLengthDes()-1,data.getDes(data.getLengthDes()-1)+dataend); }

    }
    else{

    if( (cont+1)%(column + 7*line) == 0 ) { data.addValue( Double.valueOf( reader.next() ) ); }
    else{

    if( (cont+1)%7 == 0) { data.addVol( Long.valueOf( reader.nextLine().split(",")[1] ) ); line++; }
    else { dataend = String.valueOf(reader.next()); if( dataend.contains("/") ) data.addDes(dataend); if( dataend.contains(":") ) data.setDes(data.getLengthDes()-1, data.getDes(data.getLengthDes()-1)+dataend ); }

    }

    }
    cont++;
    }

    reader.close();

    }

    }catch(FileNotFoundException e){

    System.err.print(e);

    }

    }

    protected static String findTitle(File data,String filetype) throws java.io.IOException{

    String path = data.getPath();

    path = path.split("/")[path.split("/").length-1];

    return renderTitle(filetype,path);

    }

    protected static void fillZero(Data data1,Data data2) throws java.io.IOException{

    for(int i = data1.getLength() - 1 ; i >= 0; i--)
    if( data2.containsDes( data1.getDes(i) ) == false ){ data2.addValue(0.0); data2.addDes( data2.getLength()-1 , data1.getDes(i) );}

    for(int i = data2.getLength() - 1 ; i >= 0; i--)
    if( data1.containsDes( data2.getDes(i) ) == false ){ data1.addValue(0.0); data1.addDes( data1.getLength()-1 , data2.getDes(i) );}

    }

    protected static void deleteIliquid(File[] files,String filestimescale,int tolerance) throws java.io.IOException{

    Data[] temp = new Data[files.length];
    boolean del = false;
    Writer[] writer = new Writer[files.length];
    Scanner[] reader = new Scanner[files.length];
    String number;
    int count;

    if( filestimescale.equals("minutes") )

    for(int i = 0; i < files.length ; i++){

    temp[i] = new Data();
    writer[i] = new FileWriter("/home/penalva/Dropbox/Public/Data/temp"+i+".txt");

    readFile(files[i],temp[i],5,"ADVFN CSV");
    temp[i].returns("simple");

    for ( int j = 0 ; j < temp[i].getLength() ; j++) writer[i].write(  temp[i].getValue(j)  + "," );

    writer[i].close();

    reader[i] = new Scanner(new File("/home/penalva/Dropbox/Public/Data/temp"+i+".txt") );

    reader[i].reset();

    reader[i].useDelimiter(",");

    while(reader[i].hasNext()){

    number = reader[i].findWithinHorizon("0.0",21474836);
    if (number.equals(null)) number = "1";

    if( number.equals( "0.0" ) ){

    count = 0;

    while( number.equals( "0.0" ) && reader[i].hasNext()){

    count++;
    number = reader[i].next();

    }

    if(count >= tolerance-1) System.out.println(files[i].delete());

    }

    }

    }

    }*/

}