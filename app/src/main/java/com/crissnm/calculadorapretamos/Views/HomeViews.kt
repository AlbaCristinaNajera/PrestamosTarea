package com.crissnm.calculadorapretamos.Views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.crissnm.calculadorapretamos.Componentes.Alert
import com.crissnm.calculadorapretamos.Componentes.MainButton
import com.crissnm.calculadorapretamos.Componentes.MainTextField
import com.crissnm.calculadorapretamos.Componentes.ShowInfoCards
import com.crissnm.calculadorapretamos.Componentes.SpaceH
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(){
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(text = "Calculadora de Prestamos")},
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,

            ))

    }) {
        ContentHomeView(it)
    }
}


@Composable
fun ContentHomeView(paddingValues: PaddingValues){
    var montoPrestamo by remember { mutableStateOf("") }
    var cantCuotas by remember { mutableStateOf("") }
    var tasa by remember { mutableStateOf("") }
    var montoInteres by remember { mutableStateOf(0.0) }
    var montoCuota by remember { mutableStateOf(0.0) }

    var showAlert by remember { mutableStateOf(false) }


    Column(modifier = Modifier
        .padding(paddingValues)
        .padding(10.dp)
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally){
        ShowInfoCards(
            titleInteres = "Interes",
            montoInteres = montoInteres,
            titleMonto = "Monto",
            monto = montoCuota
        )
        MainTextField(value= montoPrestamo, onValueChange = {montoPrestamo = it}, label = "Monto del Prestamo")
        SpaceH()
        MainTextField(value= cantCuotas, onValueChange = {cantCuotas = it}, label = "Cantidad de Cuotas")
        SpaceH(10.dp)
        MainTextField(value= tasa, onValueChange = {tasa = it}, label = "Tasa de Interes")
        SpaceH(20.dp)
        MainButton(text = "Calcular"){
            if(montoPrestamo!= "" && cantCuotas != "") {
                montoInteres = calcularTotal(montoPrestamo.toDouble(), cantCuotas.toInt(), tasa.toDouble())
                montoCuota = calcularCuota(montoPrestamo.toDouble(), cantCuotas.toInt(), montoInteres.toDouble())
            }else{
                showAlert = true
            }
        }
        SpaceH()
        MainButton(text = "Borrar", color = Color.Red){
            montoPrestamo = ""
            cantCuotas = ""
            tasa = ""
            montoInteres = 0.0
            montoCuota = 0.0
        }
        if(showAlert){
            Alert(
                title = "Alerta",
                message = "Ingresa los datos solicitados",
                confirmText = "Aceptar",
                onConfirmClick = {showAlert = false},
                onDismissClick = {showAlert = false}
            )
        }
    }
}

fun calcularTotal(monto: Double, cuotas: Int, tasa: Double): Double{
    val res = cuotas * calcularCuota(monto, cuotas, tasa)
    return BigDecimal(res).setScale(2, BigDecimal.ROUND_UP).toDouble()
}

fun calcularCuota(monto: Double, cuotas: Int, tasa: Double): Double{
    val TasaInteresMnesual = tasa / 12 / 100
    val cuota= monto * TasaInteresMnesual * Math.pow(1 + TasaInteresMnesual, cuotas.toDouble()) /
            (Math.pow(1 + TasaInteresMnesual, cuotas.toDouble()) - 1)
    val cuotaRedondeada = BigDecimal(cuota).setScale(2, BigDecimal.ROUND_UP).toDouble()
    return cuotaRedondeada
    return cuota
}


