package com.example.activemind.interfaces

import com.example.activemind.dto.NfcCard

interface OnClickListener {
    fun onClick(nfcCard: NfcCard, position: Int)
}