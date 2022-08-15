-- db_simrt.form_email definition

CREATE TABLE `form_email` (
  `noemail` varchar(15) NOT NULL,
  `tglemail` date DEFAULT NULL,
  `ketemail` varchar(50) DEFAULT NULL,
  `kdiuran` varchar(7) DEFAULT NULL,
  `jenisiuran` varchar(10) DEFAULT NULL,
  `namaiuran` varchar(20) DEFAULT NULL,
  `tagihan` int(11) DEFAULT NULL,
  `norekening` varchar(30) DEFAULT NULL,
  `idwarga` varchar(7) DEFAULT NULL,
  `norumah` varchar(10) DEFAULT NULL,
  `nama` varchar(20) DEFAULT NULL,
  `statuskeluarga` varchar(10) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`noemail`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- db_simrt.form_iuran definition

CREATE TABLE `form_iuran` (
  `noiuran` varchar(15) NOT NULL,
  `tgliuran` date DEFAULT NULL,
  `jmlpembayaran` int(11) DEFAULT NULL,
  `statusiuran` varchar(10) DEFAULT NULL,
  `noemail` varchar(15) DEFAULT NULL,
  `etemail` varchar(50) DEFAULT NULL,
  `kdiuran` varchar(7) DEFAULT NULL,
  `tagihan` int(11) DEFAULT NULL,
  `idwarga` varchar(7) DEFAULT NULL,
  `norumah` varchar(10) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`noiuran`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- db_simrt.form_laporan definition

CREATE TABLE `form_laporan` (
  `nolaporan` varchar(15) NOT NULL,
  `tgllapor` date DEFAULT NULL,
  `kdlaporan` varchar(7) DEFAULT NULL,
  `jenislaporan` varchar(10) DEFAULT NULL,
  `ketlaporan` varchar(30) DEFAULT NULL,
  `idwarga` varchar(7) DEFAULT NULL,
  `nama` varchar(20) DEFAULT NULL,
  `jeniskelamin` char(1) DEFAULT NULL,
  `statustinggal` varchar(10) DEFAULT NULL,
  `alamat` varchar(50) DEFAULT NULL,
  `telepon` varchar(13) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`nolaporan`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- db_simrt.form_surat definition

CREATE TABLE `form_surat` (
  `nosurat` varchar(15) NOT NULL,
  `tglsurat` date DEFAULT NULL,
  `kdsurat` varchar(7) DEFAULT NULL,
  `jenissurat` varchar(10) DEFAULT NULL,
  `ketsurat` varchar(30) DEFAULT NULL,
  `idwarga` varchar(30) DEFAULT NULL,
  `nama` varchar(20) DEFAULT NULL,
  `jeniskelamin` char(1) DEFAULT NULL,
  `alamat` varchar(50) DEFAULT NULL,
  `telepon` varchar(13) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`nosurat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- db_simrt.tb_daftariuran definition

CREATE TABLE `tb_daftariuran` (
  `kdiuran` varchar(7) NOT NULL,
  `jenisiuran` varchar(10) DEFAULT NULL,
  `namaiuran` varchar(20) DEFAULT NULL,
  `tagihan` int(11) DEFAULT NULL,
  `norekening` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`kdiuran`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- db_simrt.tb_daftarlaporan definition

CREATE TABLE `tb_daftarlaporan` (
  `kdlaporan` varchar(7) NOT NULL,
  `jenislaporan` varchar(10) DEFAULT NULL,
  `ketlaporan` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`kdlaporan`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- db_simrt.tb_daftarsurat definition

CREATE TABLE `tb_daftarsurat` (
  `kdsurat` varchar(7) NOT NULL,
  `jenissurat` varchar(10) DEFAULT NULL,
  `ketsurat` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`kdsurat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- db_simrt.tb_daftaruser definition

CREATE TABLE `tb_daftaruser` (
  `iduser` varchar(7) NOT NULL,
  `namauser` varchar(20) DEFAULT NULL,
  `level` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`iduser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- db_simrt.tb_daftarwarga definition

CREATE TABLE `tb_daftarwarga` (
  `idwarga` varchar(7) NOT NULL,
  `tglmasuk` date DEFAULT NULL,
  `norumah` varchar(10) DEFAULT NULL,
  `nama` varchar(20) DEFAULT NULL,
  `jeniskelamin` char(1) DEFAULT NULL,
  `tgllahir` date DEFAULT NULL,
  `agama` varchar(10) DEFAULT NULL,
  `kewarganegaraan` varchar(20) DEFAULT NULL,
  `statuskeluarga` varchar(20) DEFAULT NULL,
  `statustinggal` varchar(20) DEFAULT NULL,
  `alamat` varchar(50) DEFAULT NULL,
  `telepon` varchar(13) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`idwarga`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;