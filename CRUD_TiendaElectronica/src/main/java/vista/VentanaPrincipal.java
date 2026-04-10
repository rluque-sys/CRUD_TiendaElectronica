package vista;

import dao.ProductoDAO;
import modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    JTextField txtNombre, txtMarca, txtPrecio, txtStock;
    JTable tabla;
    DefaultTableModel modeloTabla;
    ProductoDAO dao = new ProductoDAO();

    public VentanaPrincipal() {

        setTitle("Sistema Tienda Electrónica");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // PANEL FORMULARIO
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBorder(
                BorderFactory.createTitledBorder("Registro de Productos")
        );

        txtNombre = new JTextField();
        txtMarca = new JTextField();
        txtPrecio = new JTextField();
        txtStock = new JTextField();

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Marca:"));
        panelFormulario.add(txtMarca);

        panelFormulario.add(new JLabel("Precio:"));
        panelFormulario.add(txtPrecio);

        panelFormulario.add(new JLabel("Stock:"));
        panelFormulario.add(txtStock);

        // PANEL BOTONES
        JPanel panelBotones = new JPanel(new FlowLayout(
                FlowLayout.CENTER, 15, 10));

        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnMostrar = new JButton("Mostrar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnMostrar);

        // PANEL SUPERIOR
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        // TABLA
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Marca");
        modeloTabla.addColumn("Precio");
        modeloTabla.addColumn("Stock");

        tabla = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tabla);

        add(scrollPane, BorderLayout.CENTER);

        // EVENTOS
        btnGuardar.addActionListener(e -> guardar());
        btnMostrar.addActionListener(e -> mostrar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();

            if (fila >= 0) {
                txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                txtMarca.setText(modeloTabla.getValueAt(fila, 2).toString());
                txtPrecio.setText(modeloTabla.getValueAt(fila, 3).toString());
                txtStock.setText(modeloTabla.getValueAt(fila, 4).toString());
            }
        });
    }

    private void guardar() {
        try {
            Producto p = new Producto();

            p.setNombre(txtNombre.getText());
            p.setMarca(txtMarca.getText());
            p.setPrecio(Double.parseDouble(txtPrecio.getText()));
            p.setStock(Integer.parseInt(txtStock.getText()));

            dao.insertar(p);
            mostrar();

            JOptionPane.showMessageDialog(this,
                    "Producto guardado correctamente");

            limpiarCampos();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Precio y Stock deben ser numéricos",
                    "Error de datos",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtMarca.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
    }

    private void mostrar() {
        modeloTabla.setRowCount(0);
        List<Producto> lista = dao.listar();

        for (Producto p : lista) {
            modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getMarca(),
                    p.getPrecio(),
                    p.getStock()
            });
        }
    }

    private void actualizar() {
        try {
            int fila = tabla.getSelectedRow();

            if (fila < 0) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un producto de la tabla",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (txtNombre.getText().isEmpty() ||
                    txtMarca.getText().isEmpty() ||
                    txtPrecio.getText().isEmpty() ||
                    txtStock.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Todos los campos son obligatorios",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Producto p = new Producto();
            p.setId((int) modeloTabla.getValueAt(fila, 0));
            p.setNombre(txtNombre.getText());
            p.setMarca(txtMarca.getText());
            p.setPrecio(Double.parseDouble(txtPrecio.getText()));
            p.setStock(Integer.parseInt(txtStock.getText()));

            dao.actualizar(p);
            mostrar();

            JOptionPane.showMessageDialog(this,
                    "Producto actualizado correctamente");

            limpiarCampos();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Precio y Stock deben ser numéricos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();

        if (fila >= 0) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            dao.eliminar(id);
            mostrar();
        }
    }
}