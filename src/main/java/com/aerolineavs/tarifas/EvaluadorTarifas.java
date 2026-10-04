package com.aerolineavs.tarifas;

import com.aerolineavs.tarifas.service.IPricingService;
import com.aerolineavs.tarifas.service.PricingServiceImpl;

/**
 * Motor de reglas para determinar la tarifa más adecuada.
 */
public final class EvaluadorTarifas {

    private static final IPricingService PRICING_SERVICE = new PricingServiceImpl();

    private EvaluadorTarifas() {
    }

    /**
     * Evalúa una única tarifa aplicable en función de los datos del cliente.
     *
     * @param cliente datos del cliente
     * @return tarifa resultante junto con suposiciones
     */
    public static ResultadoTarifa evaluar(ClientePotencial cliente) {
        return PRICING_SERVICE.evaluar(cliente);
    }
}
