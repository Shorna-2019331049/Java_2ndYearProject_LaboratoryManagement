package LaboratoryManagement;

public class ReportANDPdf{
    private String expName,report, pdf,status;
    double mrk,fmrk;

    public double getMrk() {
        return mrk;
    }

    public void setMrk(double mrk) {
        this.mrk = mrk;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getFmrk() {
        return fmrk;
    }

    public void setFmrk(double fmrk) {
        this.fmrk = fmrk;
    }

    public ReportANDPdf(String expName, String pdf, String status, double mrk, double fmrk) {
        this.expName = expName;
        this.pdf = pdf;
        this.status=status;
        this.mrk=mrk;
        this.fmrk=fmrk;
    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

//    public String getReport() {
//        return report;
//    }
//
//    public void setReport(String report) {
//        this.report = report;
//    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}
