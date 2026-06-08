package com.greenhouse.dao;


import com.greenhouse.db.ConnectionManager;
import com.greenhouse.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SupplierDao {

    public List<Supplier> findAll() throws SQLException {

        String sql = """
                SELECT id,
                       company_name,
                       contact_info,
                       rating
                FROM greenhouse.suppliers
                ORDER BY id
                """;

        List<Supplier> result = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }

        return result;
    }

    public Optional<Supplier> findById(int id) throws SQLException {

        String sql = """
                SELECT id,
                       company_name,
                       contact_info,
                       rating
                FROM greenhouse.suppliers
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next()
                        ? Optional.of(mapRow(rs))
                        : Optional.empty();
            }
        }
    }

    public int insert(Supplier supplier) throws SQLException {

        String sql = """
                INSERT INTO greenhouse.suppliers
                (
                    company_name,
                    contact_info,
                    rating
                )
                VALUES (?, ?, ?)
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, supplier.getCompanyName());
            ps.setString(2, supplier.getContactInfo());
            ps.setDouble(3, supplier.getRating());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {

                if (keys.next()) {

                    int id = keys.getInt(1);

                    supplier.setId(id);

                    return id;
                }
            }

            throw new SQLException("Failed to get generated key");
        }
    }

    public boolean update(Supplier supplier)
            throws SQLException {

        String sql = """
                UPDATE greenhouse.suppliers
                SET company_name = ?,
                    contact_info = ?,
                    rating = ?
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, supplier.getCompanyName());
            ps.setString(2, supplier.getContactInfo());
            ps.setDouble(3, supplier.getRating());
            ps.setInt(4, supplier.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id)
            throws SQLException {

        String sql = """
                DELETE FROM greenhouse.suppliers
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    private Supplier mapRow(ResultSet rs)
            throws SQLException {

        return new Supplier(
                rs.getInt("id"),
                rs.getString("company_name"),
                rs.getString("contact_info"),
                rs.getDouble("rating")
        );
    }
}
