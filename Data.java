package BaseStats;

import java.util.ArrayList;
import java.util.Collections;

public class Data{

    private ArrayList TIME_SERIES;
    private String DESCRIPTION = "",TITLE = "",SOURCE;
    private int CALENDAR = 0,ASSETS = 0;


    public Data(){

        this.TIME_SERIES = new ArrayList();
        this.SOURCE = "bovespa";

              }

    public String getSource(){

        return this.SOURCE;

    }

    public int getIndex(double datum){

        DataUnity datum1 = new DataUnity(datum,"");

        return this.TIME_SERIES.indexOf(datum1);

    }

    public void setNumberofDays(){

        Data copydata = new Data();
        
        copydata.copy(this);

        Data.heapSortDate(copydata);

        Date old_date  = new Date(copydata.get(0).getDate().getDate(),copydata.get(0).getDate().getSource());

        this.CALENDAR++;


        for(int i = 1; i < copydata.getLength(); i++) if( !copydata.get(i).getDate().isEqual(old_date) ){ old_date = new Date(copydata.get(i).getDate().getDate(),copydata.get(i).getDate().getSource()); this.CALENDAR++;}


    }

    public void setNumberofAssets(){

        Data copydata = new Data();

        copydata.copy(this);

        Data.heapSortName(copydata);

        String old_date  = copydata.get(0).getName();

        this.ASSETS++;

        for(int i = 1; i < copydata.getLength(); i++) if( !copydata.get(i).getName().equalsIgnoreCase(old_date) ){ old_date = copydata.get(i).getName(); this.ASSETS++;}

    }

    protected void excludeNoTrade(){

        for(int i = 0 ; i < this.getLength() ; i++) if(this.get(i).getVol() == 0 )  this.removePoint(i);
            
    }

    protected Data[] dataByDay(int calendar_lenght){

        
        Data[] DATA_BY_DAY = new Data[calendar_lenght];
        Date[] dates = new Date[calendar_lenght];
        
        int counter = 0;
        int counter1 = 0;

        Data.heapSortDate(this);
            
        do{

            dates[counter1] = new Date(this.get(counter).getDate().getDate(),this.getSource());
        
            DATA_BY_DAY[counter1] = new Data();
        
            while( this.get(counter).getDate().isEqual(dates[counter1]) ){
        
                DATA_BY_DAY[counter1].addValue(dates[counter1],this.get(counter).getName(),this.get(counter).getDatum(),this.get(counter).getVol(),this.get(counter).getClock() );
                counter++;
        
                if( counter >= this.getLength() ) break;
            }
            counter1++;

        }
        while(counter1 < calendar_lenght);
                   
        return DATA_BY_DAY;
        
    }

    protected Data[] dataByName(int names_number){

        String PREVIOUS_NAME;
        Data[] DATA_BY_DAY = new Data[names_number];
        
        int counter = 0;
        int counter1 = 0;

        Data.heapSortName(this);

        do{
            PREVIOUS_NAME = this.get(counter).getName();
            DATA_BY_DAY[counter1] = new Data();

            while( this.get(counter).getName().equalsIgnoreCase(PREVIOUS_NAME) ){

                DATA_BY_DAY[counter1].addValue(this.get(counter).getDate(),PREVIOUS_NAME,this.get(counter).getDatum(),this.get(counter).getVol(),this.get(counter).getClock());
                counter++;

                if( counter >= this.getLength() ) break;
            }
            counter1++;

        }
        while(counter1 < names_number);

        return DATA_BY_DAY;

    }

    protected int getNumberDays(){

        return this.CALENDAR;

    }

    protected int getNumberAssets(){

        return this.ASSETS;

    }

    protected void scalarMult(double val){

        for (int i = 0 ; i < this.getLength(); i++) this.get(i).setDatum(this.get(i).getDatum()*val);

    }

    protected static double mean(Data data){

        return Data.sum(data,0,data.getLength() - 1)/data.getLength();

    }  

    protected void firstDiff(){

            for(int i = 0 ; i < this.getLength() - 1 ; i++){

            this.get(i).getClock().setToInterval(this.get(i).getClock().getNormalClock()+"_"+this.get(i+1).getClock().getNormalClock() );
            this.get(i).setDatum(this.get(i+1).getDatum() - this.get(i).getDatum());
            this.get(i).setVol(this.get(i+1).getVol());

            }

            this.removePoint(this.getLength()-1);

    }

