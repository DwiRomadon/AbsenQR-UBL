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

        if($db->cekMahasiswa($npm,$kdmk)){
            $response["error"] = TRUE;
            $response["pesan_error"] = "Input Absen Gagal Karna Npm Mahasiswa Tidak Terdaftar di Kelas Ini Atau Mahasiswa Belum Melakukan Pembayaran, Jika ada masalah silahkan hubungi admin. Terimakasih...";
            echo json_encode($response);
        }else{
            /*if($db->cekAbsenMhs($pertemuanke,$npm,$kdmk) ){
                $response["error"] = TRUE;
                $response["pesan_error"] = "Input Absen Gagal Karna Mahasiswa Sudah Absen Dipertemuan Ini";
                echo json_encode($response);
            }else{*/
                $user = $db->inputAbsenMHS($idabsenngajar, $tglAbsen, $pertemuanke, $npm, $kdmk, $kelas, $tglInput);
                if ($user) {
                    // user stored successfully
                    $response["error"]              = FALSE;
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
            //}
        }
    }else if($tag == 'UpdateBeritaAcara'){
        $beritaAcara        =  $_POST['beritaacara'];
        $kodeMk             = $_POST['kodemk'];
        $mingguke           = $_POST['mingguKe'];
        $user = $db->beritaAcaraDosen($beritaAcara,$kodeMk,$mingguke);
        if ($user) {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal";
            echo json_encode($response);
        } else {
            // user stored successfully
            $response["error"]              = FALSE;
            //$response["BeritaAcara"]        = $user["beritaacara"];
            $response["success"]            = "Sukses";
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
    }else if($tag == 'getNamaMhsDalamSatuKelas'){
        $kdmk = $_POST['kode'];
        $user = $db->getMhs($kdmk);
    }
	else if($tag == 'getSKS'){
        $kdMk = $_POST['kodemk'];
        $user = $db->getSKS($kdMk);
        if ($user) {

            $response["error"]          = FALSE;
            $response["SKS"]            = $user["SKS"];
			$response["NamaMK"]			= $user["Nama_MK"];
            $response["success"]        = "Sukses Mengambil Data ";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal!!";
            echo json_encode($response);
        }
    }else if($tag == 'npmMhs'){

        $npm = $_POST['npm'];
        $user       = $db->mhsAbsen($npm);
        if ($user) {

            $response["error"]              = FALSE;
            $response["No"]                 = "0";
            $response["Npm"]                = $user["nimhsmsmhs"];
            $response["Nama"]               = $user["nmmhsmsmhs"];
            $response["success"]            = "Sukses Mengambil Data";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal cek mahasiswa";
            echo json_encode($response);
        }

    }else if($tag == 'getAbsenDosen'){
        $kodemk     = $_POST['kodemk'];
        $kdhari     = $_POST['kdhari'];
        $jamawal    = $_POST['awal'];
        $jamakhir   = $_POST['akhir'];
        $user       = $db->getDataAbsenDosen($kodemk,$kdhari,$jamawal,$jamakhir);
        if ($user) {
            $response["error"]              = FALSE;
            $response["KodeHari"]           = $user["Kd_hari"];
            $response["JamAwal"]            = $user["JamAwal"];
            $response["JamAkhir"]           = $user["JamAkhir"];
            $response["KodeMK"]             = $user["NoMk"];
            $response["Ruang"]              = $user["Ruang"];
            $response["Kelas"]              = $user["Kelas"];
            $response["NIDN"]               = $user["NIDN"];
			$response["KodeProgram"]        = $user["Kd_Program"];
            $response["ThnSem"]             = $user["ThnSmester"];
            $response["KodeProdi"]          = $user["Kd_Jur"];
            $response["IdJadwal"]           = $user["Id"];
            $response["success"]            = "Sukses Mengambil Data";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Anda tidak memiliki jam mengajar atau waktu absen sudah habis, silihkan hubungi admin jika ada masalah. Terimakasih...";
            echo json_encode($response);
        }
    }else if($tag == 'getMingguke'){
        $kdmk = $_POST['kode'];
        $ruang= $_POST['ruang'];
		$kelas= $_POST['kelas'];
        $user = $db->selectMingguKe($kdmk,$ruang,$kelas);
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
    }else if($tag == 'checkMatakuliah'){
        $nidn 		= $_POST['nidn'];
        $kdhari		= $_POST['kodehari'];
		$jamAwal	= $_POST['jamawal'];
		$jamAkhir	= $_POST['jamakhir'];
        $user = $db->tampilkanMatakuliah($nidn,$kdhari,$jamAwal,$jamAkhir);
        
    }else if($tag == 'getBlnThnAbsenAndMinnguKe'){
        $tglAwal  = $_POST['tglAwal'];
        $tglAkhir = $_POST['tglAkhir'];
        $user = $db->getBlnThnAndMingguKe($tglAwal,$tglAkhir);
        if ($user) {
            // user stored successfully
            $response["error"]          = FALSE;
            $response["BlnThnAbsen"]    = $user["blnthn"];
            $response["MingguKe"]       = $user["mingguke"];
            //$response["success"]        = "Sukses Mengambil Data ";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal, Minggu belum disetting, silahkan hubungi administrator";
            echo json_encode($response);
        }
    }else if($tag == 'getIdAbsenNgajar'){
        $kdmk  		= $_POST['kodemk'];
        $tglAbsen 	= $_POST['tglAbsen'];
        $user = $db->getIdAbsenNgajarFromTblAbsenNgajar($kdmk,$tglAbsen);
        if ($user) {
            // user stored successfully
            $response["error"]          = FALSE;
            $response["IdAbsenNgajar"]    = $user["Id"];
            $response["success"]        = "Sukses Mengambil Data ";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal, Minggu belum disetting, silahkan hubungi administrator";
            echo json_encode($response);
        }
    }else if($tag == 'getSmartClass'){
        $nidn       = $_POST['nidn'];
        $kdhari     = $_POST['kdhari'];
        $jamawal    = $_POST['awal'];
        $jamakhir   = $_POST['akhir'];
		$ruang		= $_POST['ruang'];
        $user       = $db->getDataSmartClass($nidn,$kdhari,$jamawal,$jamakhir,$ruang);
        if ($user) {
            ob_start();
			$response["error"]              = FALSE;
			//$response["Ruang"]              = $user["LEFT(Ruang, 4)"];
            /*$response["KodeHari"]           = $user["Kd_hari"];
            $response["JamAwal"]            = $user["JamAwal"];
            $response["JamAkhir"]           = $user["JamAkhir"];
            $response["KodeMK"]             = $user["NoMk"];
            $response["Ruang"]              = $user["Ruang"];
            $response["Kelas"]              = $user["Kelas"];
            //$response["NIDN"]             = $user["NIDN"];
			$response["KodeProgram"]        = $user["Kd_Program"];
            $response["ThnSem"]             = $user["ThnSmester"];
            $response["KodeProdi"]          = $user["Kd_Jur"];
            $response["IdJadwal"]           = $user["Id"];
            $response["success"]            = "Sukses Mengambil Data";*/
			$response["success"]            = "Silahkan masuk";
			
			
			$file1 = fopen("light.json", "w") or die("can't open file");
			fwrite($file1, '{"light": "on"}');
			fclose($file1);
			echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Anda tidak bisa membuka kelas karna anda tidak memiliki jam kuliah, silihkan hubungi admin jika ada masalah. Terimakasih...";
            $file = fopen("light.json", "w") or die("can't open file");
			fwrite($file, '{"light": "off"}');
			fclose($file);
			echo json_encode($response);
        }
		
    }else if($tag == 'getTutupPintu'){
        $file = fopen("light.json", "w") or die("can't open file");
		fwrite($file, '{"light": "off"}');
		fclose($file);
		$response["success"]       = "Berhasil Menutup Pintu";
		echo json_encode($response);
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
		$program  		= $_POST['program'];
        $operator       = $_POST['operator'];
        $thnSem         = $_POST['thnSem'];
        $idjadwal       = $_POST['idJadwal'];
        $tglInput       = $_POST['tglInput'];

		if($db->cekAbsenDosen($nidn,$kdmk,$tglAbsen,$kdhari)){
			$response["error"] = TRUE;
            $response["error_msg"] = "Input Absen Gagal Karna Dosen Sudah Absen Dipertemuan Ini";
            echo json_encode($response);
         
		}else{
			$user = $db->inputAbsenDosen($kdhari,$tglAbsen,$jamAwal,$jamakhir,$ruang,$kelas,$nidn,$kdmk,$sks,$jmlhhadir,$blnthnabsen,$kdProdi,$mingguke,$program,$operator,$thnSem,$idjadwal,$tglInput);
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