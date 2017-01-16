/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BaseStats;

/**
 *
 * @author penalva
 */
public class Clock {

   private String CLOCK_INTERVAL = "0_0";
   private String CLOCK = "00:00:00.0000";
   private Double NORMAL_CLOCK  = 0.0;
   private Integer HOUR = 0;
   private Integer MINUTES = 0;
   private Integer SECONDS = 0;
   private Integer TANDEM = 0;
   private String SOURCE = "";


    public Clock(String clocktime , String source){

        this.CLOCK = clocktime;
        this.SOURCE = source;
        this.setClock();

    }

    public String getSource(){

        return this.SOURCE;

    }

    public Clock(){}

    protected void copy(Clock clock){

        this.CLOCK_INTERVAL =  clock.getInterval();
        this.CLOCK = clock.getClock();
        this.NORMAL_CLOCK = clock.getNormalClock();
        this.MINUTES = clock.getComponent("minutes");
        this.HOUR = clock.getComponent("hour");
        this.SECONDS = clock.getComponent("seconds");
        this.TANDEM = clock.getComponent("tandem");
        this.SOURCE = clock.getSource();

    }

    private void setClock(){
        
        if( this.SOURCE.equalsIgnoreCase("bovespa") ){

        String[] clocks = this.CLOCK.split(":");
        clocks[2] = clocks[2].replace(".", " ");
        String[] tandem = clocks[2].split(" ");
        
        this.HOUR = Integer.valueOf(clocks[0]);
        this.MINUTES = Integer.valueOf(clocks[1]);
        this.SECONDS = Integer.valueOf(tandem[0]);
        this.TANDEM = Integer.valueOf(tandem[1]);

        }
        
    }

    public boolean isGreater(Clock clock){

        if( this.HOUR > clock.getComponent("hour") ) return true;
        else if( this.HOUR == clock.getComponent("hour") ){

            if( this.MINUTES > clock.getComponent("minutes") ) return true;
            else if( this.MINUTES == clock.getComponent("minutes") ){

                if( this.SECONDS > clock.getComponent("seconds") ) return true;
                else if( this.SECONDS == clock.getComponent("seconds") ){

                    if( this.TANDEM > clock.getComponent("tandem") )  return true;
                    return false;

                } else return false;

            } else return false;

        }else return false;

    }

    public boolean isEqual(Clock clock){

        if(this.TANDEM == clock.getComponent("tandem") && this.SECONDS == clock.getComponent("seconds") && this.MINUTES == clock.getComponent("minutes") && this.HOUR == clock.getComponent("hour") ) return true;
        else return false;

    }

    public boolean isGreaterEqual(Clock clock){

        if( this.isGreater( clock ) == true || this.isEqual( clock ) == true ) return true;
        else return false;

    }

    protected void setNormalT(Double normal){

        this.NORMAL_CLOCK = normal;

    }

    protected int getComponent(String clock_type){

        if(clock_type.equalsIgnoreCase("hour")) return this.HOUR;
        else if(clock_type.equalsIgnoreCase("minutes")) return this.MINUTES;
        else if(clock_type.equalsIgnoreCase("seconds")) return this.SECONDS;
        else if(clock_type.equalsIgnoreCase("tandem")) return this.TANDEM;
        else return 0;
    }

    public String getClock(){

        return this.CLOCK;

    }

    public Double getNormalClock(){

        return this.NORMAL_CLOCK;

    }

    public void setToInterval(String interval){

        this.CLOCK_INTERVAL = interval;

    }

    public String getInterval(){

        return this.CLOCK_INTERVAL;

    }

    public void normalize(int first_time,String type){

        if(this.getSource().equals("bovespa") && type.equals("0_1")){

            this.setNormalT( ( (this.getComponent("hour")-first_time)*3600.0 + this.getComponent("minutes")*60 + this.getComponent("seconds") + this.getComponent("tandem")*0.000001  )/(7.0*3600.0) );

        }

        if(this.getSource().equals("bovespa") && type.equals("0_2pi") ){

            this.setNormalT( 2*Math.PI*( (this.getComponent("hour")-first_time)*3600.0 + this.getComponent("minutes")*60 + this.getComponent("seconds") + this.getComponent("tandem")*0.000001  )/(7.0*3600.0) );

        }

    }
    
}
