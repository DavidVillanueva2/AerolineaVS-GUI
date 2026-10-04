package com.aerolineavs.tarifas.ui;

import com.aerolineavs.tarifas.ClaseVuelo;
import com.aerolineavs.tarifas.ClientePotencial;
import com.aerolineavs.tarifas.RegionDestino;
import com.aerolineavs.tarifas.ResultadoTarifa;
import com.aerolineavs.tarifas.Tarifa;
import com.aerolineavs.tarifas.TipoViajero;
import com.aerolineavs.tarifas.service.IPricingService;
import com.aerolineavs.tarifas.service.PricingServiceImpl;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

public class PricingDesktopApp extends JFrame {

    private final IPricingService pricingService;

    private final JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(30, 0, 120, 1));
    private final JSpinner flightsSpinner = new JSpinner(new SpinnerNumberModel(3, 0, 365, 1));
    private final JSpinner incomeSpinner = new JSpinner(new SpinnerNumberModel(25000, 0, 1000000, 1000));
    private final JComboBox<TipoViajero> travelerTypeCombo = new JComboBox<>(TipoViajero.values());
    private final JComboBox<ClaseVuelo> flightClassCombo = new JComboBox<>(ClaseVuelo.values());
    private final JComboBox<RegionDestino> destinationCombo = new JComboBox<>(RegionDestino.values());
    private final JCheckBox withChildrenCheckBox = new JCheckBox("Viaja con niños menores de 12");
    private final JCheckBox livesWithParentsCheckBox = new JCheckBox("Vive con sus padres");
    private final JLabel tariffLabel = new JLabel("-");
    private final JLabel discountLabel = new JLabel("-");
    private final JTextArea assumptionsArea = new JTextArea(5, 30);

    public PricingDesktopApp(IPricingService pricingService) {
        this.pricingService = pricingService;
        initFrame();
    }

    public PricingDesktopApp() {
        this(new PricingServiceImpl());
    }

    private void initFrame() {
        setTitle("Aerolínea VS - Evaluador de Tarifas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(12, 12));
        setMinimumSize(new Dimension(620, 460));
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Perfil del cliente"));
        add(formPanel, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addField(formPanel, c, row++, "Edad:", ageSpinner);
        addField(formPanel, c, row++, "Vuelos al año:", flightsSpinner);
        addField(formPanel, c, row++, "Ingresos anuales (€):", incomeSpinner);
        addField(formPanel, c, row++, "Tipo de viajero:", travelerTypeCombo);
        addField(formPanel, c, row++, "Clase preferida:", flightClassCombo);
        addField(formPanel, c, row++, "Destino preferido:", destinationCombo);

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = row++;
        formPanel.add(withChildrenCheckBox, c);

        c.gridy = row++;
        formPanel.add(livesWithParentsCheckBox, c);

        JPanel resultPanel = new JPanel();
        resultPanel.setBorder(BorderFactory.createTitledBorder("Resultado"));
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        JPanel tariffRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tariffRow.add(new JLabel("Tarifa:"));
        tariffRow.add(tariffLabel);

        JPanel discountRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        discountRow.add(new JLabel("Descuento:"));
        discountRow.add(discountLabel);

        assumptionsArea.setEditable(false);
        assumptionsArea.setLineWrap(true);
        assumptionsArea.setWrapStyleWord(true);
        assumptionsArea.setText("Pulse \"Calcular tarifa\" para evaluar el perfil.");

        resultPanel.add(tariffRow);
        resultPanel.add(discountRow);
        resultPanel.add(new JScrollPane(assumptionsArea));

        add(resultPanel, BorderLayout.SOUTH);

        JButton calculateButton = new JButton("Calcular tarifa");
        calculateButton.addActionListener(this::calculatePricing);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionsPanel.add(calculateButton);
        add(actionsPanel, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void addField(JPanel panel, GridBagConstraints constraints, int row, String labelText, java.awt.Component component) {
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.gridwidth = 1;
        constraints.weightx = 0.2;
        panel.add(new JLabel(labelText), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.8;
        panel.add(component, constraints);
    }

    private void calculatePricing(ActionEvent event) {
        ClientePotencial cliente = new ClientePotencial(
                (Integer) ageSpinner.getValue(),
                (Integer) flightsSpinner.getValue(),
                (TipoViajero) travelerTypeCombo.getSelectedItem(),
                (ClaseVuelo) flightClassCombo.getSelectedItem(),
                (RegionDestino) destinationCombo.getSelectedItem(),
                ((Number) incomeSpinner.getValue()).doubleValue(),
                withChildrenCheckBox.isSelected(),
                livesWithParentsCheckBox.isSelected()
        );

        ResultadoTarifa resultado = pricingService.evaluar(cliente);
        Tarifa tarifa = resultado.tarifa();

        tariffLabel.setText(tarifa.getNombre());
        discountLabel.setText(tarifa.getDescuentoPorcentaje() + "%");
        assumptionsArea.setText(resultado.suposiciones());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PricingDesktopApp app = new PricingDesktopApp();
            app.setVisible(true);
        });
    }
}
