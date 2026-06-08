package com.greenhouse.dao;

import com.greenhouse.db.ConnectionManager;
import com.greenhouse.model.ChangeHistory;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChangeHistoryDao {

    public List<ChangeHistory> findAll() throws SQLException {

        String sql = """
                SELECT id,
                       sensor_id,
                       value,
                       recorded_at
                FROM greenhouse.change_history
                ORDER BY id
                """;

        List<ChangeHistory> result = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }

        return result;
    }

    public Optional<ChangeHistory> findById(long id)
            throws SQLException {

        String sql = """
                SELECT id,
                       sensor_id,
                       value,
                       recorded_at
                FROM greenhouse.change_history
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next()
                        ? Optional.of(mapRow(rs))
                        : Optional.empty();
            }
        }
    }

    public long insert(ChangeHistory history)
            throws SQLException {

        String sql = """
                INSERT INTO greenhouse.change_history
                (
                    sensor_id,
                    value,
                    recorded_at
                )
                VALUES (?, ?, ?)
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, history.getSensorId());
            ps.setBigDecimal(2, history.getValue());
            ps.setObject(3, history.getRecordedAt());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {

                if (keys.next()) {

                    long id = keys.getLong(1);

                    history.setId(id);

                    return id;
                }
            }

            throw new SQLException("Failed to get generated key");
        }
    }

    public boolean update(ChangeHistory history)
            throws SQLException {

        String sql = """
                UPDATE greenhouse.change_history
                SET sensor_id = ?,
                    value = ?,
                    recorded_at = ?
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, history.getSensorId());
            ps.setBigDecimal(2, history.getValue());
            ps.setObject(3, history.getRecordedAt());
            ps.setLong(4, history.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(long id)
            throws SQLException {

        String sql = """
                DELETE FROM greenhouse.change_history
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    private ChangeHistory mapRow(ResultSet rs)
            throws SQLException {

        return new ChangeHistory(
                rs.getLong("id"),
                rs.getInt("sensor_id"),
                rs.getBigDecimal("value"),
                rs.getObject(
                        "recorded_at",
                        OffsetDateTime.class)
        );
    }
}
