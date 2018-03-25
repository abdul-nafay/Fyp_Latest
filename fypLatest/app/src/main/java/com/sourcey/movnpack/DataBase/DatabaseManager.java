package com.sourcey.movnpack.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sourcey.movnpack.Helpers.TaskScheduler;
import com.sourcey.movnpack.Model.AcceptedBidsModel;
import com.sourcey.movnpack.Model.AlarmRequestCodeModel;
import com.sourcey.movnpack.Model.AssignedTasksModel;
import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.Model.BidRecievedModel;
import com.sourcey.movnpack.Model.ConfirmBidModel;
import com.sourcey.movnpack.Model.SPBidCounterModel;
import com.sourcey.movnpack.Model.ServiceProvider;
import com.sourcey.movnpack.Model.User;
import com.sourcey.movnpack.Model.UserBidCounterModel;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Created by zafar on 10/22/17.
 */

public class DatabaseManager extends DatabaseHandler {


    private Semaphore semaphore = null;
    private SQLiteDatabase dbObj = null;
    private static DatabaseManager dbInstance;

    private DatabaseManager(Context context) {
        super(context);
        semaphore = new Semaphore(1);
        dbObj = null;
    }

    public static DatabaseManager getInstance(Context ctx){

        if (dbInstance == null){
            dbInstance = new DatabaseManager(ctx);
        }
        return dbInstance;
    }

    /**
     * This method return the database writable instance. And it is must to call the closeDb
     * so that other can use the db
     * @return SQLiteDatabase instance
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private SQLiteDatabase getDbForwrite() throws Exception
    {
        semaphore.acquire();
        dbObj = getWritableDatabase();
        return dbObj;
    }

    /**
     * This method return the database readable instance. And it is must to call the closeDb
     * so that other can use the db.
     * @return SQLiteDatabase instance
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private SQLiteDatabase getDbForRead() throws Exception
    {
        semaphore.acquire();
        dbObj = getReadableDatabase();
        return dbObj;
    }

    /**
     * This method closes the opened database and makes it available to others
     */
    @SuppressWarnings("unused")
    private void closeDb()
    {
        dbObj.close();
        semaphore.release();
    }


