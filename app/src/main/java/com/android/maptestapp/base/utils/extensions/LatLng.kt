package com.android.maptestapp.base.utils.extensions

import com.google.android.gms.maps.model.LatLng

val LatLng.toTomTom : com.tomtom.online.sdk.common.location.LatLng get() {
    return com.tomtom.online.sdk.common.location.LatLng(this.latitude, this.longitude)
}
val com.tomtom.online.sdk.common.location.LatLng.toGoogle : LatLng get() {
    return LatLng(this.latitude, this.longitude)
}