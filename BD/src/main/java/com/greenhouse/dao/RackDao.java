package com.greenhouse.dao;

import com.greenhouse.db.ConnectionManager;
import com.greenhouse.model.Rack;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RackDao {

    public List<Rack> findAll() throws SQLException {

        String sql = """
                SELECT id,
                       installation_date,
                       tier_count,
                       name
                FROM greenhouse.racks
                ORDER BY id
                """;

        List<Rack> result = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }

        return result;
    }

    public Optional<Rack> findById(int id) throws SQLException {

        String sql = """
                SELECT id,
                       installation_date,
                       tier_count,
                       name
                FROM greenhouse.racks
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

    public short insert(Rack rack) throws SQLException {

        String sql = """
                INSERT INTO greenhouse.racks
                (
                    installation_date,
                    tier_count,
                    name
                )
                VALUES (?, ?, ?)
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, Date.valueOf(rack.getInstallationDate()));
            ps.setInt(2, rack.getTierCount());
            ps.setString(3, rack.getName());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {

                if (keys.next()) {

                    short id = keys.getShort(1);

                    rack.setId((int) id);

                    return id;
                }
            }

            throw new SQLException("Failed to get generated key");
        }
    }

    public boolean update(Rack rack) throws SQLException {

        String sql = """
                UPDATE greenhouse.racks
                SET installation_date = ?,
                    tier_count = ?,
                    name = ?
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(rack.getInstallationDate()));
            ps.setInt(2, rack.getTierCount());
            ps.setString(3, rack.getName());
            ps.setInt(4, rack.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {

        String sql = """
                DELETE FROM greenhouse.racks
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    private Rack mapRow(ResultSet rs) throws SQLException {

        return new Rack(
                rs.getInt("id"),
                rs.getDate("installation_date").toLocalDate(),
                rs.getInt("tier_count"),
                rs.getString("name")
        );
    }
}