    protected Data localAverage(int localparameter){

        Data localaverage = new Data();
        Double partial;
        Double normal;

        for(int i = 0; i < this.getLength(); i++){
        
                partial = 0.0;
                normal = 0.0;

                for(int j = - localparameter; j <= localparameter; j++){

                    if( (j + i) < this.getLength() && (i + j) >= 0 ){
                    partial += this.get(i + j).getDatum(); normal++;}
                    else partial += 0.0;

                }
                
                localaverage.addValue(this.get(i).getDate(),this.get(i).getName(),partial/normal,this.get(i).getVol(),this.get(i).getClock());

        }
            
        return localaverage;
    }
     
    protected void copy(Data copy){

        for(int i = 0; i < copy.getLength() ; i++){

            this.addValue( copy.get(i).getName() );
            this.get(i).copy( copy.get(i) );

        }

        this.DESCRIPTION = copy.DESCRIPTION;
        this.SOURCE = copy.SOURCE;
        this.TITLE = copy.TITLE;
        
    }

    protected void erase(){

        this.TIME_SERIES.clear();
        this.SOURCE = "";
        this.DESCRIPTION = "";
        this.TITLE = "";

    }

    protected boolean containValues(Object o){

        return this.TIME_SERIES.contains(o);

    }
    
    protected void setTitle(String title){

        this.TITLE = title;

    }

    protected String getTitle(){
        
        return this.TITLE;

    }
    
    private void setReturn(String type){

        this.DESCRIPTION += " and is in "+type+" returns form ";

    }

    private void setDetrended(){

        this.DESCRIPTION += " and is detrend ";

    }

    protected String getDescription(){

        return this.DESCRIPTION;

    }

    protected void setDescription(String description){

        this.DESCRIPTION = description;

    }

    protected void removePoint(int index){

        this.TIME_SERIES.remove(index);
        
    }

    protected void detrend(){
        

                for(int index = 0 ; index < this.getLength() ; index++){

                    this.get(index).setDatum(this.get(index).getDatum()-Data.mean(this));

                }

                this.setDetrended();
   
    }

    protected void returns(String scale,boolean filter,double filter_value){

        if(scale.equals("log-price")){

            for(int index = 0 ;index < this.getLength() ; index++){
                
                this.get(index).setDatum((Double) Math.log( this.get(index).getDatum() ));
            
            }

            this.setReturn(scale);
                                    
            }else if(scale.equals("log-price-difference")){

            this.returns("log-price",false, 0.0);

            if(filter == true){ArrayList<Integer> mark = new ArrayList<Integer>();
                for(int i = 0 ; i < this.getLength() - 1 ; i++){
                    
                    if( Math.abs( this.get(i+1).getDatum()-this.get(i).getDatum() ) > filter_value*Math.exp(this.get(i+1).getDatum()) ) mark.add(i);
                    
                }
            
                this.firstDiff();
            
                for(int j = 0 ; j < this.getLength() - 1 ; j++){
                                                      
                    if( mark.contains(j) ) this.removePoint(j);
                    
                }
            
            
            
            } else this.firstDiff();

            this.setReturn("difference");

        }

        

    }

    protected static void printData(Data data){

        for (int i = 0; i < data.getLength() ; i++) System.out.print(" Time: "+data.get(i).getClock().getClock()+" Value: "+data.get(i).getDatum()+" Volume: "+data.get(i).getVol()+" Date: "+data.get(i).getDate().getDate()+" Name: "+data.get(i).getName()+"\n");


    }

    protected static double gaussianShape(Double data,Double point,Double bin){

        double diff = (data-point)*1.0/bin;
        double sqrtnorm = 1.0/Math.sqrt(2.0*Math.PI*bin*bin);
        double shape = Math.exp(- (diff*diff)*1.0/2.0)*sqrtnorm;

        return shape;
        
    }
        
    protected boolean containsDescription(String description){

        return this.DESCRIPTION.contains(description);

    }

    protected void addValue(String name){

        this.TIME_SERIES.add(new DataUnity(name) );
        
    }

    protected void addValue(Double datum,String name){

        this.TIME_SERIES.add(new DataUnity(datum,name) );
        
    }

    protected void addValue(Long volume,String name){

        this.TIME_SERIES.add(new DataUnity(volume,name) );

    }

