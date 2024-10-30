import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CRUDPersonas2 extends JFrame {
    private JTextField txtNombre, txtEdad, txtDireccion, txtTelefono;
    private DefaultTableModel tableModel;
    private JTable table;
    private List<String> personas;

    public CRUDPersonas2() {
        personas = new ArrayList<>();
        cargarPersonasDesdeArchivo();

        // Configurar la ventana principal
        setTitle("CRUD de Información Personal");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel para ingresar datos
        JPanel inputPanel = new JPanel(new GridLayout(4, 2)); // Ajusta la rejilla a 4 filas (1 por campo de texto)
        
        inputPanel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        inputPanel.add(txtNombre);

        inputPanel.add(new JLabel("Edad:"));
        txtEdad = new JTextField();
        inputPanel.add(txtEdad);

        inputPanel.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        inputPanel.add(txtDireccion);

        inputPanel.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        inputPanel.add(txtTelefono);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4)); // Los botones en una fila

        JButton btnCrear = new JButton("Crear");
        btnCrear.addActionListener(e -> crearPersona());
        buttonPanel.add(btnCrear);

        JButton btnLeer = new JButton("Leer");
        btnLeer.addActionListener(e -> mostrarPersonas());
        buttonPanel.add(btnLeer);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> actualizarPersona());
        buttonPanel.add(btnActualizar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarPersona());
        buttonPanel.add(btnEliminar);

        // Crear un panel superior para incluir tanto el inputPanel como el buttonPanel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Tabla para mostrar resultados
        tableModel = new DefaultTableModel(new String[]{"Nombre", "Edad", "Dirección", "Teléfono"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Agregar los paneles a la ventana
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Crear una persona (agregar al archivo)
private void crearPersona() {
    String nombre = txtNombre.getText();
    String edad = txtEdad.getText();
    String direccion = txtDireccion.getText();
    String telefono = txtTelefono.getText();

    if (!nombre.isEmpty() && !edad.isEmpty() && !direccion.isEmpty() && !telefono.isEmpty()) {
        String persona = nombre + "," + edad + "," + direccion + "," + telefono;
        personas.add(persona);
        guardarEnArchivo();
        // mostrarPersonas(); // No actualiza la tabla automáticamente
        limpiarCampos();
    } else {
        JOptionPane.showMessageDialog(this, "Complete todos los campos");
    }
}

    // Mostrar personas en la tabla
    private void mostrarPersonas() {
        tableModel.setRowCount(0); // Limpia la tabla antes de cargar los datos
        cargarPersonasDesdeArchivo(); // Asegura que se carguen los datos actuales desde el archivo
        for (String persona : personas) {
            String[] datos = persona.split(",");
            tableModel.addRow(datos); // Agrega cada persona como una fila en la tabla
        }
    }

    // Actualizar persona
    private void actualizarPersona() {
        String nombre = txtNombre.getText();
        for (int i = 0; i < personas.size(); i++) {
            String[] datos = personas.get(i).split(",");
            if (datos[0].equals(nombre)) {
                personas.set(i, nombre + "," + txtEdad.getText() + "," + txtDireccion.getText() + "," + txtTelefono.getText());
                guardarEnArchivo();
                mostrarPersonas();
                limpiarCampos();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Persona no encontrada");
    }

    // Eliminar persona
    private void eliminarPersona() {
        String nombre = txtNombre.getText();
        for (int i = 0; i < personas.size(); i++) {
            String[] datos = personas.get(i).split(",");
            if (datos[0].equals(nombre)) {
                personas.remove(i);
                guardarEnArchivo();
                mostrarPersonas();
                limpiarCampos();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Persona no encontrada");
    }

    // Guardar las personas en un archivo
    private void guardarEnArchivo() {
        try (FileWriter writer = new FileWriter("personas.txt")) {
            for (String persona : personas) {
                writer.write(persona + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo");
        }
    }

    // Cargar personas desde el archivo
    private void cargarPersonasDesdeArchivo() {
        personas.clear(); // Limpiar la lista antes de recargar
        try (BufferedReader reader = new BufferedReader(new FileReader("personas.txt"))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                personas.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Archivo no encontrado, se creará uno nuevo.");
        }
    }

    // Limpiar los campos de texto
    private void limpiarCampos() {
        txtNombre.setText("");
        txtEdad.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CRUDPersonas2 ventana = new CRUDPersonas2();
            ventana.setVisible(true);
        });
    }
}


