package dao;

import conexion.ConexionBD;
import modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public void insertar(Producto p) {
        String sql = "INSERT INTO productos(nombre, marca, precio, stock) VALUES(?,?,?,?)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getMarca());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setMarca(rs.getString("marca"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));

                lista.add(p);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return lista;
    }

    public void actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre=?, marca=?, precio=?, stock=? WHERE id=?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getMarca());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id=?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}