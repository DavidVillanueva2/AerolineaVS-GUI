package com.aerolineavs.tarifas.service;

import com.aerolineavs.tarifas.ClientePotencial;
import com.aerolineavs.tarifas.ResultadoTarifa;

public interface IPricingService {

    ResultadoTarifa evaluar(ClientePotencial cliente);
}