    public boolean addUser(User userModel){
        SQLiteDatabase db = null;
        try {
            db = getDbForwrite();
            UserTable userTable = new UserTable();
            long row = userTable.insertData(db,userModel);
            return  row >0  ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return false;

    }

    public User getUser(String email){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            UserTable userTable = new UserTable();
            data =userTable.getData(db, userTable.whereClause(), new String[]{email});
            return  data != null ? (User) (data.get(0)) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }


    public ArrayList<BaseModel> getUsers(){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            UserTable userTable = new UserTable();
            data =userTable.getData(db, null, null);
            return  data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }

    /// For Service Providers

    public boolean addServiceProvider(ServiceProvider serviceProviderModel){
        SQLiteDatabase db = null;
        try {
            db = getDbForwrite();
            ServiceProviderTable serviceProviderTable = new ServiceProviderTable();
            long row = serviceProviderTable.insertData(db,serviceProviderModel);
            return  row >0  ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return false;

    }

    public ServiceProvider getServiceProvider(String spId){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            ServiceProviderTable serviceProviderTable = new ServiceProviderTable();
            data =serviceProviderTable.getData(db, serviceProviderTable.whereClause(), new String[]{spId});
            return  data != null ? (ServiceProvider) (data.get(0)) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }


    public ArrayList<BaseModel> getServiceProviders(){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            ServiceProviderTable serviceProviderTable = new ServiceProviderTable();
            data =serviceProviderTable.getData(db, null, null);
            return  data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }


    /// End SP here

    public long updateUser(User userModel){

        SQLiteDatabase db = null;
        long result = 0;
        try {
            db = getDbForwrite();
            UserTable userTable=new UserTable() ;
            String[] whereArgs = {String.valueOf(userModel.getEmail())};
            result = userTable.updateData(db, userModel, userTable.whereClauseForUpdate(), whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return result;
    }

    public boolean edituserprofile(User userModel){
        boolean result=false;
        if(userModel!=null) {
            long x = updateUser(userModel);
            result = x > 0;
        }
        else {
            result=false;
        }
        return result;

    }


    public boolean addUserBid(BidModel bidModel){
        SQLiteDatabase db = null;
        try {
            db = getDbForwrite();
            Bid bid = new Bid();
            long row = bid.insertData(db,bidModel);
            return  row >0  ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return false;

    }

    public ArrayList<BaseModel> getBidsForUserId(){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            Bid bid = new Bid();
            data =bid.getData(db,null, null);

            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }

    public boolean addBidRecieved(BidRecievedModel bidRecievedModel){
        SQLiteDatabase db = null;
        try {
            db = getDbForwrite();
            BidRecievedTable bidRecievedTable = new BidRecievedTable();
            long row = bidRecievedTable.insertData(db,bidRecievedModel);
            return  row >0  ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return false;

    }

    public ArrayList<BaseModel> getBidsRecieved(){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            BidRecievedTable bid = new BidRecievedTable();
            String[] whereArgs = {String.valueOf("3")};
            data =bid.getData(db,bid.whereClauseForNonRejectedBids(), whereArgs);

            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }

    public ArrayList<BaseModel> getConfirmedBids(){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            Confirmed_Bids bid = new Confirmed_Bids();
            String[] whereArgs = {String.valueOf("1")};
            data =bid.getData(db,bid.whereClauseForNonDeleted(), whereArgs);

            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }
    public ArrayList<BaseModel> getAssignedTasks(){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            Assigned_Tasks bid = new Assigned_Tasks();
            String[] whereArgs = {String.valueOf("1")};
            data =bid.getData(db,bid.whereClauseForNonDeleted(), whereArgs);

            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }
    public long updateBidRecievedStatus(BidRecievedModel bidRecievedModel){

        SQLiteDatabase db = null;
        long result = 0;
        try {
            db = getDbForwrite();
            BidRecievedTable bidRecievedTable=new BidRecievedTable() ;
            String[] whereArgs = {String.valueOf(bidRecievedModel.getBidId())};
            result = bidRecievedTable.updateData(db, bidRecievedModel, bidRecievedTable.whereClauseForUpdate(), whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return result;
    }

    public long updateAssignedTask(AssignedTasksModel assignedTasksModel){

        SQLiteDatabase db = null;
        long result = 0;
        try {
            db = getDbForwrite();
            Assigned_Tasks assignedTasks=new Assigned_Tasks() ;
            String[] whereArgs = {String.valueOf(assignedTasksModel.getID())};
            result = assignedTasks.updateData(db, assignedTasksModel, assignedTasks.whereClauseForUpdate(), whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return result;
    }

    public long updateConfrimBid(ConfirmBidModel confirmBidModel){

        SQLiteDatabase db = null;
        long result = 0;
        try {
            db = getDbForwrite();
            Confirmed_Bids confirmed_bids=new Confirmed_Bids() ;
            String[] whereArgs = {String.valueOf(confirmBidModel.getID())};
            result = confirmed_bids.updateData(db, confirmBidModel, confirmed_bids.whereClauseForUpdateID(), whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return result;
    }

    public boolean editBidRecieved(BidRecievedModel bidRecievedModel){
        boolean result=false;
        if(bidRecievedModel!=null) {
            long x = updateBidRecievedStatus(bidRecievedModel);
            result = x > 0;
        }
        else {
            result=false;
        }
        return result;

    }

    public boolean addAcceptedBids(AcceptedBidsModel acceptedBidsModel){
        SQLiteDatabase db = null;
        try {
            db = getDbForwrite();
            AcceptedBids acceptedBids = new AcceptedBids();
            long row = acceptedBids.insertData(db,acceptedBidsModel);
            return  row >0  ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return false;

    }


    public ArrayList<BaseModel> getAcceptedBidId(String bidId){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            AcceptedBids acceptedBids = new AcceptedBids();
            String[] whereArgs = {String.valueOf(bidId)};
            data =acceptedBids.getData(db, acceptedBids.whereClause(), whereArgs);
            return  data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }

    public boolean addUserBidCounter(UserBidCounterModel userBidCounterModel){
        SQLiteDatabase db = null;
        try {
            db = getDbForwrite();
            UserBidCounterTable userBidCounterTable = new UserBidCounterTable();
            long row = userBidCounterTable.insertData(db,userBidCounterModel);
            return  row >0  ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return false;

    }

    public boolean addAssignedTasks(AssignedTasksModel assignedTasksModel){
        SQLiteDatabase db = null;
        try {
            db = getDbForwrite();
            Assigned_Tasks assignedTasksTable = new Assigned_Tasks();
            long row = assignedTasksTable.insertData(db,assignedTasksModel);
            return  row >0  ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return false;

    }

    public ArrayList<BaseModel> getUserBidCounterById(String bidId){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            UserBidCounterTable bidCounterTable = new UserBidCounterTable();
            String[] whereArgs = {String.valueOf(bidId)};
            data =bidCounterTable.getData(db, bidCounterTable.whereClause(), whereArgs);
            return  data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }

    public boolean addSPBidCounter(SPBidCounterModel spBidCounterModel){
        SQLiteDatabase db = null;
        try {
            db = getDbForwrite();
            SPBidCounterTable spBidCounterTable = new SPBidCounterTable();
            long row = spBidCounterTable.insertData(db,spBidCounterModel);
            return  row >0  ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return false;

    }

    public boolean addConfirmBidUser(ConfirmBidModel confirmBidModel){
        SQLiteDatabase db = null;
        try {
            db = getDbForwrite();
            Confirmed_Bids confirmBidTable = new Confirmed_Bids();
            long row = confirmBidTable.insertData(db,confirmBidModel);
            return  row >0  ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return false;

    }


    public ArrayList<BaseModel> getSPBidCounterById(String bidId){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            SPBidCounterTable spBidCounterTable = new SPBidCounterTable();
            String[] whereArgs = {String.valueOf(bidId)};
            data =spBidCounterTable.getData(db, spBidCounterTable.whereClause(), whereArgs);
            return  data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }

    public ArrayList<BaseModel> getBidById(String bidId){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            Bid bid = new Bid();
            String[] whereArgs = {String.valueOf(bidId)};
            data =bid.getData(db, bid.whereClauseForData(), whereArgs);
            return  data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }


    public ArrayList<BaseModel> getBidReceivedById(String bidId){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            BidRecievedTable bid = new BidRecievedTable();
            String[] whereArgs = {String.valueOf(bidId)};
            data =bid.getData(db, bid.whereClauseForData(), whereArgs);
            return  data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }


    public BaseModel getAssignedTaskByBidId(String bidId){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            Assigned_Tasks bid = new Assigned_Tasks();
            String[] whereArgs = {String.valueOf(bidId) , String.valueOf("0")};
            data =bid.getData(db, bid.whereClauseForData1(), whereArgs);
            if (data != null && data.size() > 0 ) {
                return data.get(0);
            }
            else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }

    public AssignedTasksModel getAssignedTaskByID(String ID){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            Assigned_Tasks bid = new Assigned_Tasks();
            String[] whereArgs = {String.valueOf(ID)};
            data =bid.getData(db, bid.whereClauseForDataWIthID(), whereArgs);
            if (data != null && data.size() > 0 ) {
                return (AssignedTasksModel) data.get(0);
            }
            else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }

    public BaseModel getConfirmedBid(String bidId){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            Confirmed_Bids bid = new Confirmed_Bids();
            String[] whereArgs = {String.valueOf(bidId) , "0"};
            data =bid.getData(db, bid.whereClauseForData(), whereArgs);
            if (data != null && data.size() > 0 ) {
                return data.get(0);
            }
            else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }
    public ConfirmBidModel getConfirmedBidByID(String cbmID){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            Confirmed_Bids bid = new Confirmed_Bids();
            String[] whereArgs = {String.valueOf(cbmID)};
            data =bid.getData(db, bid.whereClauseID(), whereArgs);
            if (data != null && data.size() > 0 ) {
                return (ConfirmBidModel) data.get(0);
            }
            else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }

    public BidModel getBidWithID(String bidID){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            Bid bid = new Bid();
            String[] whereArgs = {String.valueOf(bidID)};
            data =bid.getData(db, bid.whereClauseForData(), whereArgs);
            if (data != null && data.size() > 0 ) {
                return (BidModel) data.get(0);
            }
            else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }

    public boolean deleteAssignedTaskModel(AssignedTasksModel model) {/*
        SQLiteDatabase db = null;
        try {
            db = getDbForwrite();
            Assigned_Tasks bid = new Assigned_Tasks();
            String[] whereArgs = {String.valueOf(model.getBidId())};
            long row = bid.deleteData(db,bid.whereClauseForDelete(),whereArgs);
            return  row >0  ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return false;*/
        boolean result=false;
        if(model!=null) {
            model.setIsDeleted("1");
            long x = updateAssignedTask(model);
            result = x > 0;
        }
        return result;
    }

    public boolean deleteConfirmBid(ConfirmBidModel model) {
        boolean result=false;
        if(model!=null) {
            // Cancel Scheduled Alarm
           AlarmRequestCodeModel alarmRequestCodeModel = getAlarmRequestForBidID(model.getID());
           if (alarmRequestCodeModel != null){
               TaskScheduler.getInstance().deleteAlarmforTask(model,alarmRequestCodeModel);
               deleteAlarmRequest(alarmRequestCodeModel.getID());
           }
            //
            model.setIsDeleted("1");
            long x = updateConfrimBid(model);
            result = x > 0;
        }
        return result;
    }


    public boolean addAlarmRequestCode(AlarmRequestCodeModel alarmRequestCodeModel){
        SQLiteDatabase db = null;
        try {
            db = getDbForwrite();
            AlarmRequestCodeTable alarmRequestCodeTable = new AlarmRequestCodeTable();
            long row = alarmRequestCodeTable.insertData(db,alarmRequestCodeModel);
            return  row >0  ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return false;
    }

    public AlarmRequestCodeModel getAlarmRequestForBidID(String taskID){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            AlarmRequestCodeTable bid = new AlarmRequestCodeTable();
            String[] whereArgs = {String.valueOf(taskID)};
            data =bid.getData(db, bid.whereClauseForData(), whereArgs);
            if (data != null && data.size() > 0 ) {
                return (AlarmRequestCodeModel) data.get(0);
            }
            else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }

    public boolean deleteAlarmRequest(String ID){
        SQLiteDatabase db = null;
        try {
            db = getDbForwrite();
            AlarmRequestCodeTable alarmRequestCodeTable = new AlarmRequestCodeTable();
            String[] whereArgs = {String.valueOf(ID)};
            long row = alarmRequestCodeTable.deleteData(db,alarmRequestCodeTable.whereClause(),whereArgs);
            return  row >0  ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return false;
    }

}
