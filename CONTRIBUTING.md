# Pedoman Kontribusi untuk Proyek Minda

Terima kasih atas minat Anda untuk berkontribusi pada proyek **Minda - Aplikasi Jurnal Harian**! Kontribusi Anda, baik dalam bentuk fitur baru, perbaikan *bug*, maupun dokumentasi, sangat dihargai.

Pedoman ini akan memandu Anda melalui proses kontribusi yang efisien.

---

## Melaporkan Bug dan Isu

Sebelum mengajukan *pull request* (PR), harap laporkan isu atau *bug* yang Anda temukan:

1.  Periksa [halaman Issues](${URL_REPOSITORY_ANDA}/issues) di repository ini untuk memastikan isu tersebut belum pernah dilaporkan.
2.  Jika belum ada, buat **Isu baru** dengan deskripsi yang jelas.
3.  Sertakan langkah-langkah untuk mereproduksi *bug*, versi Android Studio/perangkat, dan *screenshot* (jika relevan).

---

## Mengajukan Perubahan (Pull Request)

Untuk berkontribusi kode:

1.  **Fork** repository ini ke akun GitHub Anda.
2.  **Clone** *fork* Anda ke mesin lokal.
3.  Buat *branch* baru dengan nama yang deskriptif (misalnya, `fitur/tambah-kalender` atau `fix/perbaikan-crud`).
    ```bash
    git checkout -b <nama-branch-anda>
    ```
4.  Lakukan perubahan dan pastikan kode lolos proses *build* lokal dan tidak ada *lint warnings*.
5.  **Commit** perubahan Anda dengan pesan yang ringkas dan informatif.
    ```bash
    git commit -m "feat: Menambahkan fitur X pada layar Y"
    ```
6.  **Push** *branch* Anda ke *fork* di GitHub.
    ```bash
    git push origin <nama-branch-anda>
    ```
7.  Buka *fork* Anda di GitHub dan buat **Pull Request (PR)** ke *branch* utama (`main` atau `master`) dari repository ini.

### Catatan PR

* Pastikan PR Anda merujuk pada **Issues** yang terkait (jika ada).
* Jelaskan secara singkat apa yang diubah dan mengapa.

---

## Standar Kode

* Gunakan **Kotlin** dan ikuti panduan gaya kode standar Android (Kotlin Style Guide).
* Gunakan **Jetpack Compose** untuk UI.
* Pertahankan arsitektur **MVVM** yang sudah ada.
* Tambahkan komentar pada kode yang kompleks.

Terima kasih atas kontribusi Anda!
