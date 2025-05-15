package LaboratoryManagement;

import javafx.scene.control.CheckBox;

import java.time.LocalDate;
import java.util.Date;

public class fine {
    private String expNam;
    private String mat;
    private String status;
    private Date dat;
    private CheckBox retStatus;

    public fine(String expNam, String mat, String status, Date dat, boolean st) {
        this.expNam = expNam;
        this.mat = mat;
        this.status = status;
        this.dat = dat;
        this.retStatus = new CheckBox();
        if(st){
            retStatus.setSelected(true);
            retStatus.setDisable(true);
        }
        else if(dat.before(java.sql.Date.valueOf(LocalDate.now()))){
            retStatus.setDisable(true);
        }
        else if(dat.after(java.sql.Date.valueOf(LocalDate.now()))){
            retStatus.setSelected(false);
            retStatus.setDisable(true);
        }
    }

    public String getExpNam() {
        return expNam;
    }

    public void setExpNam(String expNam) {
        this.expNam = expNam;
    }

    public String getMat() {
        return mat;
    }

    public void setMat(String mat) {
        this.mat = mat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDat() {
        return dat;
    }

    public void setDat(Date dat) {
        this.dat = dat;
    }

    public CheckBox getRetStatus() {
        return retStatus;
    }

    public void setRetStatus(CheckBox retStatus) {
        this.retStatus = retStatus;
    }
}