    protected void addValue(Double datum,Long volume,String name){

        this.TIME_SERIES.add(new DataUnity(datum,volume,name) );

    }

    protected void addValue(Date date,String name,Double datum,Long volume,Clock clock){

        this.TIME_SERIES.add( new DataUnity(date,name,datum,volume,clock) );

    }

    protected void addValue(int index,Date date,String name,Double datum,Long volume,Clock clock){

        this.TIME_SERIES.add(index , new DataUnity(date,name,datum,volume,clock) );

    }

    protected void setValue(int index,DataUnity a){

        this.TIME_SERIES.set(index, a);

    }

    protected DataUnity get(int index){

        DataUnity instance = (DataUnity) this.TIME_SERIES.get(index);

        return instance;
                
    }

    protected int getLength(){

        return this.TIME_SERIES.size();

    }
    
    protected boolean isEmpty(){

        return this.TIME_SERIES.isEmpty();

    }
    
    protected static Double sum(Data x, int ini,int end ){


        try{

        if ( ini == end ) {

             return x.get(ini).getDatum();
     
        }
        else{
         
             return x.get(ini).getDatum()+sum(x,ini+1, end);

        }

        }catch(StackOverflowError e){Double sum =  0.0;
        for(int i  = 0 ; i <= end; i++) sum += x.get(i).getDatum();
        return sum; 
        }


        
    }

    public static double sumSubvec(ArrayList<Data> x,int subvec, int ini,int end ){

    if ( ini == end ) {
    
    return x.get(subvec).get(ini-1).getDatum();

    }
    else{
    
    return x.get(subvec).get(ini-1).getDatum()+sumSubvec(x,subvec,ini+1, end);

    }

    }

    protected static void heapSortDate(Data v){

         buildMaxHeapDate(v);

         int n = v.getLength();

         for (int i = v.getLength() - 1; i > 0; i--)
         {
            swap(v, i , 0);
            maxHeapifyDate(v, 0, --n);
         }

       }

    private static void buildMaxHeapDate(Data v)
       {
          for (int i = v.getLength()/2 - 1; i >= 0; i--)
             maxHeapifyDate(v, i , v.getLength() );
       }

    private static void maxHeapifyDate(Data v, int pos, int n)
       {
          int max = 2 * pos + 1, right = max + 1;

          if (max < n)
          {
             if ( right < n && v.get(right).getDate().isGreater(v.get(max).getDate()) )
                max = right;

             if ( v.get(max).getDate().isGreater(v.get(pos).getDate()) )
             {

                 swap(v, max, pos);
                 maxHeapifyDate(v, max, n);

             }

          }
       }

    protected static void heapSortName(Data v){

         buildMaxHeapName(v);

         int n = v.getLength();

         for (int i = v.getLength() - 1; i > 0; i--)
         {
            swap(v, i , 0);
            maxHeapifyName(v, 0, --n);
         }

       }

    private static void buildMaxHeapName(Data v)
       {
          for (int i = v.getLength()/2 - 1; i >= 0; i--)
             maxHeapifyName(v, i , v.getLength() );
       }

    private static void maxHeapifyName(Data v, int pos, int n)
       {
          int max = 2 * pos + 1, right = max + 1;

          if (max < n)
          {
             if ( right < n && v.get(right).getNameIndex() > v.get(max).getNameIndex() )
                max = right;

             if ( v.get(max).getNameIndex() > v.get(pos).getNameIndex() )
             {

                 swap(v, max, pos);
                 maxHeapifyName(v, max, n);

             }

          }
       }

    protected static void heapSortNClock(Data v){

         buildMaxHeapNClock(v);

         int n = v.getLength();

         for (int i = v.getLength() - 1; i > 0; i--)
         {
            swap(v, i , 0);
            maxHeapifyNClock(v, 0, --n);
         }

       }

    private static void buildMaxHeapNClock(Data v)
       {
          for (int i = v.getLength()/2 - 1; i >= 0; i--)
             maxHeapifyNClock(v, i , v.getLength() );
       }

    private static void maxHeapifyNClock(Data v, int pos, int n)
       {
          int max = 2 * pos + 1, right = max + 1;

          if (max < n)
          {
             if ( right < n && v.get(right).getClock().getNormalClock() > v.get(max).getClock().getNormalClock() )
                max = right;

             if ( v.get(max).getClock().getNormalClock() > v.get(pos).getClock().getNormalClock()  )
             {

                 swap(v, max, pos);
                 maxHeapifyNClock(v, max, n);

             }

          }
       }

