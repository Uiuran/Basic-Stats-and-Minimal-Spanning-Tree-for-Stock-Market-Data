/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BaseStats;

/**
 *
 * @author penalva
 */
public class Correlation extends Data{
    
    private double VAR1 = 0.0,VAR2 = 0.0;
    private double COV = 0.0;
    private double CORR = 0.0;
    private double DISTANCE = 0.0;
    private Integer NORM;


    private double VAR1F = 0.0,VAR2F = 0.0;
    private double COVF = 0.0;
    private double CORRF = 0.0;


    public Correlation(){}
    
    public double distance(){

    return this.DISTANCE = Math.sqrt(2.0*(1 - this.CORR));

    }

    public void correlationPearson(Data data1,Data data2,int delay,int minor_time_step,int scale)  throws java.io.IOException{

    double firsttick = 0.0;
    this.NORM = 0;
    this.CORR = 0.0;
    this.COV = 0.0;
    int index1data1 = 0,index2data1 = 0,index1data2 = 0,index2data2 = 0;

    double minor_normal_step = (double) minor_time_step/(25200.0);
    
    if(minor_time_step == scale){
        
    double normal_step = minor_normal_step;

    if(data1.get(0).getClock().getNormalClock() > data2.get(0).getClock().getNormalClock()){
    firsttick = normal_step + data1.get(0).getClock().getNormalClock();
    index2data1 = data1.lastTick(firsttick);
    index2data2 = data2.lastTick(firsttick);
    index1data2 = data2.lastTick(data1.get(0).getClock().getNormalClock());
    index1data1 = 0;
    }else{
    firsttick = normal_step + data2.get(0).getClock().getNormalClock();
    index2data1 = data1.lastTick(firsttick);
    index2data2 = data2.lastTick(firsttick);
    index1data2 = 0;
    index1data1 = data1.lastTick(data2.get(0).getClock().getNormalClock());
    }
    
    double a = (Math.log(data1.get(index2data1).getDatum()) - Math.log(data1.get(index1data1).getDatum()));
    double b = ( Math.log(data2.get(index2data2).getDatum()) -  Math.log(data2.get(index1data2).getDatum()) );


    if( Math.exp( Math.abs(a) ) < (1.0 + 0.03) || Math.exp(Math.abs(b)) < (1.0 + 0.03)) {


    this.COV += a*b;
    this.VAR1 += a*a;
    this.VAR2 += b*b;
    this.NORM++;
    
    }
    
    for (double i = firsttick + normal_step ; i <= 1.0 ; i += normal_step) {


        index1data1 = index2data1;
        index1data2 = index2data2;
        index2data1 = data1.lastTick(i);
        index2data2 = data2.lastTick(i);

         a = (Math.log(data1.get(index2data1).getDatum()) - Math.log(data1.get(index1data1).getDatum()));
         b = ( Math.log(data2.get(index2data2).getDatum()) -  Math.log(data2.get(index1data2).getDatum()) );

        if( Math.exp( Math.abs(a) ) < (1.0 + 0.03) || Math.exp( Math.abs(b) ) < (1.0 + 0.03)) {

            this.COV += a*b;
            this.VAR1 += a*a;
            this.VAR2 += b*b;
            this.NORM++;

        }

    }

    }else{

    double normal_step = (double) scale/25200.0;

    for(int j = 0; j < normal_step/minor_normal_step ; j++){

    if(data1.get(0).getClock().getNormalClock() > data2.get(0).getClock().getNormalClock()){
    firsttick = normal_step + data1.get(0).getClock().getNormalClock() + j*minor_normal_step;
    index2data1 = data1.lastTick(firsttick);
    index2data2 = data2.lastTick(firsttick);
    index1data2 = data2.lastTick(firsttick - normal_step) ;
    index1data1 = data1.lastTick(firsttick - normal_step);
    }else{
    firsttick = normal_step + data2.get(0).getClock().getNormalClock() + j*minor_normal_step;
    index2data1 = data1.lastTick(firsttick);
    index2data2 = data2.lastTick(firsttick);
    index1data2 = data2.lastTick(firsttick - normal_step);
    index1data1 = data1.lastTick(firsttick - normal_step);
    }

    double a = (Math.log(data1.get(index2data1).getDatum()) - Math.log(data1.get(index1data1).getDatum()));
    double b = ( Math.log(data2.get(index2data2).getDatum()) -  Math.log(data2.get(index1data2).getDatum()) );


    if( Math.exp( Math.abs(a) ) < (1.0 + 0.03) || Math.exp(Math.abs(b)) < (1.0 + 0.03)) {


    this.COV += a*b;
    this.VAR1 += a*a;
    this.VAR2 += b*b;
    this.NORM++;

    }

    for (double i = firsttick + normal_step ; i <= 1.0 ; i += normal_step) {

        index1data1 = index2data1;
        index1data2 = index2data2;
        index2data1 = data1.lastTick(i);
        index2data2 = data2.lastTick(i);

         a = (Math.log(data1.get(index2data1).getDatum()) - Math.log(data1.get(index1data1).getDatum()));
         b = ( Math.log(data2.get(index2data2).getDatum()) -  Math.log(data2.get(index1data2).getDatum()) );

        if( Math.exp( Math.abs(a) ) < (1.0 + 0.03) || Math.exp( Math.abs(b) ) < (1.0 + 0.03)) {

            this.COV += a*b;
            this.VAR1 += a*a;
            this.VAR2 += b*b;
            this.NORM++;

        }

    }

    }

    }
    
    this.COV *= (double) 1.0/this.NORM;

    this.VAR1 *= (double) 1.0/this.NORM;

    this.VAR2 *= (double) 1.0/this.NORM;
   
    this.CORR = this.COV/Math.sqrt(this.VAR1*this.VAR2);
    
    }

