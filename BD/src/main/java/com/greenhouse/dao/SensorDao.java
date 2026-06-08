package com.greenhouse.dao;


import com.greenhouse.db.ConnectionManager;
import com.greenhouse.model.Sensor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SensorDao {

    public List<Sensor> findAll() throws SQLException {

        String sql = """
                SELECT id,
                       cell_id,
                       sensor_type,
                       calibration_date
                FROM greenhouse.sensors
                ORDER BY id
                """;

        List<Sensor> result = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }

        return result;
    }

    public Optional<Sensor> findById(int id) throws SQLException {

        String sql = """
                SELECT id,
                       cell_id,
                       sensor_type,
                       calibration_date
                FROM greenhouse.sensors
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    public int insert(Sensor sensor) throws SQLException {

        String sql = """
                INSERT INTO greenhouse.sensors
                (
                    cell_id,
                    sensor_type,
                    calibration_date
                )
                VALUES (?, ?, ?)
                """;

        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, sensor.getCellId());
            ps.setString(2, sensor.getSensorType());
            ps.setDate(3, Date.valueOf(sensor.getCalibrationDate()));

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {

                if (keys.next()) {

                    int id = keys.getInt(1);

                    sensor.setId(id);

                    return id;
                }
            }

            throw new SQLException("Failed to get generated key");
        }
    }

    public boolean update(Sensor sensor) throws SQLException {

        String sql = """
                UPDATE greenhouse.sensors
                SET cell_id = ?,
                    sensor_type = ?,
                    calibration_date = ?
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, sensor.getCellId());
            ps.setString(2, sensor.getSensorType());
            ps.setDate(3, Date.valueOf(sensor.getCalibrationDate()));
            ps.setInt(4, sensor.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {

        String sql = """
                DELETE FROM greenhouse.sensors
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    private Sensor mapRow(ResultSet rs) throws SQLException {

        return new Sensor(rs.getInt("id"), rs.getInt("cell_id"), rs.getString("sensor_type"), rs.getDate("calibration_date").toLocalDate());
    }
}
