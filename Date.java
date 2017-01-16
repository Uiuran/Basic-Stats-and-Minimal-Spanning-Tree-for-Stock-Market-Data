/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BaseStats;

/**
 *
 * @author penalva
 */
public class Date {

    private String DATE = "00-00-00";
    private int day = 00;
    private int month = 00;
    private int year = 00;
    private String SOURCE = "";

    public Date(String date,String source){

        this.DATE = date;
        this.SOURCE = source;
        this.setDate(source);

    }

    public Date(){}

    private void setDate(String source){

        if(source.equalsIgnoreCase("bovespa")){

            String[] dates = this.DATE.split("-");

            this.day = Integer.valueOf(dates[2]);
            this.month = Integer.valueOf(dates[1]);
            this.year = Integer.valueOf(dates[0]);

        }

    }

    public int getComponent(String date_type){

        if( date_type.equalsIgnoreCase("day") ) return this.day;
        else if(date_type.equalsIgnoreCase("month")) return this.month;
        else if(date_type.equalsIgnoreCase("year")) return this.year;
        else return 0;

    }

    public String getDate(){ return this.DATE;}

    public void copy(Date date){

        this.DATE = date.getDate();
        this.day = date.getComponent("day");
        this.month = date.getComponent("month");
        this.year = date.getComponent("year");
        this.SOURCE = date.getSource();

    }

    public boolean isEqual(Date date){

        if(this.day == date.getComponent("day") && this.month == date.getComponent("month") && this.year == date.getComponent("year") ) return true;
        else return false;

    }

    public boolean isGreater(Date date){

        if( this.year > date.getComponent("year") ) return true;
        else if( this.year == date.getComponent("year") ){

            if( this.month > date.getComponent("month") ) return true;
            else if( this.month == date.getComponent("month") ){

                if( this.day > date.getComponent("day") ) return true;
                else return false;

            } else return false;

        }else return false;

    }

    public boolean isGreaterEqual(Date date){

        if( this.isGreater( date ) == true || this.isEqual( date ) == true ) return true;
        else return false;

    }

    public String getSource(){

        return this.SOURCE;
    }

}
