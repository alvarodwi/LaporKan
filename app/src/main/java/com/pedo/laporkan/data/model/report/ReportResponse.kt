package com.pedo.laporkan.data.model.report

import androidx.room.ColumnInfo
import com.pedo.laporkan.data.model.StatusLaporan

data class LaporanReportResponse(
    @ColumnInfo(name = "laporanCount") var laporanCount : Int,
    @ColumnInfo(name = "status") var laporanStatus : StatusLaporan
)

data class UserReportResponse(
    @ColumnInfo(name = "userCount") var userCount : Int,
    @ColumnInfo(name = "userId") var userId : String
)