package com.example.paymentqrscanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class PaymentActivity : AppCompatActivity() {
    lateinit var qrCodeResult: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val qrCode = intent.getStringExtra("qrCodeResult") ?: "No QR code detected."
        qrCodeResult = qrCode
        val imeProdavca = extractValue(qrCodeResult, "N:", "|")
        val racunProdavca = extractValue(qrCodeResult, "R:", "|")
        val modelIPozivNaBroj = extractValue(qrCodeResult, "RO:", "|")
        val valutaIIznos = extractValue(qrCodeResult, "I:", "|")
        val sifraPlacanja = extractValue(qrCodeResult, "SF:", "|")
        val svhraPlacanja = extractValue(qrCodeResult, "S:", "|")
        setContent {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val context : Context = LocalContext.current
                Column(Modifier.padding(4.dp).align(Alignment.Center)){
                    BasicText(text = "Nalog za placanje", Modifier.padding(14.dp))
                    TextField(
                        value = imeProdavca,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        enabled = false,
                        label = { androidx.compose.material3.Text("Naziv primaoca") }
                    )
                    TextField(
                        value = racunProdavca,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        enabled = false,
                        label = { androidx.compose.material3.Text("Broj računa primaoca") }
                    )
                    TextField(
                        value = modelIPozivNaBroj.substring(0,2),
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        enabled = false,
                        label = { androidx.compose.material3.Text("Model") }
                    )
                    TextField(
                        value = modelIPozivNaBroj.substring(2),
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        enabled = false,
                        label = { androidx.compose.material3.Text("Poziv na broj") }
                    )
                    Row{
                        TextField(
                            value = valutaIIznos.substring(3),
                            modifier = Modifier.padding(4.dp),
                            onValueChange = {},
                            enabled = false,
                            label = { androidx.compose.material3.Text("Iznos") }
                        )
                        TextField(
                            value = valutaIIznos.substring(0,3),
                            modifier = Modifier.padding(4.dp),
                            onValueChange = {},
                            enabled = false,
                            label = { androidx.compose.material3.Text("Valuta") }
                        )
                    }
                    TextField(
                        value = sifraPlacanja,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        enabled = false,
                        label = { androidx.compose.material3.Text("Šifra plaćanja") }
                    )
                    TextField(
                        value = svhraPlacanja,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        enabled = false,
                        label = { androidx.compose.material3.Text("Uneditable Field") }
                    )
                    Button(
                        onClick = {
                            PaymentService.pay(pozivNaBroj = modelIPozivNaBroj.substring(2), context)
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth()) {
                        BasicText(text="Izvrsi placanje")
                    }
                }
            }
        }
    }
    fun extractValue(text: String, prefix: String, postfix: String): String {
        val prefixIndex = text.indexOf(prefix)
        if (prefixIndex == -1) {
            return ""
        }
        val startIndex = prefixIndex + prefix.length
        val postfixIndex = text.indexOf(postfix, startIndex)
        if (postfixIndex == -1) {
            return text.substring(startIndex)
        }
        return text.substring(startIndex, postfixIndex)
    }

}