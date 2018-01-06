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
     *Update berita acara
     *
     */
    public function beritaAcaraDosen($beritaacara,$kodeMk,$mingguKe){
        $result = mysql_query("UPDATE absenngajar20171 SET beritaacara ='$beritaacara' WHERE `kdmk`='$kodeMk' AND `mingguke`='$mingguKe'");
        if($result){
            $id = mysql_insert_id();
            $result = mysql_query("SELECT * FROM absenngajar20171 WHERE Id = $id");
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

    public function getDataAbsenDosen($kodemk,$kdhari,$jamawal,$jamakhir){
        $result = mysql_query("SELECT * FROM jadwaldossplit20171 WHERE NoMk='$kodemk' AND JamAwal <= '$jamawal' AND JamAkhir >= '$jamakhir' AND Kd_hari='$kdhari'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            if ($kodemk && $kdhari) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }
	
	public function tampilkanMatakuliah($nidn,$kdhari,$jamawal,$jamakhir){
        $r = mysql_query("SELECT * FROM jadwaldossplit20171 INNER JOIN matakuliah ON NoMk=Kode_MK WHERE NIDN='$nidn' AND JamAwal <= '$jamawal' AND JamAkhir >= '$jamakhir' AND Kd_hari='$kdhari'");
        $result = array();

        while($row = mysql_fetch_array($r)){
            array_push($result,array(
                'matakuliah'=>$row['Nama_MK'],
				'nidn'=>$row['NIDN'],
				'kodeMK'=>$row['NoMk'],
            ));
        }

        if($result){
			echo json_encode(array('error'=>FALSE,'result'=>$result));
		}else{
			echo json_encode(array('error'=>TRUE,'message'=>'Anda tidak memiliki jam kuliah, Jika ada masalah silahkan hubungi admin'));
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

    public function selectMingguKe($kdmk,$ruang,$kelas){
        $result = mysql_query("SELECT max(`mingguke`)as 'n' FROM `absenngajar20171` WHERE kdmk = '$kdmk' AND ruang='$ruang' AND kelas='$kelas' ORDER BY jamawal DESC, jamakhir DESC");
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
    public function inputAbsenDosen($kdhari,$tglAbsen,$jamAwal,$jamakhir,$ruang,$kelas,$nidn,$kdmk,$sks,$jmlhhadir,$blnthnabsen,$kdProdi,$mingguke,$program,$operator,$thnSem,$idjadwal,$tglInput){
        $result  = mysql_query("INSERT INTO `absenngajar20171`(`kdhari`, `tglabsen`, `jamawal`, `jamakhir`, `ruang`, `kelas`,`nidn`, `kdmk`, `sks`, `jmlhadir`, `blnthnabsen`, `kdprodi`, `mingguke`, `program`, `operator`,`hitung`, `thnsem`, `idjadwal`, `tglinput`) VALUES ('$kdhari','$tglAbsen','$jamAwal','$jamakhir','$ruang','$kelas','$nidn','$kdmk','$sks','$jmlhhadir','$blnthnabsen','$kdProdi','$mingguke','$program','$operator','-1','$thnSem','$idjadwal','$tglInput')");
        if($result){
            $id = mysql_insert_id();
            $result = mysql_query("SELECT * FROM absenngajar20171 WHERE Id = $id");
            return mysql_fetch_array($result);
        }else{
            return false;
        }
    }

    /*
     * Check Ruang Kelas
     */
    public function cekRuangKelas($npm,$ruang,$matkul){



    }
	
	
	/*
     * Function lihat jika dosen sudah absen
     */
    public function cekAbsenDosen($nidn,$kdmk,$tglAbsen,$kodeHari){
        $result = mysql_query("SELECT * FROM `absenngajar20171` WHERE nidn='$nidn' and `kdmk`='$kdmk' and `kdhari`='$kodeHari' and `tglabsen`='$tglAbsen'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // user existed
            return true;
        } else {
            // user not existed
            return false;
        }
    }

    /*
     * Function lihat jika sudah absen
     */
    public function cekAbsenMhs($pertemuan,$npm,$kdmk){
        $result = mysql_query("SELECT npm FROM `absenmhs20171` WHERE `pertemuanke`='$pertemuan' AND npm='$npm' AND kdmk='$kdmk'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // user existed
            return true;
        } else {
            // user not existed
            return false;
        }
    }

    /*
     * Function untuk mengecek daftar mhs dalam kelas
     */
    public function cekMahasiswa($npm,$kodeMk){
        $result = mysql_query("SELECT `nimhstrnlm` FROM `trnlm20171` INNER JOIN msmhs ON nimhstrnlm=nimhsmsmhs WHERE `nimhstrnlm`='$npm' and `kdmktrnlm`='$kodeMk' and stmhsmsmhs='A'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // user existed
            return false;
        } else {
            // user not existed
            return true;
        }
    }

    /*
     * Fucntion untuk menampilkan mahasiswa yang sudah absen
     */
    public function getMhs($kdmk){
        $r = mysql_query("SELECT `nimhsmsmhs`,`nmmhsmsmhs`,stmhsmsmhs FROM `msmhs` INNER JOIN trnlm20171 ON `nimhsmsmhs`=nimhstrnlm WHERE kdmktrnlm='$kdmk' and stmhsmsmhs='A'");

        $result = array();

        while($row = mysql_fetch_array($r)){
            array_push($result,array(
                'Npm'=>$row['nimhsmsmhs'],
                'Nama'=>$row['nmmhsmsmhs']
            ));
        }

        echo json_encode(array('result'=>$result));
    }

    public function mhsAbsen($npm){
        $result = mysql_query("SELECT nimhsmsmhs,nmmhsmsmhs FROM msmhs WHERE nimhsmsmhs='$npm'");
        $no_of_rows = mysql_num_rows($result);
        if($no_of_rows > 0){
            $result = mysql_fetch_array($result);
            return $result;
        } else{
            return false;
        }
    }
	
	public function getBlnThnAndMingguKe($tglAwal,$tglAkhir){
		$result = mysql_query("SELECT * FROM `setabsenngajar` WHERE `tglawal`<='$tglAwal' and `tglakhir`>='$tglAkhir'");
		$no_of_rows = mysql_num_rows($result);
        if($no_of_rows > 0){
            $result = mysql_fetch_array($result);
            return $result;
        } else{
            return false;
        }
	}
	
	public function getIdAbsenNgajarFromTblAbsenNgajar($kdmk, $tglAbsen){
		$result = mysql_query("SELECT * FROM `absenngajar20171` WHERE `kdmk`='$kdmk' and `tglabsen`='$tglAbsen'");
		$no_of_rows = mysql_num_rows($result);
        if($no_of_rows > 0){
            $result = mysql_fetch_array($result);
            return $result;
        } else{
            return false;
        }
	}
	
	public function getDataSmartClass($nidn,$kdhari,$jamawal,$jamakhir,$ruang){
        $result = mysql_query("SELECT LEFT(Ruang, 4) FROM jadwaldossplit20171 WHERE NIDN='$nidn' AND JamAwal <= '$jamawal' AND JamAkhir >= '$jamakhir' AND LEFT(Ruang, 4)='$ruang' AND Kd_hari='$kdhari'");
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
}

?>