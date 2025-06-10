import javax.swing.*;
import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
//import java.awt.image.BufferedImage;

public class DB extends JFrame {

    private JComboBox<String> sedeComboBox;
    private JComboBox<String> areaComboBox;
    private JTextField usuarioField;
    private JTextField sistemaField;
    private JTextField serialField;
    private JTextField tipoMaquinaField;
    private JTextField cantidadMonitoresField;
    private JTextField marcaMonitorField;
    private JTextField pulgadasMonitorField;
    private JTextField serialMonitorField;
    private JTextField perifericosField;
    private JTextField ramField;
    private JTextField fabricanteField;
    private JTextField modeloField;
    private JTextField tipoDiscoField;
    private JTextField capacidadDiscoField;
    private JTextField soField;
    private JTextField procesadorField;
    private JTextField licenciaOfficeField;
    
    private JButton guardarButton;
    private JButton editarButton;
    private JButton eliminarButton;
    private JButton buscarButton;
    private JButton exportarButton;
    private JButton mostrarButton;
    
    private List<String[]> registros;
    private File archivo;

    
    public DB() {
        super("Registro de Inventario de Máquinas (Archivo)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 730);
        setLayout(new GridLayout(0, 2, 10, 10));
        
        // Inicializar lista de registros y archivo
        registros = new ArrayList<>();
        archivo = new File("C:\\Users\\ASUS-PC\\Desktop\\DBapp\\inventario_maquinas.csv");
        
        // Cargar registros existentes si el archivo existe
        cargarRegistrosExistentes();
        
        // Inicializar componentes
        initComponents();
        
        // Configurar eventos
        configurarEventos();

        mostrarRegistros();
        
        setVisible(true);
    }

    private void cargarRegistrosExistentes() {
        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                // Saltar la primera línea (encabezados)
                br.readLine();
                
                while ((linea = br.readLine()) != null) {
                    String[] registro = linea.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                    registros.add(registro);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + e.getMessage(), 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void initComponents() {
        // Sede
        add(new JLabel("Sede:"));
        String[] sedes = {"Centro", "Chopo"};
        sedeComboBox = new JComboBox<>(sedes);
        add(sedeComboBox);
        
        // Área (se actualizará según la sede seleccionada)
        add(new JLabel("Área:"));
        areaComboBox = new JComboBox<>();
        actualizarAreas();
        add(areaComboBox);
        
        // Campos de texto
        add(new JLabel("Nombre de Usuario:"));
        usuarioField = new JTextField();
        add(usuarioField);
        
        add(new JLabel("Nombre del Sistema:"));
        sistemaField = new JTextField();
        add(sistemaField);

        add(new JLabel("Serial No."));
        serialField = new JTextField();
        add(serialField);
        
        add(new JLabel("Tipo de Máquina:"));
        tipoMaquinaField = new JTextField();
        add(tipoMaquinaField);
        
        // Información del monitor
        add(new JLabel("Cantidad de Monitores:"));
        cantidadMonitoresField = new JTextField();
        add(cantidadMonitoresField);
        
        add(new JLabel("Marca del Monitor:"));
        marcaMonitorField = new JTextField();
        add(marcaMonitorField);
        
        add(new JLabel("Pulgadas del Monitor:"));
        pulgadasMonitorField = new JTextField();
        add(pulgadasMonitorField);
        
        add(new JLabel("Número Serial del Monitor:"));
        serialMonitorField = new JTextField();
        add(serialMonitorField);

        add(new JLabel("Perifericos (Mouse/teclado)"));
        perifericosField = new JTextField();
        add(perifericosField);
        
        add(new JLabel("RAM:"));
        ramField = new JTextField();
        add(ramField);

        add(new JLabel("Fabricante:"));
        fabricanteField = new JTextField();
        add(fabricanteField);

        add(new JLabel("Modelo:"));
        modeloField = new JTextField();
        add(modeloField);
        
        add(new JLabel("Tipo de Disco Duro:"));
        tipoDiscoField = new JTextField();
        add(tipoDiscoField);
        
        add(new JLabel("Capacidad de Disco Duro:"));
        capacidadDiscoField = new JTextField();
        add(capacidadDiscoField);
        
        add(new JLabel("Sistema Operativo:"));
        soField = new JTextField();
        add(soField);
        
        add(new JLabel("Procesador:"));
        procesadorField = new JTextField();
        add(procesadorField);
        
        
        add(new JLabel("Licencia Office:"));
        licenciaOfficeField = new JTextField();
        add(licenciaOfficeField);
        
        // Botones
        guardarButton = new JButton("Guardar");
        add(guardarButton);
        
        buscarButton = new JButton("Buscar");
        add(buscarButton);
        
        editarButton = new JButton("Editar");
        add(editarButton);

        eliminarButton = new JButton("Eliminar");
        add(eliminarButton);
        
        exportarButton = new JButton("Exportar a CSV");
        add(exportarButton);

        mostrarButton = new JButton("Mostrar registros");
        add(mostrarButton);


    }
    
    private void configurarEventos() {
        // Actualizar áreas cuando cambia la sede
        sedeComboBox.addActionListener(e -> actualizarAreas());
        
        // Guardar registro
        guardarButton.addActionListener(e -> guardarRegistro());
        
        // Buscar registro
        buscarButton.addActionListener(e -> buscarRegistro());
        
        // Editar registro
        editarButton.addActionListener(e -> editarRegistro());

        //Eliminar registro
        eliminarButton.addActionListener(e -> eliminarRegistro());
        
        // Exportar a CSV
        exportarButton.addActionListener(e -> exportarACSV());

        //Mostrar registros
        mostrarButton.addActionListener(e -> mostrarRegistros());
    }
    
    private void actualizarAreas() {
        String sedeSeleccionada = (String) sedeComboBox.getSelectedItem();
        String[] areas;
        
        if ("Centro".equals(sedeSeleccionada)) {
            areas = new String[]{"Almacén", "3D", "Control de Calidad", "Plataformas", 
                               "Recepción", "Capacitación", "TMK", "Ventas"};
        } else { // Chopo
            areas = new String[]{"IA", "Tienda Virtual", "Mercadoctenia", 
                               "Mejora Continua", "Precidencia", "Ventas Proyectos Tecnologicos",
                               "Ventas en pltaformas", "Diseño Gráfico", "Compras", 
                               "RH", "Recepcion", "Plataformas digitales", "Contabilidad", 
                               "Ingeniería General"};
        }
        
        areaComboBox.setModel(new DefaultComboBoxModel<>(areas));
    }
    
    private void guardarRegistro() {
        try {
            String[] nuevoRegistro = new String[19];
            
            nuevoRegistro[0] = (String) sedeComboBox.getSelectedItem();
            nuevoRegistro[1] = (String) areaComboBox.getSelectedItem();
            nuevoRegistro[2] = usuarioField.getText();
            nuevoRegistro[3] = sistemaField.getText();
            nuevoRegistro[4] = serialField.getText();
            nuevoRegistro[5] = tipoMaquinaField.getText();
            nuevoRegistro[6] = cantidadMonitoresField.getText();
            nuevoRegistro[7] = marcaMonitorField.getText();
            nuevoRegistro[8] = pulgadasMonitorField.getText();
            nuevoRegistro[9] = serialMonitorField.getText();
            nuevoRegistro[10] = perifericosField.getText();
            nuevoRegistro[11] = ramField.getText();
            nuevoRegistro[12] = fabricanteField.getText();
            nuevoRegistro[13] = modeloField.getText();
            nuevoRegistro[14] = tipoDiscoField.getText();
            nuevoRegistro[15] = capacidadDiscoField.getText();
            nuevoRegistro[16] = soField.getText();
            nuevoRegistro[17] = procesadorField.getText();
            nuevoRegistro[18] = licenciaOfficeField.getText();
            
            // Verificar si el sistema ya existe
            boolean existe = false;
            for (String[] registro : registros) {
                if (registro[4].equals(nuevoRegistro[4])) {
                    existe = true;
                    break;
                }
            }
            
            if (existe) {
                JOptionPane.showMessageDialog(this, "Ya existe un registro con ese Numero de serie", 
                                          "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                registros.add(nuevoRegistro);
                JOptionPane.showMessageDialog(this, "Registro guardado en memoria", 
                                          "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el registro: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarRegistro() {
        String numeroSerie = serialField.getText().trim();
        
        if (numeroSerie.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el Numero de serie a buscar", 
                                      "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        boolean encontrado = false;
        for (String[] registro : registros) {
            if (registro[4].equals(numeroSerie)) {
                // Llenar los campos con los datos encontrados
                sedeComboBox.setSelectedItem(registro[0]);
                areaComboBox.setSelectedItem(registro[1]);
                usuarioField.setText(registro[2]);
                sistemaField.setText(registro[3]);
                tipoMaquinaField.setText(registro[5]);
                cantidadMonitoresField.setText(registro[6]);
                marcaMonitorField.setText(registro[7]);
                pulgadasMonitorField.setText(registro[8]);
                serialMonitorField.setText(registro[9]);
                perifericosField.setText(registro[10]);
                ramField.setText(registro[11]);
                fabricanteField.setText(registro[12]);
                modeloField.setText(registro[13]);
                tipoDiscoField.setText(registro[14]);
                capacidadDiscoField.setText(registro[15]);
                soField.setText(registro[16]);
                procesadorField.setText(registro[17]);
                licenciaOfficeField.setText(registro[18]);
                
                encontrado = true;
                break;
            }
        }
        
        if (encontrado) {
            JOptionPane.showMessageDialog(this, "Registro encontrado", 
                                      "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ningún registro con ese nombre de sistema", 
                                      "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void editarRegistro() {
        String numeroSerie = serialField.getText().trim();
        
        if (numeroSerie.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el numero de serie a editar", 
                                      "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        boolean encontrado = false;
        for (int i = 0; i < registros.size(); i++) {
            String[] registro = registros.get(i);
            if (registro[4].equals(numeroSerie)) {
                // Actualizar el registro
                registro[0] = (String) sedeComboBox.getSelectedItem();
                registro[1] = (String) areaComboBox.getSelectedItem();
                registro[2] = usuarioField.getText();
                registro[3] = sistemaField.getText();
                registro[5] = tipoMaquinaField.getText();
                registro[6] = cantidadMonitoresField.getText();
                registro[7] = marcaMonitorField.getText();
                registro[8] = pulgadasMonitorField.getText();
                registro[9] = serialMonitorField.getText();
                registro[10] = perifericosField.getText();
                registro[11] = ramField.getText();
                registro[12] = fabricanteField.getText();
                registro[13] = modeloField.getText();
                registro[14] = tipoDiscoField.getText();
                registro[15] = capacidadDiscoField.getText();
                registro[16] = soField.getText();
                registro[17] = procesadorField.getText();
                registro[18] = licenciaOfficeField.getText();
                
                registros.set(i, registro);
                encontrado = true;
                break;
            }
        }
        
        if (encontrado) {
            JOptionPane.showMessageDialog(this, "Registro actualizado en memoria", 
                                      "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ningún registro con ese nombre de sistema", 
                                      "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarRegistro(){
        String numeroSerie = serialField.getText().trim();
        if (numeroSerie.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el numero de serie a eliminar",
            "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
            }
            boolean encontrado = false;
            for (int i = 0; i < registros.size(); i++){
                String[] registro = registros.get(i);
                if (registro[4].equals(numeroSerie)) {

                    //Confirmar eliminacion
                    int respuesta = JOptionPane.showConfirmDialog(this, "¿Desea eliminar el registro?",
                    "Confirmación", JOptionPane.YES_NO_OPTION);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        registros.remove(i);

                        encontrado = true;
                        JOptionPane.showMessageDialog(this, "Registro eliminado de la memoria",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        limpiarCampos();
                    }
                    break;
                }
            }
            if (!encontrado) {
                JOptionPane.showMessageDialog(this, "No se encontró ningún registro con ese nombre de sistema",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
    }
    
    private void exportarACSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            // Escribir encabezados
            pw.println("Sede, Área, Usuario, Sistema, Serial No, Tipo Máquina, Cantidad Monitores, Marca Monitor, " +
                      "Pulgadas Monitor, Serial Monitor, Perifericos(Mouse/Teclado), RAM, Fabricante, Modelo, Tipo Disco, Capacidad Disco, Sistema Operativo, " +
                      "Procesador, Licencia Office");
            
            // Escribir registros
            for (String[] registro : registros) {
                pw.println(String.join(",", registro));
            }
            
            JOptionPane.showMessageDialog(this, "Datos exportados exitosamente a " + archivo.getName(), 
                                      "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al exportar a CSV: " + e.getMessage(), 
                                      "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JFrame frameRegistrosExistente = null;
private String areaActual = null;

private void mostrarRegistros() {
    String areaSeleccionada = (String) areaComboBox.getSelectedItem();
    
    // Filtrar registros por el área seleccionada
    List<String[]> registrosFiltrados = registros.stream()
        .filter(registro -> registro[1].equals(areaSeleccionada))
        .collect(Collectors.toList());
    
    if (registrosFiltrados.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay registros para el área seleccionada", 
                                  "Información", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    
    // Verificar si ya hay una ventana abierta para esta área
    if (frameRegistrosExistente != null && areaSeleccionada.equals(areaActual)) {
        // Actualizar la tabla existente
        actualizarTablaRegistros(frameRegistrosExistente, registrosFiltrados, areaSeleccionada);
        frameRegistrosExistente.toFront();
        return;
    }
    
    // Si no existe o es para otra área, crear nueva ventana
    areaActual = areaSeleccionada;
    frameRegistrosExistente = new JFrame("Registros del Área: " + areaSeleccionada);
    frameRegistrosExistente.setLocation(485, 0);
    frameRegistrosExistente.setSize(890, 730);
    frameRegistrosExistente.setLayout(new BorderLayout());
    
    // Configurar cierre de ventana
    frameRegistrosExistente.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
            frameRegistrosExistente = null;
            areaActual = null;
        }
    });
    
    // Crear y mostrar la tabla
    actualizarTablaRegistros(frameRegistrosExistente, registrosFiltrados, areaSeleccionada);
    frameRegistrosExistente.setVisible(true);
}

private void actualizarTablaRegistros(JFrame frame, List<String[]> registrosFiltrados, String areaSeleccionada) {
    // Limpiar el frame antes de agregar nuevos componentes
    frame.getContentPane().removeAll();
    
    // Crear tabla con los datos
    String[] columnNames = {"Sede", "Área", "Usuario", "Sistema", "Serial No", "Tipo Máquina", 
                          "Cant. Monitores", "Marca Monitor", "Pulgadas", "Serial Monitor", 
                          "Periféricos", "RAM", "Fabricante", "Modelo", "Tipo Disco", 
                          "Capacidad Disco", "Sistema Operativo", "Procesador", "Licencia Office"};
    
    Object[][] data = new Object[registrosFiltrados.size()][19];
    for (int i = 0; i < registrosFiltrados.size(); i++) {
        data[i] = registrosFiltrados.get(i);
    }
    
    JTable table = new JTable(data, columnNames);
    JScrollPane scrollPane = new JScrollPane(table);
    frame.add(scrollPane, BorderLayout.CENTER);
    
    // Actualizar título por si cambió el área
    frame.setTitle("Registros del Área: " + areaSeleccionada);
    
    // Redibujar el frame
    frame.revalidate();
    frame.repaint();
}

    private void limpiarCampos() {
        //sedeComboBox.setSelectedIndex(0);
        //areaComboBox.setSelectedIndex(0);
        usuarioField.setText("");
        sistemaField.setText("");
        serialField.setText("");
        tipoMaquinaField.setText("");
        cantidadMonitoresField.setText("");
        marcaMonitorField.setText("");
        pulgadasMonitorField.setText("");
        serialMonitorField.setText("");
        perifericosField.setText("");
        ramField.setText("");
        fabricanteField.setText("");
        modeloField.setText("");
        tipoDiscoField.setText("");
        capacidadDiscoField.setText("");
        soField.setText("");
        procesadorField.setText("");
        licenciaOfficeField.setText("");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DB());
    }
}