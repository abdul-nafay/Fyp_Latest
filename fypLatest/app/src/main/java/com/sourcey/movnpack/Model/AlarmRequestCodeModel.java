package com.sourcey.movnpack.Model;

/**
 * Created by ali.haider on 2/13/2018.
 */

public class AlarmRequestCodeModel extends BaseModel {
    String ID , taskID ;

    public AlarmRequestCodeModel() {

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }
}
