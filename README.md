cat << 'EOF' > README.md
# 📝 Minda - Aplikasi Jurnal Harian (Mobile Programming Week 6)

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-Compose-blueviolet?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin Badge">
  <img src="https://img.shields.io/badge/Android-Studio-green?style=for-the-badge&logo=android&logoColor=white" alt="Android Studio Badge">
  <img src="https://img.shields.io/badge/Database-Room%20(SQLite)-red?style=for-the-badge&logo=sqlite&logoColor=white" alt="Room SQLite Badge">
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-00CED1?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose Badge">
  <img src="https://img.shields.io/badge/State%20Mgmt-MVVM-orange?style=for-the-badge&logo=android&logoColor=white" alt="MVVM Architecture Badge">
</p>

---

## 🌟 Deskripsi Project

Project ini merupakan implementasi dari **Modul Praktikum Mobile Programming Week 6** yang berfokus pada penggunaan **Database Lokal (SQLite - Room ORM)** dan **Jetpack Compose** untuk membangun aplikasi jurnal harian bernama **"Minda"**.

**Minda** dirancang sebagai aplikasi yang **100% *offline*** dan **privasi-sentris**, di mana semua data catatan harian disimpan secara aman menggunakan **Room Persistence Library** di perangkat pengguna. Tujuannya adalah untuk mendemonstrasikan kemampuan mahasiswa dalam:

1.  Menerapkan arsitektur modern (MVVM).
2.  Mengelola data relasional (jurnal) menggunakan Room (CRUD).
3.  Mengelola preferensi non-relasional (nama pengguna) menggunakan DataStore.
4.  Membangun alur *onboarding* dinamis.

---

## 🛠️ Detail Teknis Implementasi

Aplikasi ini mengadopsi arsitektur **MVVM** (Model-View-ViewModel) dan menggunakan **Repository Pattern** untuk memisahkan sumber data dari logika UI, memastikan kode yang lebih bersih dan teruji.

### 1. Database Persistence (Room)

* **Entity:** Membuat kelas data **`Jurnal`** sebagai representasi tabel di SQLite.
* **DAO (Data Access Object):** Mendefinisikan *interface* dengan fungsi-fungsi **CRUD** (`insert`, `getAll`, `update`, `delete`) yang mengembalikan `Flow` untuk *real-time updates* pada Compose UI.
* **Database:** Menggunakan anotasi `@Database` untuk mengkonfigurasi database Room.
* **Repository:** Menghubungkan ViewModel dengan DAO, menyediakan abstraksi untuk akses data.

### 2. Penyimpanan Preferensi (DataStore)

* **DataStore:** Digunakan untuk menyimpan data sederhana, seperti:
    * Status **`ONBOARDING_COMPLETED`** (Boolean).
    * **`USERNAME`** (String) untuk personalisasi sapaan.
* Penyimpanan data ini dilakukan secara **asinkron** dan **aman *type-safe***.

### 3. Arsitektur UI (Jetpack Compose & Navigation)

* **Start Screen Dinamis:** Fungsi utama di `MainActivity` memeriksa status `ONBOARDING_COMPLETED` dari DataStore untuk menentukan *start destination* di `NavHost`:
    * `true`: Langsung ke **`HomeScreen`**.
    * `false`: Mulai dari **`OnboardingScreen`**.
* **UI Reaktif:** UI yang dibangun dengan Compose secara otomatis diperbarui ketika ada perubahan pada data yang berasal dari `Flow` Room.

---

## 📦 Dependensi Kunci

Berikut adalah dependensi utama yang digunakan dalam project ini:

| Kategori | Dependensi | Deskripsi |
| :--- | :--- | :--- |
| **Database** | `androidx.room:room-ktx` | Core Room Persistence Library. |
| | `androidx.room:room-compiler` | Kapt/KSP untuk menghasilkan kode Room. |
| **DataStore** | `androidx.datastore:datastore-preferences` | Untuk menyimpan preferensi pengguna (nama, status onboarding). |
| **UI/Navigasi** | `androidx.compose.ui:ui` | Jetpack Compose core. |
| | `androidx.navigation:navigation-compose` | Navigasi untuk Jetpack Compose. |
| **Coroutine** | `org.jetbrains.kotlinx:kotlinx-coroutines-core` | Digunakan secara ekstensif untuk operasi I/O asinkron (Room, DataStore). |

---

## 🚀 Fitur Utama Aplikasi

1.  **Alur Onboarding Multi-Step:** Panduan pengguna saat pertama kali membuka aplikasi:
    * *Welcome Screen*.
    * *Input Nama* untuk personalisasi.
    * *Hello Screen* dengan nama yang sudah disimpan di DataStore.
2.  **Manajemen Jurnal (CRUD):**
    * `Create`: Menambah catatan baru.
    * `Read`: Menampilkan daftar dan detail catatan.
    * `Update`: Mengubah isi catatan yang sudah ada.
    * `Delete`: Menghapus catatan.
3.  **Personalisasi:** Sapaan **"Hi, <Nama>"** di Home Screen menggunakan data yang diambil dari DataStore.
4.  **Komponen Navigasi Modern:** Penggunaan **Floating Action Button (FAB)** dan **Bottom Navigation Bar** yang hanya muncul di tab-tab utama.

---

## 📷 Tampilan Aplikasi

Berikut adalah urutan tampilan selama alur *onboarding* dan layar utama:

| Alur Onboarding (1-4) | Home Screen | Calendar Screen |
| :---: | :---: | :---: |
|  |  |  |
| *Status Onboarding dikontrol oleh DataStore* | *Menampilkan daftar jurnal (Read)* | *Menampilkan kalender* |

---

## 👨‍💻 Kontributor

* **Dosen Pengampu:** Muhayat, M.IT
* **Project Developer:** [Ganti dengan Nama Anda]

---

## 💡 Cara Menjalankan Project

1.  **Clone Repository:**
    ```bash
    git clone [URL_REPOSITORY_ANDA]
    ```
2.  **Buka di Android Studio:** Gunakan versi Android Studio terbaru yang mendukung Jetpack Compose dan Kotlin.
3.  **Build dan Run:** Jalankan proyek pada emulator atau perangkat fisik Android (Minimum API Level 24+).
EOF
