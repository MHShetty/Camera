package app.grapheneos.camera

import androidxc.exifinterface.media.ExifInterface
import java.util.TimeZone
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


private val exifAttributes = arrayOf(
    ExifInterface.TAG_IMAGE_WIDTH,
    ExifInterface.TAG_IMAGE_LENGTH,
    ExifInterface.TAG_BITS_PER_SAMPLE,
    ExifInterface.TAG_COMPRESSION,
    ExifInterface.TAG_PHOTOMETRIC_INTERPRETATION,
    //ExifInterface.TAG_ORIENTATION,
    ExifInterface.TAG_SAMPLES_PER_PIXEL,
    ExifInterface.TAG_PLANAR_CONFIGURATION,
    ExifInterface.TAG_Y_CB_CR_SUB_SAMPLING,
    ExifInterface.TAG_Y_CB_CR_POSITIONING,
    ExifInterface.TAG_X_RESOLUTION,
    ExifInterface.TAG_Y_RESOLUTION,
    ExifInterface.TAG_RESOLUTION_UNIT,
    ExifInterface.TAG_STRIP_OFFSETS,
    ExifInterface.TAG_ROWS_PER_STRIP,
    ExifInterface.TAG_STRIP_BYTE_COUNTS,
    ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT,
    ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH,
    ExifInterface.TAG_TRANSFER_FUNCTION,
    ExifInterface.TAG_WHITE_POINT,
    ExifInterface.TAG_PRIMARY_CHROMATICITIES,
    ExifInterface.TAG_Y_CB_CR_COEFFICIENTS,
    ExifInterface.TAG_REFERENCE_BLACK_WHITE,
    ExifInterface.TAG_DATETIME,
    ExifInterface.TAG_IMAGE_DESCRIPTION,
    ExifInterface.TAG_MAKE,
    ExifInterface.TAG_MODEL,
    ExifInterface.TAG_SOFTWARE,
    ExifInterface.TAG_ARTIST,
    ExifInterface.TAG_COPYRIGHT,
    ExifInterface.TAG_EXIF_VERSION,
    ExifInterface.TAG_FLASHPIX_VERSION,
    ExifInterface.TAG_COLOR_SPACE,
    ExifInterface.TAG_GAMMA,
    ExifInterface.TAG_PIXEL_X_DIMENSION,
    ExifInterface.TAG_PIXEL_Y_DIMENSION,
    ExifInterface.TAG_COMPONENTS_CONFIGURATION,
    ExifInterface.TAG_COMPRESSED_BITS_PER_PIXEL,
    ExifInterface.TAG_MAKER_NOTE,
    ExifInterface.TAG_USER_COMMENT,
    ExifInterface.TAG_RELATED_SOUND_FILE,
    ExifInterface.TAG_DATETIME_ORIGINAL,
    ExifInterface.TAG_DATETIME_DIGITIZED,
    ExifInterface.TAG_OFFSET_TIME,
    ExifInterface.TAG_OFFSET_TIME_ORIGINAL,
    ExifInterface.TAG_OFFSET_TIME_DIGITIZED,
    ExifInterface.TAG_SUBSEC_TIME,
    ExifInterface.TAG_SUBSEC_TIME_ORIGINAL,
    ExifInterface.TAG_SUBSEC_TIME_DIGITIZED,
    ExifInterface.TAG_EXPOSURE_TIME,
    ExifInterface.TAG_F_NUMBER,
    ExifInterface.TAG_EXPOSURE_PROGRAM,
    ExifInterface.TAG_SPECTRAL_SENSITIVITY,
    ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY,
    ExifInterface.TAG_OECF,
    ExifInterface.TAG_SENSITIVITY_TYPE,
    ExifInterface.TAG_STANDARD_OUTPUT_SENSITIVITY,
    ExifInterface.TAG_RECOMMENDED_EXPOSURE_INDEX,
    ExifInterface.TAG_ISO_SPEED,
    ExifInterface.TAG_ISO_SPEED_LATITUDE_YYY,
    ExifInterface.TAG_ISO_SPEED_LATITUDE_ZZZ,
    ExifInterface.TAG_SHUTTER_SPEED_VALUE,
    ExifInterface.TAG_APERTURE_VALUE,
    ExifInterface.TAG_BRIGHTNESS_VALUE,
    ExifInterface.TAG_EXPOSURE_BIAS_VALUE,
    ExifInterface.TAG_MAX_APERTURE_VALUE,
    ExifInterface.TAG_SUBJECT_DISTANCE,
    ExifInterface.TAG_METERING_MODE,
    ExifInterface.TAG_LIGHT_SOURCE,
    ExifInterface.TAG_FLASH,
    ExifInterface.TAG_SUBJECT_AREA,
    ExifInterface.TAG_FOCAL_LENGTH,
    ExifInterface.TAG_FLASH_ENERGY,
    ExifInterface.TAG_SPATIAL_FREQUENCY_RESPONSE,
    ExifInterface.TAG_FOCAL_PLANE_X_RESOLUTION,
    ExifInterface.TAG_FOCAL_PLANE_Y_RESOLUTION,
    ExifInterface.TAG_FOCAL_PLANE_RESOLUTION_UNIT,
    ExifInterface.TAG_SUBJECT_LOCATION,
    ExifInterface.TAG_EXPOSURE_INDEX,
    ExifInterface.TAG_SENSING_METHOD,
    ExifInterface.TAG_FILE_SOURCE,
    ExifInterface.TAG_SCENE_TYPE,
    ExifInterface.TAG_CFA_PATTERN,
    ExifInterface.TAG_CUSTOM_RENDERED,
    ExifInterface.TAG_EXPOSURE_MODE,
    ExifInterface.TAG_WHITE_BALANCE,
    ExifInterface.TAG_DIGITAL_ZOOM_RATIO,
    ExifInterface.TAG_FOCAL_LENGTH_IN_35MM_FILM,
    ExifInterface.TAG_SCENE_CAPTURE_TYPE,
    ExifInterface.TAG_GAIN_CONTROL,
    ExifInterface.TAG_CONTRAST,
    ExifInterface.TAG_SATURATION,
    ExifInterface.TAG_SHARPNESS,
    ExifInterface.TAG_DEVICE_SETTING_DESCRIPTION,
    ExifInterface.TAG_SUBJECT_DISTANCE_RANGE,
    ExifInterface.TAG_IMAGE_UNIQUE_ID,
    ExifInterface.TAG_CAMERA_OWNER_NAME,
    ExifInterface.TAG_BODY_SERIAL_NUMBER,
    ExifInterface.TAG_LENS_SPECIFICATION,
    ExifInterface.TAG_LENS_MAKE,
    ExifInterface.TAG_LENS_MODEL,
    ExifInterface.TAG_LENS_SERIAL_NUMBER,
    ExifInterface.TAG_GPS_VERSION_ID,
    ExifInterface.TAG_GPS_LATITUDE_REF,
    ExifInterface.TAG_GPS_LATITUDE,
    ExifInterface.TAG_GPS_LONGITUDE_REF,
    ExifInterface.TAG_GPS_LONGITUDE,
    ExifInterface.TAG_GPS_ALTITUDE_REF,
    ExifInterface.TAG_GPS_ALTITUDE,
    ExifInterface.TAG_GPS_TIMESTAMP,
    ExifInterface.TAG_GPS_SATELLITES,
    ExifInterface.TAG_GPS_STATUS,
    ExifInterface.TAG_GPS_MEASURE_MODE,
    ExifInterface.TAG_GPS_DOP,
    ExifInterface.TAG_GPS_SPEED_REF,
    ExifInterface.TAG_GPS_SPEED,
    ExifInterface.TAG_GPS_TRACK_REF,
    ExifInterface.TAG_GPS_TRACK,
    ExifInterface.TAG_GPS_IMG_DIRECTION_REF,
    ExifInterface.TAG_GPS_IMG_DIRECTION,
    ExifInterface.TAG_GPS_MAP_DATUM,
    ExifInterface.TAG_GPS_DEST_LATITUDE_REF,
    ExifInterface.TAG_GPS_DEST_LATITUDE,
    ExifInterface.TAG_GPS_DEST_LONGITUDE_REF,
    ExifInterface.TAG_GPS_DEST_LONGITUDE,
    ExifInterface.TAG_GPS_DEST_BEARING_REF,
    ExifInterface.TAG_GPS_DEST_BEARING,
    ExifInterface.TAG_GPS_DEST_DISTANCE_REF,
    ExifInterface.TAG_GPS_DEST_DISTANCE,
    ExifInterface.TAG_GPS_PROCESSING_METHOD,
    ExifInterface.TAG_GPS_AREA_INFORMATION,
    ExifInterface.TAG_GPS_DATESTAMP,
    ExifInterface.TAG_GPS_DIFFERENTIAL,
    ExifInterface.TAG_GPS_H_POSITIONING_ERROR,
    ExifInterface.TAG_INTEROPERABILITY_INDEX,
    ExifInterface.TAG_THUMBNAIL_IMAGE_LENGTH,
    ExifInterface.TAG_THUMBNAIL_IMAGE_WIDTH,
    //TAG_THUMBNAIL_ORIENTATION,
    ExifInterface.TAG_DNG_VERSION,
    ExifInterface.TAG_DEFAULT_CROP_SIZE,
    ExifInterface.TAG_ORF_THUMBNAIL_IMAGE,
    ExifInterface.TAG_ORF_PREVIEW_IMAGE_START,
    ExifInterface.TAG_ORF_PREVIEW_IMAGE_LENGTH,
    ExifInterface.TAG_ORF_ASPECT_FRAME,
    ExifInterface.TAG_RW2_SENSOR_BOTTOM_BORDER,
    ExifInterface.TAG_RW2_SENSOR_LEFT_BORDER,
    ExifInterface.TAG_RW2_SENSOR_RIGHT_BORDER,
    ExifInterface.TAG_RW2_SENSOR_TOP_BORDER,
    ExifInterface.TAG_RW2_ISO,
    ExifInterface.TAG_RW2_JPG_FROM_RAW,
    ExifInterface.TAG_XMP,
    ExifInterface.TAG_NEW_SUBFILE_TYPE,
    ExifInterface.TAG_SUBFILE_TYPE,
)

