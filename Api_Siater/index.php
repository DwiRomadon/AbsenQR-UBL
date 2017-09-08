<?php

/**
 * Author by Dwi Romadon S.KOM
 */
if (isset($_POST['tag']) && $_POST['tag'] != '') {
    // get tag
    $tag = $_POST['tag'];

    // include db handler
    require_once 'include/DB_Functions.php';
    $db = new DB_Functions();

    // response Array
    $response = array("tag" => $tag, "error" => FALSE);

    // check for tag type
    if ($tag == 'login') {
        // Request type is check Login
        $username = $_POST['username'];
        $password = md5($_POST['password']);

        // check for user
        $user = $db->getUserByUsernameAndPassword($username, $password);
        if ($user != false) {
            // user found
            $response["error"]  = FALSE;
            $response["id"]     = $user["Id"];
			$response["nama"]   = $user["nmlengkap"];
			$response["level"]  = $user["id_level"];
            $response["nidn"]   = $user["username"];
            echo json_encode($response);
        } else {
            // user not found
            // echo json with error = 1
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Periksa kembali Username atau Password ! ";
            echo json_encode($response);
        }
    } else if($tag == 'inputabsenmhs'){
        $idabsenngajar = $_POST['idabsenngajar'];
        $tglAbsen      = $_POST['tglabsen'];
        $pertemuanke   = $_POST['pertemuan'];
        $npm           = $_POST['npm'];
        $kdmk          = $_POST['kodemk'];
        $kelas         = $_POST['kelas'];
        $tglInput      = $_POST['tglInput'];
        $user = $db->inputAbsenMHS($idabsenngajar, $tglAbsen, $pertemuanke, $npm, $kdmk, $kelas, $tglInput);
        if ($user) {
            // user stored successfully
            $response["error"]      = FALSE;
            $response["Id"]                 = $user["Id"];
            $response["IdAbsenNgajar"]      = $user["idabsenngajar"];
            $response["TglAbsen"]           = $user["tglabsen"];
            $response["Pertemuan"]          = $user["pertemuanke"];
            $response["Npm"]                = $user["npm"];
            $response["KodeMK"]             = $user["kdmk"];
            $response["Kelas"]              = $user["kelas"];
            $response["JumlahHadir"]        = $user["jmlhadir"];
            $response["TglInput"]           = $user["tglinput"];
            $response["success"]            = "Sukses Input Absen ";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal Input Absen, Silahkan Coba Kembali";
            echo json_encode($response);
        }
    } else if($tag == 'selectKodeMK'){
        $kdmk = $_POST['kode'];

        $user = $db->selectKodeMK($kdmk);
        if ($user) {
            // user stored successfully
            $response["error"]          = FALSE;
            $response["IdAbsenNgajar"]  = $user["Id"];
            $response["Kelas"]          = $user["kelas"];
            $response["success"]        = "Sukses Mengambil Data ";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal, Silahkan Cek Kode Matakuliah !!";
            echo json_encode($response);
        }
    } else if ($tag == 'getKodeMK'){

        $nidn = $_POST['nidn'];

        $user = $db->getKodeMK($nidn);
    }else if($tag == 'getPertemuanke'){
        $kdmk = $_POST['kode'];
        $user = $db->selectPertemuan($kdmk);
        if ($user) {
            // user stored successfully
            $response["error"]          = FALSE;
            $response["pertemuan"]      = $user["n"]+1;
            $response["success"]        = "Sukses Mengambil Data ";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal, Silahkan Cek Kode Matakuliah !!";
            echo json_encode($response);
        }
    }else if($tag == 'getSKS'){
        $kdMk = $_POST['kodemk'];
        $user = $db->getSKS($kdMk);
        if ($user) {

            $response["error"]          = FALSE;
            $response["SKS"]            = $user["SKS"];
            $response["success"]        = "Sukses Mengambil Data ";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal!!";
            echo json_encode($response);
        }
    }else if($tag == 'getAbsenDosen'){
        $nidn       = $_POST['nidn'];
        $kdhari     = $_POST['kdhari'];
        $jamawal    = $_POST['awal'];
        $jamakhir   = $_POST['akhir'];
        $user       = $db->getDataAbsenDosen($nidn,$kdhari,$jamawal,$jamakhir);
        if ($user) {

            $response["error"]              = FALSE;
            $response["KodeHari"]           = $user["Kd_hari"];
            $response["JamAwal"]            = $user["JamAwal"];
            $response["JamAkhir"]           = $user["JamAkhir"];
            $response["KodeMK"]             = $user["NoMk"];
            $response["Ruang"]              = $user["Ruang"];
            $response["Kelas"]              = $user["Kelas"];
            //$response["NIDN"]             = $user["NIDN"];
            $response["ThnSem"]             = $user["ThnSmester"];
            $response["KodeProdi"]          = $user["Kd_Jur"];
            $response["IdJadwal"]           = $user["Id"];
            $response["success"]            = "Sukses Mengambil Data";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Waktu Absen sudah habis";
            echo json_encode($response);
        }
    }else if($tag == 'getMingguke'){
        $kdmk = $_POST['kode'];
        $user = $db->selectMingguKe($kdmk);
        if ($user) {
            // user stored successfully
            $response["error"]          = FALSE;
            $response["mingguke"]       = $user["n"]+1;
            $response["success"]        = "Sukses Mengambil Data ";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal, Silahkan Cek Kode Matakuliah !!";
            echo json_encode($response);
        }
    }else if($tag == 'inputabsendosen'){

        $kdhari         = $_POST['kdhari'];
        $tglAbsen       = $_POST['tglAbsen'];
        $jamAwal        = $_POST['jamAwal'];
        $jamakhir       = $_POST['jamakhir'];
        $ruang          = $_POST['ruang'];
        $kelas          = $_POST['kelas'];
        $nidn           = $_POST['nidn'];
        $kdmk           = $_POST['kdmk'];
        $sks            = $_POST['sks'];
        $jmlhhadir      = $_POST['jmlhadir'];
        $blnthnabsen    = $_POST['blnthnabsen'];
        $kdProdi        = $_POST['kdProdi'];
        $mingguke       = $_POST['mingguKe'];
        $operator       = $_POST['operator'];
        $thnSem         = $_POST['thnSem'];
        $idjadwal       = $_POST['idJadwal'];
        $tglInput       = $_POST['tglInput'];

        $user = $db->inputAbsenDosen($kdhari,$tglAbsen,$jamAwal,$jamakhir,$ruang,$kelas,$nidn,$kdmk,$sks,$jmlhhadir,$blnthnabsen,$kdProdi,$mingguke,$operator,$thnSem,$idjadwal,$tglInput);
        if ($user) {
            // user stored successfully
            $response["error"]      = FALSE;
            /*$response["Id"]                 = $user["Id"];
            $response["IdAbsenNgajar"]      = $user["idabsenngajar"];
            $response["TglAbsen"]           = $user["tglabsen"];
            $response["Pertemuan"]          = $user["pertemuanke"];
            $response["Npm"]                = $user["npm"];
            $response["KodeMK"]             = $user["kdmk"];
            $response["Kelas"]              = $user["kelas"];
            $response["JumlahHadir"]        = $user["jmlhadir"];
            $response["TglInput"]           = $user["tglinput"];*/
            $response["success"]            = "Sukses";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal";
            echo json_encode($response);
        }
    }
    else {
        // user failed to store
        $response["error"]      = TRUE;
        $response["error_msg"]  = "Unknow 'tag' value.";
        echo json_encode($response);
    }
} else {
    $response["error"]      = TRUE;
    $response["error_msg"]  = "Required parameter 'tag' is missing!";
    echo json_encode($response);
}
?>