    protected static void heapSortVol(Data v){

         buildMaxHeapVol(v);

         int n = v.getLength();

         for (int i = v.getLength() - 1; i > 0; i--)
         {
            swap(v, i , 0);
            maxHeapifyVol(v, 0, --n);
         }

       }

    private static void buildMaxHeapVol(Data v)
       {
          for (int i = v.getLength()/2 - 1; i >= 0; i--)
             maxHeapifyVol(v, i , v.getLength() );
       }

    private static void maxHeapifyVol(Data v, int pos, int n)
       {
          int max = 2 * pos + 1, right = max + 1;

          if (max < n)
          {
             if ( right < n && v.get(right).getVol() > v.get(max).getVol() )
                max = right;

             if ( v.get(max).getVol() > v.get(pos).getVol()  )
             {

                 swap(v, max, pos);
                 maxHeapifyVol(v, max, n);

             }

          }
       }

    protected static void heapSortDatum(Data v){

         buildMaxHeapDatum(v);

         int n = v.getLength();

         for (int i = v.getLength() - 1; i > 0; i--)
         {
            swap(v, i , 0);
            maxHeapifyDatum(v, 0, --n);
         }

       }

    private static void buildMaxHeapDatum(Data v)
       {
          for (int i = v.getLength()/2 - 1; i >= 0; i--)
             maxHeapifyDatum(v, i , v.getLength() );
       }

    private static void maxHeapifyDatum(Data v, int pos, int n)
       {
          int max = 2 * pos + 1, right = max + 1;

          if (max < n)
          {
             if ( right < n && v.get(right).getDatum() > v.get(max).getDatum() )
                max = right;

             if ( v.get(max).getDatum() > v.get(pos).getDatum()  )
             {

                 swap(v, max, pos);
                 maxHeapifyDatum(v, max, n);

             }

          }
       }

    protected static void swap (Data v, int j, int aposJ ){

          DataUnity aux = new DataUnity( v.get(aposJ).getName() );
          
          aux.copy(v.get(aposJ));
          v.setValue(aposJ , v.get(j) );
          v.setValue(j, aux);

       }
        
    protected static void heapSortClock(Data v){

         buildMaxHeapClock(v);

         int n = v.getLength();

         for (int i = v.getLength() - 1; i > 0; i--)
         {
            swap(v, i , 0);
            maxHeapifyClock(v, 0, --n);
         }

       }

    private static void buildMaxHeapClock(Data v)
       {
          for (int i = v.getLength()/2 - 1; i >= 0; i--)
             maxHeapifyClock(v, i , v.getLength() );
       }

    private static void maxHeapifyClock(Data v, int pos, int n)
       {
          int max = 2 * pos + 1, right = max + 1;

          if (max < n)
          {
             if ( right < n && v.get(right).getClock().isGreater(v.get(max).getClock())  )
                max = right;

             if ( v.get(max).getClock().isGreater(v.get(pos).getClock()) )
             {

                 swap(v, max, pos);
                 maxHeapifyClock(v, max, n);

             }

          }
       }

    protected static Data indexMean(Data[] data_array){

        Data mean = new Data();

        for(int i = 0 ; i < data_array[0].getLength(); i++)
        {
           mean.addValue(0.0,"");
           for(int j = 0 ; j < data_array.length ; j++)
        mean.get(i).setDatum( mean.get(i).getDatum() + (data_array[j].get(i).getDatum()/data_array.length) );

        }

        return mean;
    }


    protected int containsNTime(double ntime){

        DataUnity datum = new DataUnity("");
        datum.getClock().setNormalT(ntime);
        int index = Collections.binarySearch(this.TIME_SERIES, datum);

        return index;

    }

    protected int lastTick(double normal_time){

        int index = this.containsNTime(normal_time);

        if( index >= 0 ) return index;
        else return - index - 2;
        
    }



    protected void sampler(int frequency){
        
        Data copydata = new Data();
        int j = 0;

        for(int i = 0; i < this.getLength(); i += frequency){

            copydata.addValue(this.getTitle());
            copydata.get(j).copy(this.get(i));
            j++;
            
        }

        this.erase();

        this.copy(copydata);

    }

}




















