package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;

import javax.naming.LimitExceededException;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;

public class User {
    private String userName;
    private ArrayList<ShoppingTrip> trips;
    private Date lastLogin;
    private Date creationDate;
    public SimpleStringProperty ssp;

    public User (String userName) throws IOException, LimitExceededException {
        creationDate = new Date();
        this.userName = userName;
        trips = new ArrayList<ShoppingTrip>();
        lastLogin = new Date();
    }
    public User(String userName, Date lastLogin, Date creationDate){
        this.userName = userName;
        this.creationDate = creationDate;
        trips = new ArrayList<ShoppingTrip>();
        this.lastLogin = lastLogin;
    }


    public User(String userName, Date lastLogin, Date creationDatm, ArrayList<ShoppingTrip> trips){
        this.userName = userName;
        this.creationDate = creationDate;
        this.trips = trips;
        this.lastLogin = lastLogin;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public String getLastLoginFromNow(){
        long miliTime =  (long) new Date().getTime() - lastLogin.getTime();
        if (miliTime < 1000) {
            return miliTime + "ms ago";
        }
        long secTime = miliTime/1000;
        if (secTime < 60) {
            return secTime + "s ago";
        }
        long minTime = secTime/60;
        if (minTime<60){
            return minTime + " min ago";
        }
        long hourTime = minTime/60;
        if(hourTime<24){
            if (hourTime == 1){
                return hourTime + " hour ago";
            }
            return hourTime + " hours ago";
        }
        long dayTime = hourTime/24;
        if (dayTime == 1){
            return dayTime + " day ago";
        }
        return dayTime + " days ago";
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public ArrayList<ShoppingTrip> getTrips() {
        return trips;
    }





}
