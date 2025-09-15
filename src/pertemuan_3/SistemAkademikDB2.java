package pertemuan_3;

import java.sql.*;
import java.util.Scanner;

public class SistemAkademikDB2 {

    static Scanner scan = new Scanner(System.in);

    // ====== Jurusan ======
    public static void kelolaJurusan() {
        System.out.println("1. Tambah Jurusan\n2. Lihat Jurusan\n3. Hapus Jurusan");
        int pilih = scan.nextInt();
        scan.nextLine();

        try (Connection conn = Database.connect()) {
            if (pilih == 1) {
                System.out.print("Kode Jurusan: ");
                String kode = scan.nextLine();
                System.out.print("Nama Jurusan: ");
                String nama = scan.nextLine();

                String sql = "INSERT INTO jurusan (kode_jurusan, nama) VALUES (?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, kode);
                ps.setString(2, nama);
                ps.executeUpdate();
                System.out.println("✅ Jurusan berhasil ditambahkan!");
            } else if (pilih == 2) {
                String sql = "SELECT * FROM jurusan";
                ResultSet rs = conn.createStatement().executeQuery(sql);
                while (rs.next()) {
                    System.out.println(rs.getString("kode_jurusan") + " - " + rs.getString("nama"));
                }
            } else if (pilih == 3) {
                System.out.print("Masukkan kode jurusan yang ingin dihapus: ");
                String kode = scan.nextLine();

                String sql = "DELETE FROM jurusan WHERE kode_jurusan = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, kode);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("✅ Jurusan berhasil dihapus (beserta data terkait).");
                } else {
                    System.out.println("❌ Jurusan tidak ditemukan.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ====== Mata Kuliah ======
    public static void kelolaMataKuliah() {
        System.out.print("Masukkan kode jurusan: ");
        String kodeJurusan = scan.nextLine();

        try (Connection conn = Database.connect()) {
            // cek jurusan
            String cekJur = "SELECT * FROM jurusan WHERE kode_jurusan=?";
            PreparedStatement psCek = conn.prepareStatement(cekJur);
            psCek.setString(1, kodeJurusan);
            ResultSet rsCek = psCek.executeQuery();
            if (!rsCek.next()) {
                System.out.println("❌ Jurusan tidak ditemukan!");
                return;
            }

            System.out.println("1. Tambah MK\n2. Hapus MK\n3. Lihat MK");
            int pilih = scan.nextInt();
            scan.nextLine();

            if (pilih == 1) {
                System.out.print("Kode MK: ");
                String kode = scan.nextLine();
                System.out.print("Nama MK: ");
                String nama = scan.nextLine();
                System.out.print("SKS: ");
                int sks = scan.nextInt();
                scan.nextLine();

                String sql = "INSERT INTO matakuliah (kode_matakuliah, nama, sks, kode_jurusan) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, kode);
                ps.setString(2, nama);
                ps.setInt(3, sks);
                ps.setString(4, kodeJurusan);
                ps.executeUpdate();
                System.out.println("✅ Mata kuliah berhasil ditambahkan!");
            } else if (pilih == 2) {
                System.out.print("Kode MK yang dihapus: ");
                String kode = scan.nextLine();
                String sql = "DELETE FROM matakuliah WHERE kode_matakuliah=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, kode);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("✅ Mata kuliah berhasil dihapus.");
                } else {
                    System.out.println("❌ Mata kuliah tidak ditemukan.");
                }
            } else if (pilih == 3) {
                String sql = "SELECT * FROM matakuliah WHERE kode_jurusan=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, kodeJurusan);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    System.out.println(rs.getString("kode_matakuliah") + " - " + rs.getString("nama") + " (" + rs.getInt("sks") + " SKS)");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ====== Mahasiswa ======
    public static void kelolaMahasiswa() {
        System.out.println("1. Tambah Mahasiswa\n2. Lihat Mahasiswa\n3. Detail Mahasiswa");
        int pilih = scan.nextInt();
        scan.nextLine();

        try (Connection conn = Database.connect()) {
            if (pilih == 1) {
                System.out.print("NIM: ");
                String nim = scan.nextLine();
                System.out.print("Nama: ");
                String nama = scan.nextLine();
                System.out.print("Kode Jurusan: ");
                String kodeJurusan = scan.nextLine();

                String sql = "INSERT INTO mahasiswa (nim, nama, kode_jurusan) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nim);
                ps.setString(2, nama);
                ps.setString(3, kodeJurusan);
                ps.executeUpdate();
                System.out.println("✅ Mahasiswa berhasil ditambahkan!");
            } else if (pilih == 2) {
                String sql = "SELECT m.nim, m.nama, j.nama AS jurusan FROM mahasiswa m JOIN jurusan j ON m.kode_jurusan=j.kode_jurusan";
                ResultSet rs = conn.createStatement().executeQuery(sql);
                while (rs.next()) {
                    System.out.println(rs.getString("nim") + " - " + rs.getString("nama") + " (" + rs.getString("jurusan") + ")");
                }
            } else if (pilih == 3) {
                System.out.print("Masukkan NIM Mahasiswa: ");
                String nim = scan.nextLine();

                String sql = "SELECT m.nim, m.nama, j.nama AS jurusan FROM mahasiswa m JOIN jurusan j ON m.kode_jurusan=j.kode_jurusan WHERE m.nim=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nim);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.println("Detail: " + rs.getString("nim") + " - " + rs.getString("nama") + " (" + rs.getString("jurusan") + ")");
                    System.out.println("Nilai:");
                    String sqlNilai = "SELECT mk.nama, mm.indeks_nilai FROM mahasiswa_matakuliah mm JOIN matakuliah mk ON mm.kode_matakuliah=mk.kode_matakuliah WHERE mm.nim=?";
                    PreparedStatement psN = conn.prepareStatement(sqlNilai);
                    psN.setString(1, nim);
                    ResultSet rsN = psN.executeQuery();
                    while (rsN.next()) {
                        System.out.println(rsN.getString("nama") + " : " + rsN.getString("indeks_nilai"));
                    }
                } else {
                    System.out.println("❌ Mahasiswa tidak ditemukan.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ====== Beri Nilai ======
    public static void beriNilai() {
        System.out.print("Masukkan NIM mahasiswa: ");
        String nim = scan.nextLine();

        try (Connection conn = Database.connect()) {
            // cek mahasiswa
            String cekMhs = "SELECT * FROM mahasiswa WHERE nim=?";
            PreparedStatement psCek = conn.prepareStatement(cekMhs);
            psCek.setString(1, nim);
            ResultSet rsM = psCek.executeQuery();
            if (!rsM.next()) {
                System.out.println("❌ Mahasiswa tidak ditemukan!");
                return;
            }

            System.out.print("Masukkan kode mata kuliah: ");
            String kode = scan.nextLine();

            String cekMk = "SELECT * FROM matakuliah WHERE kode_matakuliah=?";
            PreparedStatement psMk = conn.prepareStatement(cekMk);
            psMk.setString(1, kode);
            ResultSet rsMk = psMk.executeQuery();
            if (!rsMk.next()) {
                System.out.println("❌ Mata kuliah tidak ditemukan!");
                return;
            }

            System.out.print("Indeks Nilai (A/B/C/D/E): ");
            String indeks = scan.nextLine().toUpperCase();

            String sql = "INSERT INTO mahasiswa_matakuliah (nim, kode_matakuliah, indeks_nilai) VALUES (?, ?, ?) "
                    + "ON CONFLICT (nim, kode_matakuliah) DO UPDATE SET indeks_nilai=EXCLUDED.indeks_nilai";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nim);
            ps.setString(2, kode);
            ps.setString(3, indeks);
            ps.executeUpdate();
            System.out.println("✅ Nilai berhasil disimpan.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ====== Hitung IP ======
    public static void hitungIP() {
        System.out.print("Masukkan NIM mahasiswa: ");
        String nim = scan.nextLine();

        try (Connection conn = Database.connect()) {
            String cekSql = "SELECT nama FROM mahasiswa WHERE nim = ?";
            PreparedStatement cekPs = conn.prepareStatement(cekSql);
            cekPs.setString(1, nim);
            ResultSet cekRs = cekPs.executeQuery();

            if (!cekRs.next()) {
                System.out.println("❌ Mahasiswa dengan NIM " + nim + " tidak ditemukan.");
                return;
            }

            String nama = cekRs.getString("nama");

            // ambil data nilai & SKS
            String sql = "SELECT mk.sks, mm.indeks_nilai "
                    + "FROM mahasiswa_matakuliah mm "
                    + "JOIN matakuliah mk ON mm.kode_matakuliah = mk.kode_matakuliah "
                    + "WHERE mm.nim = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nim);
            ResultSet rs = ps.executeQuery();

            int totalBobot = 0, totalSks = 0;
            while (rs.next()) {
                String indeks = rs.getString("indeks_nilai");
                int sks = rs.getInt("sks");

                int bobot = switch (indeks) {
                    case "A" -> 4;
                    case "B" -> 3;
                    case "C" -> 2;
                    case "D" -> 1;
                    case "E" -> 0;
                    default -> 0;
                };

                totalBobot += bobot * sks;
                totalSks += sks;
            }

            if (totalSks == 0) {
                System.out.println("⚠️ Mahasiswa " + nama + " (" + nim + ") belum punya nilai.");
            } else {
                double ip = (double) totalBobot / totalSks;
                System.out.println("✅ IP Mahasiswa " + nama + " (" + nim + ") = " + ip);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ====== Main Menu ======
    public static void main(String[] args) {
        while (true) {
            System.out.println("\nMenu Sistem Akademik:");
            System.out.println("1. Kelola Jurusan");
            System.out.println("2. Kelola Mata Kuliah");
            System.out.println("3. Kelola Mahasiswa");
            System.out.println("4. Beri Nilai");
            System.out.println("5. Hitung IP");
            System.out.println("0. Keluar");
            System.out.print("Pilih: ");
            int pilih = scan.nextInt();
            scan.nextLine();
            switch (pilih) {
                case 1 -> kelolaJurusan();
                case 2 -> kelolaMataKuliah();
                case 3 -> kelolaMahasiswa();
                case 4 -> beriNilai();
                case 5 -> hitungIP();
                case 0 -> System.exit(0);
                default -> System.out.println("❌ Pilihan tidak valid");
            }
        }
    }
}
