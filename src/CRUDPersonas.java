import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class CRUDPersonas extends JFrame {
    private JTextField txtNombre, txtEdad, txtDireccion, txtTelefono;
    private JTextArea displayArea;
    private List<String> personas;

    public CRUDPersonas() {
        personas = new ArrayList<>();
        cargarPersonasDesdeArchivo();

        // Configurar la ventana principal
        setTitle("CRUD de Información Personal");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel para ingresar datos
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        
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
        JPanel buttonPanel = new JPanel();
        
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

        // Área para mostrar resultados
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Agregar los paneles a la ventana
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
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
            mostrarPersonas();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Complete todos los campos");
        }
    }

    // Leer (mostrar todas las personas)
    private void mostrarPersonas() {
        personas.clear(); // Limpia la lista para evitar duplicados
        cargarPersonasDesdeArchivo(); // Vuelve a cargar desde el archivo
        displayArea.setText("");
        for (String persona : personas) {
            displayArea.append(persona.replace(",", " | ") + "\n");
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
            CRUDPersonas ventana = new CRUDPersonas();
            ventana.setVisible(true);
        });
    }
}