    public double getCov(){

        return this.COV;

    }
   

    public double getVar(int slot){

        if(slot == 1)
        return this.VAR1;
        else return this.VAR2;

    }


    public double getCorr(){

        return this.CORR;

    }

    public void fourierE(Data data1,Data data2,int timefreq,String type){

        this.COVF = 0.0;

        for(int i = 1 ; i <= 24600*0.5/timefreq ; i++)
        this.COVF += this.fCoefA(data1, 1.0*i)*this.fCoefA(data2, 1.0*i) + this.fCoefB(data1, 1.0*i)*this.fCoefB(data2, 1.0*i);
              
        this.COVF *= 2.0*Math.PI*Math.PI*timefreq/24600.0;

        if(type.equalsIgnoreCase("correlation")){ this.varfourierE(data1, timefreq); this.varfourierE(data2, timefreq);

        }
                
    }

    private double fCoefA(Data data,double freq){

        double a = 0.0;
        int tick = 0;
        int tick0 = 0;
        for(double i = data.get(0).getClock().getNormalClock(); i <= 2*Math.PI ; i += Math.PI*0.007936508){
            tick = data.lastTick( i + Math.PI*0.007936508 );
            tick0 = data.lastTick(i);
            a += Math.log(data.get(tick).getDatum())*Math.cos(freq*data.get(tick).getClock().getNormalClock()) - Math.log(data.get(tick0).getDatum())*Math.cos(freq*data.get(tick).getClock().getNormalClock());
        }
        a *= 1.0/Math.PI;
        return a;

    }

     private double fCoefB(Data data,double freq){

        double b = 0.0;
        int tick = 0;
        int tick0 = 0;
        for(double i = data.get(0).getClock().getNormalClock(); i <= 2*Math.PI ; i += Math.PI*0.007936508){
            tick = data.lastTick(i + Math.PI*0.007936508 );
            tick0 = data.lastTick(i);
            b += Math.log(data.get(tick).getDatum())*Math.sin(freq*data.get(tick).getClock().getNormalClock()) - Math.log(data.get(tick0).getDatum())*Math.sin(freq*data.get(tick).getClock().getNormalClock());
        }
        b *= 1.0/Math.PI;
        return b;

    }

     public void varfourierE(Data data1,int timefreq){

         Correlation varfourier = new Correlation();
         varfourier.fourierE(data1, data1, timefreq,"variance");
         if( this.VAR1F != 0.0) this.VAR2F = varfourier.getCovf();
         else this.VAR1F = varfourier.getCovf();


    }

     public double getCorrf(){
        
         return this.getCovf()/Math.sqrt(this.getVarf(1)*this.getVarf(2));

     }

     public double getCovf(){

         return this.COVF;

     }

     public double getVarf(int type){

         if(type == 1) return this.VAR1F;
         else return this.VAR2F;

     }
    /* data already demeaned 

}

public Integer getNorm(

){

        return this.norm1;

    }

public double

corr(){

        return this.corr;

    }

protected Double variance(Data data){

       this.var = 0.0;

       for

(int i = data.getLength()-1; i >=

0 ; i--)

{

           this.var += data.getValue(i)*data.getValue(i);

       }

return this.var/(data.getLength()-1);

    }

public void

printCor(){

        System.out.println(this.corr);

    }
*/
}

