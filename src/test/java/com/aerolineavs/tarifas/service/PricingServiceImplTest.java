package com.aerolineavs.tarifas.service;

import com.aerolineavs.tarifas.ClaseVuelo;
import com.aerolineavs.tarifas.ClientePotencial;
import com.aerolineavs.tarifas.RegionDestino;
import com.aerolineavs.tarifas.Tarifa;
import com.aerolineavs.tarifas.TipoViajero;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PricingServiceImplTest {

    @Test
    void debeEvaluarTarifaConServicio() {
        IPricingService pricingService = new PricingServiceImpl();

        ClientePotencial cliente = new ClientePotencial(
                23,
                3,
                TipoViajero.TRABAJADOR_JOVEN,
                ClaseVuelo.TURISTA,
                RegionDestino.OTRA,
                15000,
                false,
                true
        );

        assertEquals(Tarifa.VIAJA_AHORA_QUE_PUEDES, pricingService.evaluar(cliente).tarifa());
    }
}
