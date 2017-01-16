package BaseStats;

import java.io.*;
import java.util.Scanner;
import com.panayotis.gnuplot.*;
import com.panayotis.gnuplot.dataset.*;
import com.panayotis.gnuplot.plot.*;
import com.panayotis.gnuplot.style.*;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.terminal.*;
import java.text.*;
import java.util.Locale;

import javax.imageio.ImageIO;

/*fazer normalizador de retornos*/

public class PDFEstimation extends Correlation{


    public static void cumulative(String plot,String dataset,String dataout){
    /* Palavras chaves referentes à plotar o cumulativo*/
        if(plot.equalsIgnoreCase("plot") || plot.startsWith("p") || plot.startsWith("P") ){


        if(dataset.equalsIgnoreCase("Gaussian") || dataset.equalsIgnoreCase("Normal")){

        try{

        Writer writer = new FileWriter(dataout);

        for(double i = -0.3; i<=0.3; i += 0.0001){

        writer.write(String.valueOf(i)+"\t\t"+String.valueOf(ErrorFunction.Phi(i,0.001,0.0))+"\n");

        }

        writer.close();
        plot2D(new File(dataout));



        }catch(Exception e){

        System.err.println(java.util.ResourceBundle.getBundle("BaseMath/Bundle").getString("Output_file_does_not_exist."));

        }

        }else{

        try{

        Scanner reader = new Scanner(new File(dataset));
        Data data = new Data();

        Writer writer = new FileWriter("/home/penalva/Desktop/Algoritmos/cumulout.csv");

        while (reader.hasNext()){

        data.addValue( Double.parseDouble( reader.nextLine() ) , "" );

        }

        reader.close();
        Data.heapSortDatum(data);  /* ordenamento árvore binária no módulo básico BaseMath>Data.java*/

        for (int i = 0; i < data.getLength(); i++) {

            writer.write(String.valueOf(data.get(i).getDatum()) + "\t\t" + String.valueOf((double) i / data.getLength()) + "\n");

        }

        writer.close();
        plot2D(new File(dataout));

    }
    catch(Exception e){

                System.err.println(e);

            }
    }

    }else{

        System.out.println("The syntax is: plot or p");

    }

}

    public static void cumulative(String dataset,String dataout){

        if(dataset.equalsIgnoreCase("Gaussian") || dataset.equalsIgnoreCase( "Normal")){

            try{

            Writer writer = new FileWriter(dataout);

            for(double i = -5.0; i<=5.0; i += 0.0001){

                writer.write(String.valueOf(i)+"\t\t"+String.valueOf(ErrorFunction.Phi(i,0.0001,0.0))+"\n");

            }

            writer.close();

            }catch(Exception e){

                System.err.println( "Output file does not exist.");

            }

            }else{

            try{

                Scanner reader = new Scanner(new File(dataset));
                Data data= new Data();

                Writer writer= new FileWriter(dataout);

                while(reader.hasNext()){

                    data.addValue( Double.parseDouble( reader.nextLine() ) ,"" );

                }

                reader.close();
                Data.heapSortDatum(data);/*heapsort no arquivo Data.java*/

                for(int i = 0; i<data.getLength(); i++){

                        writer.write( String.valueOf( data.get(i).getDatum() )+"\t\t"+String.valueOf( (double) i/data.getLength() )+"\n" );

                }

            writer.close();


            }catch(Exception e){

                System.err.println(e);

            }

            }
    }


    public static double histogram(Data data,double point,double bin){

        double histodata = 0.0;

        for(int index = 0 ; index < data.getLength() ; index++){
            if ( - 0.5 <= (data.get(index).getDatum() - point)/bin  && (data.get(index).getDatum() - point)/bin <= 0.5)
            histodata += 1/(data.getLength()*bin);

        }

    return histodata;

    }

public static Data unfolding(Data original,String Path) throws java.io.FileNotFoundException{

        try{
        Data pdf = new Data();
        Data probability= new Data();
        Data UNFOLDED_SPECTRA= new Data();
        Double sum= 0.0;
        int norm = 0;
        DecimalFormat df= new DecimalFormat();

        DataReader.readTxt(new File(Path), pdf,"first");
        DataReader.readTxt(new File(Path), probability,"second");



        for(int i = 0; i <original.getLength() ; i++){

            sum = 0.0;

            for(Double density = 0.0 ; density <=original.get(i).getDatum(); density +=0.001){

                sum += probability.get( pdf.getIndex(DataReader.truncate(density,3)) ).getDatum()*0.001;
            }

        }

        UNFOLDED_SPECTRA.addValue(sum, "unfolded");

        return UNFOLDED_SPECTRA;

        }catch(java.io.IOException e){

        }

        return null;
    }

public static double

kernelEstimation(Data data,double point,double bin,String scale){

    Data gaussdata = new Data();
    for(int index = 0; index < data.getLength(); index++)

    if( scale.equals("renormalize") ){

           gaussdata.addValue( gaussianShape(data.get(index).getDatum(), point, bin) ,"");
           gaussdata.setValue(index, new DataUnity( bin*gaussdata.get(index).getDatum() , "") );

    }else{

            gaussdata.addValue( gaussianShape(data.get(index).getDatum(), point, bin ), "" );

    }

    double weight = 1.0/gaussdata.getLength();

    return sum(gaussdata,0,gaussdata.getLength() - 1)*weight;

 }

public static void

pdfPlot(Data data,double bin,String scale,double xlower,double xupper,int precision,double interat,double plotxlowerbound,double plotxupperbound,String name,String imagename){

        try{

            Data estimate = new Data();
            Data xaxis = new Data();


        for(double i = xlower; i <= xupper; i += interat){
            estimate.addValue( kernelEstimation(data,i,bin,scale),"" ); xaxis.addValue(i,"");
        }

        scrapFile(xaxis,estimate,"/home/penalva/Dropbox/Public/Data/stdplot/"+name,precision);

        Plot pdfplot = new Plot("/home/penalva/Dropbox/Public/Data/stdplot/"+name,plotxlowerbound,plotxupperbound,"Daily Returns Covariance","Marginal Eigenvalue Distribution","",13,"POINTS",false,NamedPlotColor.BROWN);
//"sqrt( 4*x*0.383908435 - (x + 0.383908435 - 1)**2 )/(2*pi*x*0.383908435)"
        pdfplot.plot();

        pdfplot.save("pdfestimation");

        }

catch(Exception e){}

    }

