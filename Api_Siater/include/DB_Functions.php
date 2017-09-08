<?php

/**
 * Author by Dwi Romadon S.KOM
 * Fungsi untuk mengeksekusi qery dari database
 */

class DB_Functions {

    private $db;

    //put your code here
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }

    // destructor
    function __destruct() {
        
    }


    /**
     * Get user by Username and password
     */
    public function getUserByUsernameAndPassword($username, $password) {
        $result = mysql_query("SELECT * FROM groupuser WHERE username = '$username' and password='$password'") or die(mysql_error());
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            if ($username && $password) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }

    /*
     * Insert Absen Mahasiswa
     */
    public function inputAbsenMHS($idabsenngajar, $tglabsen, $pertemuanke, $npm, $kdmk, $kelas,$tglinput){
        $result  = mysql_query("INSERT INTO absenmhs20171(idabsenngajar, tglabsen, pertemuanke, npm, kdmk, kelas, jmlhadir, tglinput) VALUES ('$idabsenngajar', '$tglabsen', '$pertemuanke','$npm', '$kdmk', '$kelas', '1', '$tglinput') ");
        if($result){
            $id = mysql_insert_id();
            $result = mysql_query("SELECT * FROM absenmhs20171 WHERE Id = $id");
            return mysql_fetch_array($result);
        }else{
            return false;
        }
    }

    /*
     * Select data by Kode MK
     */
    public function selectKodeMK($kdmk){
        $result = mysql_query("SELECT * FROM absenngajar20171 WHERE kdmk = '$kdmk'") or die(mysql_error());
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            if ($kdmk) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }

    /*
     * Select Data Pertemuan ke
     */
    public function selectPertemuan($kdmk){
        $result = mysql_query("SELECT max(right(`pertemuanke`,4))as 'n' FROM `absenmhs20171` WHERE kdmk = '$kdmk'");
        $no_of_rows = mysql_num_rows($result);
        if($no_of_rows > 0){
            $result = mysql_fetch_array($result);
            return $result;
        } else{
            return false;
        }
    }

    public function getKodeMK($nidn){
        $r = mysql_query("SELECT DISTINCT kdmk FROM absenngajar20171 WHERE nidn = '$nidn'");

        $result = array();

        while($row = mysql_fetch_array($r)){
            array_push($result,array(
                'KodeMK'=>$row['kdmk']
            ));
        }

        echo json_encode(array('result'=>$result));
    }

    public function getKodeMKDosen($kdmk){
        $result = mysql_query("SELECT DISTINCT `kdhari`,`jamawal`,`jamakhir`,`ruang`,`kelas`,`nidn`,`kdmk`,`sks`, jmlhadir,`kdprodi` FROM `absenngajar20171` WHERE `kdmk` = '$kdmk'");
        $no_of_rows = mysql_num_rows($result);
        if($no_of_rows > 0){
            $result = mysql_fetch_array($result);
            return $result;
        } else{
            return false;
        }
    }

    public function getDataAbsenDosen($nidn,$kdhari,$jamawal,$jamakhir){
        $result = mysql_query("SELECT * FROM jadwaldossplit20171 WHERE NIDN='$nidn' AND JamAwal <= '$jamawal' AND JamAkhir >= '$jamakhir' AND Kd_hari='$kdhari'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            if ($nidn && $kdhari) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }

    public function getSKS($kdmk){
        $result = mysql_query("SELECT * from matakuliah WHERE Kode_MK='$kdmk'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            if ($kdmk) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }

    public function selectMingguKe($kdmk){
        $result = mysql_query("SELECT max(right(`mingguke`,4))as 'n' FROM `absenngajar20171` WHERE kdmk = '$kdmk'");
        $no_of_rows = mysql_num_rows($result);
        if($no_of_rows > 0){
            $result = mysql_fetch_array($result);
            return $result;
        } else{
            return false;
        }
    }

    /*
     * Insert Absen Mahasiswa
     */
    public function inputAbsenDosen($kdhari,$tglAbsen,$jamAwal,$jamakhir,$ruang,$kelas,$nidn,$kdmk,$sks,$jmlhhadir,$blnthnabsen,$kdProdi,$mingguke,$operator,$thnSem,$idjadwal,$tglInput){
        $result  = mysql_query("INSERT INTO `absenngajar20171`(`kdhari`, `tglabsen`, `jamawal`, `jamakhir`, `ruang`, `kelas`,`nidn`, `kdmk`, `sks`, `jmlhadir`, `blnthnabsen`, `kdprodi`, `mingguke`, `program`, `operator`,`hitung`, `thnsem`, `idjadwal`, `tglinput`) VALUES ('$kdhari','$tglAbsen','$jamAwal','$jamakhir','$ruang','$kelas','$nidn','$kdmk','$sks','$jmlhhadir','$blnthnabsen','$kdProdi','$mingguke','R','$operator','-1','$thnSem','$idjadwal','$tglInput')");
        if($result){
            $id = mysql_insert_id();
            $result = mysql_query("SELECT * FROM absenngajar20171 WHERE Id = $id");
            return mysql_fetch_array($result);
        }else{
            return false;
        }
    }
}

?>