fun ExifInterface.fixExif(captureTime: Date) {
    val millis = TimeZone.getDefault().getOffset(captureTime.time)

    val totalMins = (millis / (1000 * 60))

    val hours = (totalMins / 60)
    var hoursStrRep = hours.toString().padStart(2, '0')

    if (hours >= 0)
        hoursStrRep = "+${hoursStrRep}"

    val mins = (totalMins % 60).toString().padEnd(2, '0')

    val offsetTime = "$hoursStrRep:$mins"

    setAttribute(ExifInterface.TAG_OFFSET_TIME, offsetTime)
    setAttribute(ExifInterface.TAG_OFFSET_TIME_ORIGINAL, offsetTime)
//    exifInterface.setAttribute(ExifInterface.TAG_OFFSET_TIME_DIGITIZED, offset_time)

    val nowStrRep = SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US).format(captureTime)

    setAttribute(ExifInterface.TAG_DATETIME_ORIGINAL, nowStrRep)
    setAttribute(ExifInterface.TAG_DATETIME, nowStrRep)
}

fun ExifInterface.clearExif() {
    for (exifTag in exifAttributes) {
        removeAttribute(exifTag)
    }
}

// TODO: (Re-)use this code later to implement custom EXIF removal setting
//fun clearExif(context: Context, uri : Uri) {
//
//    val inStream = context.contentResolver.openFileDescriptor(
//        uri,
//        "rw"
//    )!!
//
//    val exifInterface = ExifInterface(inStream.fileDescriptor)
//
//    // Last modified
//    exifInterface.removeAttribute(ExifInterface.TAG_DATETIME) // original
//    exifInterface.removeAttribute(ExifInterface.TAG_DATETIME_DIGITIZED) // digitized
//    // Created on
//    exifInterface.removeAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)
//
//    // Camera/Device Make - Manufacturer (for e.g. Google)
//    exifInterface.removeAttribute(ExifInterface.TAG_MAKE)
//
//    // Camera/Device Model - Model (for e.g. Pixel 4a)
//    exifInterface.removeAttribute(ExifInterface.TAG_MODEL)
//
//    // Focal Length
//    exifInterface.removeAttribute(ExifInterface.TAG_FOCAL_LENGTH)
//    exifInterface.removeAttribute(ExifInterface.TAG_FOCAL_LENGTH_IN_35MM_FILM)
//    exifInterface.removeAttribute(ExifInterface.TAG_FOCAL_PLANE_RESOLUTION_UNIT)
//    exifInterface.removeAttribute(ExifInterface.TAG_FOCAL_PLANE_X_RESOLUTION)
//    exifInterface.removeAttribute(ExifInterface.TAG_FOCAL_PLANE_Y_RESOLUTION)
//
//    // Exposure Time
//    exifInterface.removeAttribute(ExifInterface.TAG_EXPOSURE_TIME)
//
//    // Aperture
//    exifInterface.removeAttribute(ExifInterface.TAG_MAX_APERTURE_VALUE)
//
//    // White balance
//    exifInterface.removeAttribute(ExifInterface.TAG_WHITE_BALANCE)
//
//    // Metering mode
//    exifInterface.removeAttribute(ExifInterface.TAG_METERING_MODE)
//
//    // ISO equiv.
//    exifInterface.removeAttribute(ExifInterface.TAG_RW2_ISO)
//
//    // Flash used
//    exifInterface.removeAttribute(ExifInterface.TAG_FLASH)
//
//    // Color space
//    exifInterface.removeAttribute(ExifInterface.TAG_COLOR_SPACE)
//
//    // Color space
//    exifInterface.removeAttribute(ExifInterface.TAG_SCENE_TYPE)
//
//    exifInterface.saveAttributes()
//}

fun ExifInterface.removeAttribute(tag: String) {
    setAttribute(tag, null)
}