    public static void pdfPlot(Data data,double bin,double xlower,double xupper,int precision,double plotxlowerbound,double plotxupperbound,String name,String imagename){
        Data estimate = new Data();
        Data xaxis = new Data();

        for(double i = xlower; i <= xupper; i += bin){
            estimate.addValue( histogram(data,i,bin) , "" ); xaxis.addValue(i,"");
        }

        scrapFile(xaxis,estimate,"/home/penalva/Dropbox/Public/Data/stdplot/"+name,precision);
        plot(new File("/home/penalva/Dropbox/Public/Data/stdplot/"+name),plotxlowerbound,plotxupperbound,imagename,"STEPS");

    }

    private static void plot2D(File data){

        try{

        Graph graf = new Graph();
        FileDataSet set = new FileDataSet(data);
        FileDataSet set0 = new FileDataSet(new File("/home/penalva/Desktop/Algoritmos/cumulout.csv"));
        JavaPlot plotter = new JavaPlot();
        plotter.setGNUPlotPath("/usr/bin/gnuplot");
        DataSetPlot plot = new DataSetPlot();
        DataSetPlot plot0 = new DataSetPlot();
        plot.setDataSet(set);
        plot0.setDataSet(set0);
        graf.add(plot);
        graf.add(plot0);
        plotter.addGraph(graf);
        plotter.plot();

        }catch(Exception e){

            System.err.println("Gnuplot_not_found.");

        }

}

    protected static void scrapFile(Data datax,Data datay, String path,int dec){

        try{
        NumberFormat df  = NumberFormat.getInstance(Locale.US);
        df.setGroupingUsed(false);
        df.setMaximumFractionDigits(dec);

        Writer write = new FileWriter(path);
        for(int index = 0; index < datax.getLength() ; index++)

        write.write( "\t\t"+df.format(datax.get(index).getDatum() )+"\t\t"+String.valueOf( datay.get(index).getDatum() )+"\n" );
        write.close();

        }catch(IOException e){ System.err.println(e);}

    }
    

    private static void plot(File path,double plotxlowerbound,double plotxupperbound,String imagefile,String style){

        try{

            Graph grafico1 = new Graph();  /**   Gerador de referenciais gráficos, cada objeto **/
                                           /** Graphs instancia um conjunto de eixos.           **/

            grafico1.getAxis("x").setBoundaries(plotxlowerbound,plotxupperbound);
            FileDataSet array = new FileDataSet(path);

                                                             /**   Objetos FileDataSet instanciam dados originados **/
                                                              /** de um objeto tipo File(File instancia o arquivo   **/
                                                            /** externamente), permitindo o uso de dados externos.**/

            JavaPlot plotter = new JavaPlot();              /**    Instância responsavel por passar comandos ao gnuplot **/

            ImageTerminal image = new ImageTerminal();

            plotter.setTerminal(image);

            plotter.setGNUPlotPath( "/usr/bin/gnuplot" );     /** e iniciar o gnuplot.                                    **/

            DataSetPlot plot1 = new DataSetPlot();          /**    Instância de referência à dados para o plot.    **/

            plot1.setDataSet(array);                      /**    Adiciona dados à serem plotados.                **/

            plot1.setPlotStyle(new PlotStyle( Style.valueOf(style) ));

            grafico1.addPlot(plot1);                        /**    Adiciona plots de dados gerais à um frame       **/

            plotter.addGraph(grafico1);                     /**    Adiciona frame gráfico à uma instância de Javaplot **/

            plotter.plot();                                 /**    Método que inicializa o processo de graficar com gnuplot  **/

            ImageIO.write(image.getImage(),"png",new File("/home/penalva/Dropbox/Public/Data/Graphs/"+imagefile));


        }catch(Exception e){
        System.out.println("Gnuplot não encontrado.");
        }
        }

}
