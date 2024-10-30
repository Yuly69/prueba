import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Rockola extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private int creditos = 0; // Créditos del usuario
    private JLabel creditLabel;
    private int saldoFavor = 0; // Saldo a favor
    private List<String[]> cancionesList = new ArrayList<>(); // Almacenar todas las canciones
    private DefaultTableModel playlistModel; // Modelo de la tabla de lista de reproducción
    private JTable playlistTable; // Tabla de lista de reproducción

    // Constructor
    public Rockola() {
        setTitle("Rockola");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crear la tabla de canciones
        tableModel = new DefaultTableModel(new String[]{"Canción", "Artista", "Género", "Ruta"}, 0);
        table = new JTable(tableModel);
        cargarCancionesDesdeCSV("canciones.csv");
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de búsqueda
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(15);
        filterComboBox = new JComboBox<>(new String[]{"Nombre", "Artista", "Género"});
        JButton searchButton = new JButton("Buscar");
        searchPanel.add(new JLabel("Buscar por:"));
        searchPanel.add(filterComboBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Panel de créditos y área de lista de reproducción
        JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel creditPanel = new JPanel();
        JButton addCreditButton = new JButton("Agregar Dinero");
        creditLabel = new JLabel("Créditos: 0 | Saldo a favor: 0");
        creditPanel.add(addCreditButton);
        creditPanel.add(creditLabel);
        rightPanel.add(creditPanel, BorderLayout.NORTH);

        // Tabla para la lista de reproducción
        playlistModel = new DefaultTableModel(new String[]{"Canción", "Artista", "Ruta"}, 0);
        playlistTable = new JTable(playlistModel);
        JScrollPane playlistScrollPane = new JScrollPane(playlistTable);
        rightPanel.add(playlistScrollPane, BorderLayout.CENTER);

        // Botones de control para la lista de reproducción
        JButton addToPlaylistButton = new JButton("Agregar a lista de reproducción");
        JButton playButton = new JButton("Reproducir");
        JButton removeFromPlaylistButton = new JButton("Quitar de lista de reproducción");
        JPanel playlistControlPanel = new JPanel();
        playlistControlPanel.add(addToPlaylistButton);
        playlistControlPanel.add(removeFromPlaylistButton);
        playlistControlPanel.add(playButton);
        rightPanel.add(playlistControlPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);

        // Evento buscar
        searchButton.addActionListener(e -> buscarCanciones());

        // Evento agregar crédito
        addCreditButton.addActionListener(e -> agregarDinero());

        // Evento agregar canción a la lista de reproducción
        addToPlaylistButton.addActionListener(e -> agregarAListaDeReproduccion());

        // Evento para reproducir la canción seleccionada de la lista de reproducción
        playButton.addActionListener(e -> reproducirCancionDeLista());

        // Evento para quitar una canción de la lista de reproducción
        removeFromPlaylistButton.addActionListener(e -> quitarDeListaDeReproduccion());
    }

    // Método para buscar y filtrar canciones
    private void buscarCanciones() {
        String filtro = searchField.getText().toLowerCase();
        String criterio = filterComboBox.getSelectedItem().toString().toLowerCase();
        tableModel.setRowCount(0); // Limpiar la tabla
        for (String[] cancion : cancionesList) {
            if (cancionMatches(cancion, criterio, filtro)) {
                tableModel.addRow(cancion);
            }
        }
    }

    // Comprobar si una canción coincide con el criterio de búsqueda
    private boolean cancionMatches(String[] cancion, String criterio, String filtro) {
        switch (criterio) {
            case "nombre": return cancion[0].toLowerCase().contains(filtro);
            case "artista": return cancion[1].toLowerCase().contains(filtro);
            case "género": return cancion[2].toLowerCase().contains(filtro);
        }
        return false;
    }

    // Método para agregar dinero y actualizar los créditos
    private void agregarDinero() {
        String input = JOptionPane.showInputDialog(this, "Ingresa la cantidad de dinero:");
        try {
            int dinero = Integer.parseInt(input);
            if (dinero > 0) {
                int creditosNuevos = dinero / 500;
                int excedente = dinero % 500;
                creditos += creditosNuevos;
                saldoFavor += excedente;
                // Verificar si el saldo a favor puede generar un nuevo crédito
                if (saldoFavor >= 500) {
                    creditos += saldoFavor / 500;
                    saldoFavor = saldoFavor % 500; // Actualizar el saldo restante
                }
                creditLabel.setText("Créditos: " + creditos + " | Saldo a favor: " + saldoFavor);
            } else {
                JOptionPane.showMessageDialog(this, "Ingresa una cantidad válida.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un número válido.");
        }
    }

    // Método para agregar la canción seleccionada a la lista de reproducción
    private void agregarAListaDeReproduccion() {
        int row = table.getSelectedRow();
        if (row != -1) {
            String cancion = tableModel.getValueAt(row, 0).toString();
            String artista = tableModel.getValueAt(row, 1).toString();
            String ruta = tableModel.getValueAt(row, 3).toString();
            playlistModel.addRow(new String[]{cancion, artista, ruta});
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una canción de la tabla.");
        }
    }

    // Método para quitar una canción de la lista de reproducción
    private void quitarDeListaDeReproduccion() {
        int row = playlistTable.getSelectedRow();
        if (row != -1) {
            playlistModel.removeRow(row);
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una canción de la lista de reproducción.");
        }
    }

    // Método para reproducir una canción de la lista de reproducción
    private void reproducirCancionDeLista() {
        int row = playlistTable.getSelectedRow();
        if (row != -1 && creditos > 0) {
            String ruta = playlistModel.getValueAt(row, 2).toString();
            boolean reproduccionExitosa = reproducirCancion(ruta);
            if (reproduccionExitosa) {
                creditos--;
                creditLabel.setText("Créditos: " + creditos + " | Saldo a favor: " + saldoFavor);
                playlistModel.removeRow(row); // Eliminar la canción reproducida de la lista
            }
        } else if (creditos <= 0) {
            JOptionPane.showMessageDialog(this, "No tienes créditos suficientes.");
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una canción de la lista de reproducción.");
        }
    }

    // Método para cargar canciones desde un archivo CSV
    private void cargarCancionesDesdeCSV(String archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                cancionesList.add(datos); // Guardar para búsquedas
                tableModel.addRow(datos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    // Método para reproducir una canción utilizando Desktop
    private boolean reproducirCancion(String ruta) {
        try {
            File archivo = new File(ruta);
            if (archivo.exists()) {
                Desktop.getDesktop().open(archivo); // Abre el archivo en el reproductor predeterminado
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Archivo no encontrado: " + ruta);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }*/
     // Método para reproducir una canción utilizando Desktop
     private boolean reproducirCancion(String ruta) {
        try {
            File archivo; 
            
            if (ruta.startsWith("http"))
            {
                new ProcessBuilder("C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe", ruta).start();
            }
            else
            {
                archivo = new File(ruta);
                
                if (archivo.exists()) {
                    Desktop.getDesktop().open(archivo); // Abre el archivo en el reproductor predeterminado
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "Archivo no encontrado: " + ruta);
                    return false;
                }
            }
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método principal para iniciar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Rockola rockola = new Rockola();
            rockola.setVisible(true);
        });
    }
}
