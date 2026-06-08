package com.greenhouse.dao;

import com.greenhouse.db.ConnectionManager;
import com.greenhouse.model.SeedBatch;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SeedBatchDao {

    public List<SeedBatch> findAll() throws SQLException {

        String sql = """
                SELECT id,
                       culture_id,
                       supplier_id,
                       delivery_date,
                       germination_percent
                FROM greenhouse.seed_batches
                ORDER BY id
                """;

        List<SeedBatch> result = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }

        return result;
    }

    public Optional<SeedBatch> findById(int id) throws SQLException {

        String sql = """
                SELECT id,
                       culture_id,
                       supplier_id,
                       delivery_date,
                       germination_percent
                FROM greenhouse.seed_batches
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

    public int insert(SeedBatch batch) throws SQLException {

        String sql = """
                INSERT INTO greenhouse.seed_batches
                (
                    culture_id,
                    supplier_id,
                    delivery_date,
                    germination_percent
                )
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, batch.getCultureId());
            ps.setInt(2, batch.getSupplierId());
            ps.setDate(3, Date.valueOf(batch.getDeliveryDate()));
            ps.setDouble(4, batch.getGerminationPercent());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {

                if (keys.next()) {

                    int id = keys.getInt(1);

                    batch.setId(id);

                    return id;
                }
            }

            throw new SQLException("Failed to get generated key");
        }
    }

    public boolean update(SeedBatch batch) throws SQLException {

        String sql = """
                UPDATE greenhouse.seed_batches
                SET culture_id = ?,
                    supplier_id = ?,
                    delivery_date = ?,
                    germination_percent = ?
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, batch.getCultureId());
            ps.setInt(2, batch.getSupplierId());
            ps.setDate(3, Date.valueOf(batch.getDeliveryDate()));
            ps.setDouble(4, batch.getGerminationPercent());
            ps.setInt(5, batch.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {

        String sql = """
                DELETE FROM greenhouse.seed_batches
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    private SeedBatch mapRow(ResultSet rs) throws SQLException {

        return new SeedBatch(
                rs.getInt("id"),
                rs.getInt("culture_id"),
                rs.getInt("supplier_id"),
                rs.getDate("delivery_date").toLocalDate(),
                rs.getDouble("germination_percent")
        );
    }
}