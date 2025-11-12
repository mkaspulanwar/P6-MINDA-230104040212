package id.antasari.p6minda_230104040212.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Fungsi utilitas untuk memformat stempel waktu (timestamp) Long menjadi String tanggal dan waktu.
 * * @param ts Stempel waktu dalam milidetik (Long).
 * @return String tanggal dan waktu yang diformat (contoh: "12 Nov, 2025, 03:19 PM").
 */
fun formatTimestamp(ts: Long): String {
    // Membuat objek SimpleDateFormat dengan format yang diinginkan
    // "dd MMM, yyyy, hh:mm a" akan menghasilkan tanggal seperti: 01 Jan, 2023, 05:30 PM
    val sdf = SimpleDateFormat("dd MMM, yyyy, hh:mm a", Locale.getDefault())

    // Mengubah stempel waktu Long menjadi objek Date, lalu memformatnya
    return sdf.format(Date(ts))
}