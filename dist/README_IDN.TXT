JGetQuakeInfo
Versi 1.1-alpha
ReadMe (Indonesia)

----------------------------
Catatan Perubahan
----------------------------
JGetQuakeInfo ver. 1.1-alpha (Juni 2017)
- Desain UI Terbaru (Windows, Windows Classic, Metal, Nimbus, CDE/Motif)
- Resource dioptimalkan
- Alat Baru: Reset Pengaturan (JGQIReset.jar)
- Pembacaan XML dioptimalkan (BMKG)
- Ikon-ikon baru
- Kompatibilitas dengan Sistem Operasi Lain (Linux)
- Metode Debugger dioptimalkan
- Dihilangkan (sementara): Sembunyikan batang informasi

GetQuakeInfo ver 1.0 (Februari 2014)
- Rilis Pertama

----------------------------
Fitur-fitur
----------------------------
- Dapat berjalan di platform yang berbeda (Windows, Linux, Mac)
- Mencari dan Mendapatkan Informasi Gempa Lokal dari BMKG
- Memutar suara ketika terdapat kekuatan gempa yang besar
- Ikon pada System Tray (Jika sistem operasi Anda tidak mendukung ini, maka secara otomatis dipindahkan menuju Jendela Utama)
- Mendapatkan informasi gempa secara otomatis dengan rentang waktu tertentu (tergantung pengguna, Anda juga dapat mencari informasi gempa secara manual)
- Batang Informasi (Jika Anda lupa kapan gempa baru terjadi)

----------------------------
Kebutuhan Minimal Sistem
----------------------------
Processor: Intel Pentium 4 1.0 GHz atau lebih tinggi
RAM: 256 MB
HDD: 5 MB
Resolusi: 800 x 600

Tambahan:
- Koneksi Internet
- JRE/JDK 7 atau lebih baru

----------------------------
Paduan Penggunaan
----------------------------
1. Menjalankan program
	- Anda dapat mengklik dua kali JGetQuakeInfo.jar
	- Menggunakan CMD/Terminal:
		java -jar "JGetQuakeInfo.jar"
		
2. Mencari informasi terbaru
	- Apabila sistem operasi Anda mendukung System Tray:
		Klik kanan pada ikon tray JGetQuakeInfo > Gempa > Cek informasi gempa terbaru...
	- Apabila sistem operasi Anda tidak mendukung System Tray:
		a. Dari Jendela Utama:
			Klik "PERBARUI INFO"
		b. Dari Batang Informasi
			Klik kanan > Gempa > Cek informasi gempa terbaru...
			
3. Mendapat Informasi Terakhir
	- Apabila sistem operasi Anda mendukung System Tray:
		Klik kanan pada ikon tray JGetQuakeInfo > Gempa > Lihat informasi terakhir
	- Apabila sistem operasi Anda tidak mendukung System Tray:
		a. Dari Jendela Utama:
			Klik "GEMPA TERAKHIR"
		b. Dari Batang Informasi
			Klik kanan > Gempa > Lihat informasi terakhir

4. Mengkustomisasi UI
	- Apabila sistem operasi Anda mendukung System Tray:
		a. Klik kanan ikon tray JGetQuakeInfo > Preferensi
		b. Pilih tab "Umum"
		c. Di bagian "Desain UI", pilih desain mana yang Anda inginkan untuk diterapkan ke JGetQuakeInfo
		d. Klik "Terapkan" lalu "OK"
		e. Jalankan ulang aplikasi
	- Apabila sistem operasi Anda tidak mendukung System Tray:
		a. Dari Jendela Utama:
			a. Klik "PREFERENSI"
			b. Pilih tab "Umum"
			c. Di bagian "Desain UI", pilih desain mana yang Anda inginkan untuk diterapkan ke JGetQuakeInfo
			d. Klik "Terapkan" lalu "OK"
			e. Jalankan ulang aplikasi
		b. Dari Batang Informasi:
			a. Klik kanan > Preferensi
			b. Pilih tab "Umum"
			c. Di bagian "Desain UI", pilih desain mana yang Anda inginkan untuk diterapkan ke JGetQuakeInfo
			d. Klik "Terapkan" lalu "OK"
			e. Jalankan ulang aplikasi

----------------------------
Pemecahan Masalah
----------------------------
- Apabila Pengaturan JGetQuakeInfo bermasalah
	Buka "JGQIReset.jar" lalu ikuti instruksi yang ada pada jendela
- Apabila Anda menemukan sebuah kesalahan (error)
	Screenshot, simpan file log nya dan kirim semua file-filenya (screenshot dan file log) dan jelaskan kapan dan bagaimana error tersebut terjadi.