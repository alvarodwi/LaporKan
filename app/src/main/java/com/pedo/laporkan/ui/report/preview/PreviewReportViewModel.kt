package com.pedo.laporkan.ui.report.preview

import android.app.Application
import android.os.Environment
import android.util.Log
import androidx.lifecycle.*
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.pedo.laporkan.data.model.StatusLaporan
import com.pedo.laporkan.data.repository.MainRepository
import com.pedo.laporkan.utils.Constants.DEFAULT_TAG
import com.pedo.laporkan.utils.Constants.SharedPrefKey.LOGGED_USER_NAME
import com.pedo.laporkan.utils.Helpers.indonesianLocale
import com.pedo.laporkan.utils.Helpers.laporKanDateFormat
import kotlinx.coroutines.Job
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PreviewReportViewModel(date: Date? = null, val app: Application) : AndroidViewModel(app) {
    private val repository = MainRepository.getInstance(app.applicationContext)

    private val viewModelJob = Job()

    private val listDate = getFirstAndLastDayOfDateMonth(date!!)
    lateinit var pdfFile: File

    val laporanReportData = repository.getLaporanReportByDate(
        listDate[0],
        listDate[1]
    )
    val petugasReportData = repository.getPetugasReportByDate(
        listDate[0],
        listDate[1]
    )
    val userReportData = repository.getUserReportByDate(
        listDate[0],
        listDate[1]
    )

    val judulReport = MutableLiveData<String>()
    val reportCreatorInfo = MutableLiveData<String>()
    val reportTanggalInfo = MutableLiveData<String>()

    private val reportCreatorName =
        repository.mainSharedPreferences.getString(LOGGED_USER_NAME, null)

    private val _openPdf = MutableLiveData<Boolean>()
    val openPdf: LiveData<Boolean>
        get() = _openPdf


    init {
        reportCreatorInfo.value = "Report ini dibuat oleh $reportCreatorName"

        val cal = Calendar.getInstance()
        cal.time = date!!
        judulReport.value = "${cal.getDisplayName(
            Calendar.MONTH,
            Calendar.LONG,
            indonesianLocale
        )} ${cal.get(Calendar.YEAR)}"

        val localDate = LocalDate.now()
        reportTanggalInfo.value = "Pada tanggal ${localDate.format(laporKanDateFormat)}"
    }

    private fun getFirstAndLastDayOfDateMonth(date: Date): List<String> {
        val dateArr = ArrayList<String>()

        val cal = Calendar.getInstance()
        cal.time = date

        Log.d(
            DEFAULT_TAG,
            "${cal.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                indonesianLocale
            )} ${cal.get(
                Calendar.YEAR
            )}"
        )

        val yearMonth = YearMonth.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1)
        val startOfMonth = yearMonth.atDay(1)
        val endOfMonth = yearMonth.atEndOfMonth()

        dateArr.add(startOfMonth.toString())
        dateArr.add(endOfMonth.toString())
        return dateArr
    }

    fun onCetakPdfClicked() {
        try {
            createPdf()
        } catch (e: IOException) {
            Log.e(DEFAULT_TAG, e.localizedMessage, e)
        }
    }

    private fun createPdf() {
        val dlFolder = app.applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            ?: return
        if (!dlFolder.exists()) {
            dlFolder.mkdir()
            Log.d(DEFAULT_TAG, "Buat folder baru!")
        }

        pdfFile = File(
            dlFolder.absolutePath, "LaporKan-Report-${SimpleDateFormat(
                "yyyyMMddHHmmss'.pdf'",
                indonesianLocale
            ).format(Date())}"
        )
        populatePdfContent(pdfFile)
        _openPdf.value = true
    }

    private fun populatePdfContent(pdfFile: File) {
        val document = Document()
        PdfWriter.getInstance(document, FileOutputStream(pdfFile))
        //open to write
        document.open()
        //document settings
        document.pageSize = PageSize.A4
        document.addCreationDate()
        document.addAuthor("LaporKan")
        document.addCreator(reportCreatorName)

        //document variable
        val mColorRed = BaseColor(208, 2, 27)
        val mHeadingFontSize = 32.0f
        val mContentFontSize = 22.0f
        val poppinsFont =
            BaseFont.createFont("assets/fonts/poppins.ttf", "UTF-8", BaseFont.EMBEDDED)
        val poppinsLightFont =
            BaseFont.createFont("assets/fonts/poppins_light.ttf","UTF-8",BaseFont.EMBEDDED)
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = mColorRed
        lineSeparator.lineWidth = 5f

        //the document
        //header,report bulanan
        val mReportBulananParagraph = Paragraph(
            Chunk(
                "Report Bulanan",
                Font(poppinsFont, mHeadingFontSize, Font.BOLD, BaseColor.BLACK)
            )
        )
        document.add(mReportBulananParagraph)
        //red title
        val mReportTitleParagraph = Paragraph(
            Chunk(
                judulReport.value,
                Font(poppinsFont, mHeadingFontSize, Font.BOLD, mColorRed)
            )
        )
        document.add(mReportTitleParagraph)
        //gray info
        val mGrayInfoTextFont = Font(poppinsFont,mContentFontSize,Font.NORMAL, BaseColor.GRAY)
        val mReportAuthorParagraph = Paragraph(
            Chunk(
                reportCreatorInfo.value,
                mGrayInfoTextFont
            )
        )
        val mReportDateParagraph = Paragraph(
            Chunk(
                reportTanggalInfo.value,
                mGrayInfoTextFont
            )
        )
        document.add(mReportAuthorParagraph)
        document.add(mReportDateParagraph)

        //separator
        document.add(Paragraph(""))
        document.add(Chunk(lineSeparator))
        document.add(Paragraph(""))

        val mContentHeaderFont = Font(poppinsFont,mContentFontSize,Font.BOLD, BaseColor.BLACK)
        val mContentFont = Font(poppinsLightFont,mContentFontSize,Font.NORMAL, BaseColor.BLACK)

        //statistik
        val statistikHeaderParagraph = Paragraph(
            Chunk(
                "Statistik",
                mContentHeaderFont
            )
        )
        document.add(statistikHeaderParagraph)

        //get laporan data
        laporanReportData.value?.let {
            var totalLaporan = 0
            var totalLaporanSelesai = 0
            var totalLaporanProses = 0
            var totalLaporanBaru = 0
            var totalLaporanGagal = 0

            for(laporanResponse in it){
                totalLaporan += laporanResponse.laporanCount
                when(laporanResponse.laporanStatus){
                    StatusLaporan.SELESAI -> {
                        totalLaporanSelesai = laporanResponse.laporanCount
                    }
                    StatusLaporan.PROSES -> {
                        totalLaporanProses = laporanResponse.laporanCount
                    }
                    StatusLaporan.BARU -> {
                        totalLaporanBaru = laporanResponse.laporanCount
                    }
                    StatusLaporan.GAGAL -> {
                        totalLaporanGagal = laporanResponse.laporanCount
                    }
                }
            }

            document.add(Chunk("Dari ", mContentFont))
            document.add(Chunk("$totalLaporan",mContentHeaderFont))
            document.add(Chunk(" laporan yang dibuat bulan ini",mContentFont))
            document.add(Paragraph(Chunk.NEWLINE))

            document.add(Chunk("   $totalLaporanSelesai",mContentHeaderFont))
            document.add(Chunk(" laporan selesai",mContentFont))
            document.add(Paragraph(Chunk.NEWLINE))

            document.add(Chunk("   $totalLaporanProses",mContentHeaderFont))
            document.add(Chunk(" laporan masih dalam proses",mContentFont))
            document.add(Paragraph(Chunk.NEWLINE))

            document.add(Chunk("   $totalLaporanBaru",mContentHeaderFont))
            document.add(Chunk(" laporan belum divalidasi",mContentFont))
            document.add(Paragraph(Chunk.NEWLINE))

            document.add(Chunk("   $totalLaporanGagal",mContentHeaderFont))
            document.add(Chunk(" laporan gagal validasi",mContentFont))
            document.add(Paragraph(Chunk.NEWLINE))
        }

        document.add(Paragraph(Chunk.NEWLINE))

        //kontributor
        val kontributorHeaderParagraph = Paragraph(Chunk("Kontributor", mContentHeaderFont))
        document.add(kontributorHeaderParagraph)

        //get kontributor data
        userReportData.value?.let {
            document.add(Chunk("${it.size}",mContentHeaderFont))
            document.add(Chunk(" masyarakat berkontribusi",mContentFont))
            document.add(Paragraph(Chunk.NEWLINE))
        }

        document.add(Paragraph(Chunk.NEWLINE))

        //respons
        val responsHeaderParagraph = Paragraph(Chunk("Respons", mContentHeaderFont))
        document.add(responsHeaderParagraph)

        //get kontributor data
        petugasReportData.value?.let {
            document.add(Chunk("${it.size}",mContentHeaderFont))
            document.add(Chunk(" petugas membuat respons tanggapan",mContentFont))
            document.add(Paragraph(Chunk.NEWLINE))
        }

        document.close()
    }

    fun resetOpenPdf() {
        _openPdf.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(private val date: Date, val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if (modelClass.isAssignableFrom(PreviewReportViewModel::class.java)) {
                return PreviewReportViewModel(
                    date,
                    app
                ) as T
            }
            throw IllegalArgumentException("Cannot construct viewmodel")
        }

    }
}