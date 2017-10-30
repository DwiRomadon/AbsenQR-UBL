package pojo;

/**
 * Created by Terminator on 14/09/2017.
 */

public class DataMhsAbsen {
    private String no,npm,nama;

    public DataMhsAbsen(){}

    public DataMhsAbsen(String no,String npm, String nama){
        this.no     = no;
        this.npm    = npm;
        this.nama   = nama;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNpm() {
        return npm;
    }

    public void setNpm(String npm) {
        this.npm = npm;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
