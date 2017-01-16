package BaseStats;

import com.panayotis.gnuplot.style.NamedPlotColor;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author penalva
 */
public class Epps extends java.lang.Object{

    public static void eppsPlot(int pointrate,Data asset1,Data asset2,double plotxupperbound,int intervalupper,boolean logx,String type) throws java.io.IOException{

    Data datax = new Data();

    ArrayList<Correlation> epps = new ArrayList<Correlation>();

    if(type.equalsIgnoreCase("Fourier")){

    for(int i = 1 ; i <= intervalupper ; i += 1){

    epps.add(new Correlation());

    datax.addValue(1.0*i, "");

    epps.get(i-1).fourierE(asset1, asset2, i*pointrate,"correlation");

    }

    //ArrayList<HYCovE> mean_epps = new ArrayList<HYCovE>(HYCovE.meanField(epps, intervalupper, neighborhood));

    Epps.scrapFileF(datax, epps, "/home/penalva/Dropbox/Public/Data/stdplot/epps_plot_f" );

    Plot plotter = new Plot("/home/penalva/Dropbox/Public/Data/stdplot/epps_plot_f",1.0,plotxupperbound,asset1.getTitle()+"-"+asset2.getTitle(),"Epps effect","Cross-Correlation",13,"LINES",logx,NamedPlotColor.AQUAMARINE);

    plotter.plot();

    }else{
    
    for(int i = 1 ; i <= intervalupper ; i++){

    epps.add(new Correlation());
    datax.addValue( i*1.0 ,"");
    epps.get(i-1).correlationPearson(asset1,asset2,0,pointrate,i*pointrate);

    }
    
    Epps.scrapFile(datax, epps, "/home/penalva/Dropbox/Public/Data/stdplot/epps_plot_data"+asset1.get(0).getDate().getDate()+asset1.getTitle()+asset2.getTitle() );

        Plot plotter = new Plot("/home/penalva/Dropbox/Public/Data/stdplot/epps_plot_data"+asset1.get(0).getDate().getDate()+asset1.getTitle()+asset2.getTitle() ,1.0,plotxupperbound,asset1.getTitle()+"-"+asset2.getTitle(),"Epps effect","Cross-Correlation",13,"POINTS",logx,NamedPlotColor.ORANGE);

        plotter.plot(); }

    }

    private static void scrapFile(Data datax,ArrayList<Correlation> covariance, String path){

    try{

    Writer write = new FileWriter(path);

    for(int index = 0; index < datax.getLength() ; index++){if(index < covariance.size()) write.write("\t\t"+ String.valueOf( datax.get(index).getDatum() ) + "\t\t" + String.valueOf( covariance.get(index).getCorr() )+ "\n" );}

    write.close();

    }catch(IOException e){ System.err.println(e);}

    }

    private static void scrapFileF(Data datax,ArrayList<Correlation> covariance, String path){

    try{

    Writer write = new FileWriter(path);

    for(int index = 0; index < datax.getLength() ; index++){if(index < covariance.size()) write.write("\t\t"+ String.valueOf( datax.get(index).getDatum() ) + "\t\t" + String.valueOf( covariance.get(index).getCorrf() )+ "\n" );}

    write.close();

    }catch(IOException e){ System.err.println(e);}

    }



}
